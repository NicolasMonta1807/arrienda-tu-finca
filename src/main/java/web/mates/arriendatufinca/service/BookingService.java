package web.mates.arriendatufinca.service;

import jakarta.validation.Valid;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import web.mates.arriendatufinca.exceptions.EntityNotFoundException;
import web.mates.arriendatufinca.exceptions.InvalidBookingException;
import web.mates.arriendatufinca.exceptions.UnauthorizedException;
import web.mates.arriendatufinca.model.booking.Booking;
import web.mates.arriendatufinca.model.booking.dto.MyBookingsDTO;
import web.mates.arriendatufinca.model.booking.dto.NewBookingDTO;
import web.mates.arriendatufinca.model.booking.dto.SimpleBookingDTO;
import web.mates.arriendatufinca.model.property.Property;
import web.mates.arriendatufinca.model.status.Status;
import web.mates.arriendatufinca.model.user.User;
import web.mates.arriendatufinca.repository.BookingRepository;
import web.mates.arriendatufinca.security.jwt.JWTFilter;

import java.util.*;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final PropertyService propertyService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final JWTFilter jwtFilter;

    BookingService(BookingRepository bookingRepository, PropertyService propertyService, UserService userService, ModelMapper modelMapper, JWTFilter jwtFilter) {
        this.bookingRepository = bookingRepository;
        this.propertyService = propertyService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.jwtFilter = jwtFilter;
    }

    private Boolean checkAuth(String emailToCheck) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName().equalsIgnoreCase(emailToCheck);
    }

    public List<SimpleBookingDTO> getAll() {
        Iterable<Booking> bookings = bookingRepository.findAll();
        List<SimpleBookingDTO> bookingDTOS = new ArrayList<>();

        for (Booking b : bookings) {
            SimpleBookingDTO bookingDTO = modelMapper.map(b, SimpleBookingDTO.class);
            bookingDTO.setLesseeId(b.getLessee().getId());
            bookingDTO.setPropertyId(b.getProperty().getId());
            bookingDTOS.add(bookingDTO);
        }

        return bookingDTOS;
    }

    public SimpleBookingDTO getById(@NonNull UUID id) {
        Optional<Booking> foundBooking = bookingRepository.findById(id);
        if (foundBooking.isEmpty())
            throw new EntityNotFoundException("Booking not found");

        SimpleBookingDTO bookingDTO = modelMapper.map(foundBooking.get(), SimpleBookingDTO.class);
        Booking booking = foundBooking.get();

        bookingDTO.setLesseeId(booking.getLessee().getId());
        bookingDTO.setPropertyId(booking.getProperty().getId());
        return bookingDTO;
    }

    public MyBookingsDTO getByUser() {
        UUID authId = jwtFilter.getAuthId();
        Iterable<Booking> bookingsAsLessee = bookingRepository.findByUserAsLessee(authId);
        Iterable<Booking> bookingsAsLessor = bookingRepository.findByUserAsLessor(authId);

        List<SimpleBookingDTO> bookingDTOSAsLessee = new ArrayList<>();
        List<SimpleBookingDTO> bookingDTOSAsLessor = new ArrayList<>();

        for (Booking b : bookingsAsLessee)
            bookingDTOSAsLessee.add(getById(b.getId()));

        for (Booking b : bookingsAsLessor)
            bookingDTOSAsLessor.add(getById(b.getId()));


        MyBookingsDTO result = MyBookingsDTO.builder().build();
        result.setAsLessee(bookingDTOSAsLessee);
        result.setAsLessor(bookingDTOSAsLessor);
        return result;
    }

    private void validateBooking(Booking booking, Property property) {
        Date startDate = booking.getStartDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date dateToCompare = calendar.getTime();

        if (booking.getStartDate().before(new Date()))
            throw new InvalidBookingException("The start date cannot be earlier than the current date");

        if (booking.getEndDate().before(dateToCompare))
            throw new InvalidBookingException("The end date should be at least one day after start date");

        if (booking.getGuests() > property.getRooms() * 3)
            throw new InvalidBookingException("This property can not host so many guests");
    }

    public SimpleBookingDTO create(@NonNull @Valid NewBookingDTO booking) {
        UUID authId = jwtFilter.getAuthId();
        Property property = modelMapper.map(propertyService.getById(booking.getPropertyId()), Property.class);
        Booking bookingToSave = modelMapper.map(booking, Booking.class);
        bookingToSave.setLessee(modelMapper.map(
                userService.getById(authId),
                User.class
        ));
        bookingToSave.setProperty(property);

        Booking saved = bookingRepository.save(bookingToSave);
        return getById(saved.getId());
    }

    public SimpleBookingDTO update(@NonNull UUID id, @NonNull @Valid NewBookingDTO booking) {
        Optional<Booking> foundBooking = bookingRepository.findById(id);
        UUID authId = jwtFilter.getAuthId();
        if (foundBooking.isEmpty())
            throw new EntityNotFoundException("Booking not found");

        // Check if either lessee or lessor is updating
        if (!checkAuth(foundBooking.get().getLessee().getEmail()) &&
                !checkAuth(foundBooking.get().getProperty().getOwner().getEmail()))
            throw new UnauthorizedException();

        Booking bookingToSave = modelMapper.map(booking, Booking.class);
        Property property = modelMapper.map(propertyService.getById(booking.getPropertyId()), Property.class);

        validateBooking(bookingToSave, property);

        bookingToSave.setLessee(modelMapper.map(
                userService.getById(authId),
                User.class
        ));
        bookingToSave.setProperty(property);
        bookingToSave.setStartDate(booking.getStartDate());
        bookingToSave.setEndDate(booking.getStartDate());
        bookingToSave.setGuests(booking.getGuests());
        bookingToSave.setStatus(booking.getStatus());

        return modelMapper.map(bookingRepository.save(bookingToSave), SimpleBookingDTO.class);
    }

    public void delete(@NonNull UUID id) {
        Optional<Booking> bookingToDelete = bookingRepository.findById(id);
        if (bookingToDelete.isEmpty())
            return;

        // Check if either lessee or lessor is updating
        if (!checkAuth(bookingToDelete.get().getLessee().getEmail()) &&
                !checkAuth(bookingToDelete.get().getProperty().getOwner().getEmail()))
            throw new UnauthorizedException();

        bookingRepository.deleteById(id);
    }

    public void updateStatus(UUID id, Status status) {
        Optional<Booking> foundBooking = bookingRepository.findById(id);

        if(foundBooking.isEmpty())
            throw new EntityNotFoundException("Booking not found");

        Booking booking = foundBooking.get();
        booking.setStatus(status);
        bookingRepository.save(booking);
    }
}
