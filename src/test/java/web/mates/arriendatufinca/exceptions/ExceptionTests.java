package web.mates.arriendatufinca.exceptions;


import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import web.mates.arriendatufinca.ArriendatufincaApplication;
import web.mates.arriendatufinca.dto.*;
import web.mates.arriendatufinca.helper.TestVariables;
import web.mates.arriendatufinca.model.*;
import web.mates.arriendatufinca.repository.*;
import web.mates.arriendatufinca.service.*;

import java.awt.print.Book;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@SpringBootTest(classes = ArriendatufincaApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class ExceptionTests {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    MunicipalityRepository municipalityRepository;

    @Autowired
    MunicipalityService municipalityService;

    @Autowired
    PropertyRepository propertyRepository;

    @Autowired
    PropertyService propertyService;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    BookingService bookingService;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ReviewService reviewService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    EntityManager entityManager;

    private List<User> users = TestVariables.users;
    private final List<Municipality> municipalities = TestVariables.municipalities;
    private final List<Property> properties = TestVariables.properties;
    private List<Booking> bookings = TestVariables.bookings;
    private final List<Review> reviews = TestVariables.reviews;

    @Transactional
    void completelyDeleteData() {
        entityManager.joinTransaction();
        entityManager.createQuery("DELETE FROM Review ").executeUpdate();
        entityManager.createQuery("DELETE FROM Booking ").executeUpdate();
        entityManager.createQuery("DELETE FROM Property ").executeUpdate();
        entityManager.createQuery("DELETE FROM Municipality ").executeUpdate();
        entityManager.createQuery("DELETE FROM User ").executeUpdate();
    }

    @BeforeEach
    void setup() {
        this.users = TestVariables.users;
        this.bookings = TestVariables.bookings;
        completelyDeleteData();
    }

    @Test
    void UserService_RegisterExistingEmail_ThrowsDuplicateEmailException() {
        User user = users.get(0);
        RequestUserDTO requestUserDTO = modelMapper.map(user, RequestUserDTO.class);

        userService.newUser(requestUserDTO);

        Assertions.assertThatThrownBy(() -> userService.newUser(requestUserDTO))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessageContaining("Email already exists");
    }

    @Test
    void MunicipalityService_SaveExistingMunicipality_ThrowsDuplicateMunicipalityException() {
        Municipality municipality = municipalities.get(0);
        MunicipalityDTO municipalityDTO = modelMapper.map(municipality, MunicipalityDTO.class);

        municipalityService.create(municipalityDTO);

        Assertions.assertThatThrownBy(() -> municipalityService.create(municipalityDTO))
                .isInstanceOf(DuplicateMunicipalityException.class)
                .hasMessageContaining("Municipality already exists");
    }

    @Test
    void BookingService_CreateBookingWithWrongDates_ThrowsInvalidDateException() {
        MunicipalityDTO savedMunie = municipalityService.create(modelMapper.map(municipalities.get(0), MunicipalityDTO.class));
        UserDTO lessee = userService.newUser(modelMapper.map(users.get(0), RequestUserDTO.class));

        PropertyDTO propertyToSave = modelMapper.map(properties.get(0), PropertyDTO.class);
        propertyToSave.setOwnerID(lessee.getId());
        propertyToSave.setMunicipalityID(savedMunie.getId());

        PropertyDTO property = propertyService.newProperty(propertyToSave);

        Booking bookingToSend = this.bookings.get(0);

        Date date = new GregorianCalendar(2024, Calendar.MAY, 20).getTime();

        bookingToSend.setStartDate(date);
        bookingToSend.setEndDate(date);

        BookingDTO bookingDTO = modelMapper.map(bookingToSend, BookingDTO.class);
        bookingDTO.setLesseeId(lessee.getId());
        bookingDTO.setPropertyId(property.getId());

        Assertions.assertThatThrownBy(() -> bookingService.create(bookingDTO))
                .isInstanceOf(InvalidDateException.class)
                .hasMessageContaining("The end date should be at least one day after start date");


        Date yesterday = Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        bookingDTO.setStartDate(yesterday);

        Assertions.assertThatThrownBy(() -> bookingService.create(bookingDTO))
                .isInstanceOf(InvalidDateException.class)
                .hasMessageContaining("The start date cannot be earlier than the current date");
    }

    @Test
    void ReviewService_ReviewBookingWithNoState_ThrowsInvalidBookingStateException() {
        MunicipalityDTO savedMunie = municipalityService.create(modelMapper.map(municipalities.get(0), MunicipalityDTO.class));
        UserDTO lessee = userService.newUser(modelMapper.map(users.get(0), RequestUserDTO.class));

        PropertyDTO propertyToSave = modelMapper.map(properties.get(0), PropertyDTO.class);
        propertyToSave.setOwnerID(lessee.getId());
        propertyToSave.setMunicipalityID(savedMunie.getId());

        PropertyDTO property = propertyService.newProperty(propertyToSave);

        Date startDate = new GregorianCalendar(2024, Calendar.MAY, 20).getTime();
        Date endDate = new GregorianCalendar(2024, Calendar.MAY, 25).getTime();

        BookingDTO bookingDTO = BookingDTO.builder()
                .id(bookings.get(0).getId())
                .startDate(startDate)
                .endDate(endDate)
                .lesseeId(lessee.getId())
                .propertyId(property.getId())
                .status(Status.ACCEPTED)
                .build();

        BookingDTO savedBooking = bookingService.create(bookingDTO);

        ReviewDTO reviewToSend = modelMapper.map(reviews.get(0), ReviewDTO.class);
        reviewToSend.setAuthorId(lessee.getId());
        reviewToSend.setBookingId(savedBooking.getId());

        Assertions.assertThatThrownBy(() -> reviewService.create(reviewToSend))
                .isInstanceOf(InvalidBookingStateException.class)
                .hasMessageContaining("Booking is not in state of review");
    }
}
