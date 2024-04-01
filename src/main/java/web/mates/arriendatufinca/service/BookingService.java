package web.mates.arriendatufinca.service;

import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import web.mates.arriendatufinca.dto.BookingDTO;
import web.mates.arriendatufinca.model.Booking;
import web.mates.arriendatufinca.model.Property;
import web.mates.arriendatufinca.model.User;
import web.mates.arriendatufinca.repository.BookingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final PropertyService propertyService;

    BookingService(BookingRepository bookingRepository, ModelMapper modelMapper, UserService userService, PropertyService propertyService) {
        this.bookingRepository = bookingRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.propertyService = propertyService;
    }

    public List<BookingDTO> getAll() {
        Iterable<Booking> bookings = bookingRepository.findAll();
        List<BookingDTO> bookingDTOS = new ArrayList<>();

        for (Booking b : bookings) {
            bookingDTOS.add(getById(b.getId()));
        }
        return bookingDTOS;
    }

    public BookingDTO getById(@NonNull UUID id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isPresent()) {
            BookingDTO bookingDTO = modelMapper.map(booking.get(), BookingDTO.class);
            bookingDTO.setLesseeId(booking.get().getLessee().getId());
            bookingDTO.setPropertyId(booking.get().getProperty().getId());
            return bookingDTO;
        }
        return null;
    }

    public BookingDTO create(@NonNull BookingDTO bookingDTO) {
        Booking newBooking = modelMapper.map(bookingDTO, Booking.class);

        // TODO : Verify property guests capacity

        newBooking.setLessee(
                modelMapper.map(
                        userService.getUserById(bookingDTO.getLesseeId()),
                        User.class));

        newBooking.setProperty(
                modelMapper.map(
                        propertyService.getPropertyById(bookingDTO.getPropertyId()),
                        Property.class));

        bookingRepository.save(newBooking);
        return getById(newBooking.getId());
    }

    public BookingDTO update(@NonNull UUID id, @NonNull BookingDTO bookingDTO) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isPresent()) {
            Booking foundBooking = booking.get();
            foundBooking.setStatus(bookingDTO.getStatus());
            foundBooking.setGuests(bookingDTO.getGuests());
            bookingRepository.save(foundBooking);
            return getById(foundBooking.getId());
        } else
            return null;
    }

    public void delete(@NonNull UUID id) {
        bookingRepository.deleteById(id);
    }
}
