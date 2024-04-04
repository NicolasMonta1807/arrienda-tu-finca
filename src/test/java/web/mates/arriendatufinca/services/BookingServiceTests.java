package web.mates.arriendatufinca.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import web.mates.arriendatufinca.ArriendatufincaApplication;
import web.mates.arriendatufinca.dto.BookingDTO;
import web.mates.arriendatufinca.dto.PropertyDTO;
import web.mates.arriendatufinca.dto.UserDTO;
import web.mates.arriendatufinca.helper.TestVariables;
import web.mates.arriendatufinca.model.Booking;
import web.mates.arriendatufinca.repository.BookingRepository;
import web.mates.arriendatufinca.service.BookingService;
import web.mates.arriendatufinca.service.PropertyService;
import web.mates.arriendatufinca.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ArriendatufincaApplication.class)
@RunWith(MockitoJUnitRunner.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class BookingServiceTests {
    @Mock
    BookingRepository bookingRepository;

    @Mock
    UserService userService;

    @Mock
    PropertyService propertyService;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    BookingService bookingService;

    private final List<Booking> bookings = TestVariables.bookings;

    @Test
    void BookingService_CreateBooking_ReturnsBookingDTO() {
        // Given
        Booking baseBooking = bookings.get(0);

        BookingDTO bookingDTO = modelMapper.map(baseBooking, BookingDTO.class);
        bookingDTO.setLesseeId(baseBooking.getLessee().getId());
        bookingDTO.setPropertyId(baseBooking.getProperty().getId());

        UserDTO lesseeDTO = modelMapper.map(baseBooking.getLessee(), UserDTO.class);
        PropertyDTO propertyDTO = modelMapper.map(baseBooking.getProperty(), PropertyDTO.class);

        when(userService.getUserById(any(UUID.class))).thenReturn(lesseeDTO);
        when(propertyService.getPropertyById(any(UUID.class))).thenReturn(propertyDTO);
        when(bookingRepository.save(any(Booking.class))).thenReturn(baseBooking);
        when(bookingRepository.findById(any(UUID.class))).thenReturn(Optional.of(baseBooking));

        // When
        BookingDTO savedBooking = bookingService.create(bookingDTO);

        // Then
        Assertions.assertThat(savedBooking)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(bookingDTO);

        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void BookingService_GetAllBookings_ReturnsListBookingDTO() {
        // Given
        when(bookingRepository.findAll()).thenReturn(bookings);
        for (Booking b : bookings) {
            when(bookingRepository.findById(b.getId())).thenReturn(Optional.of(b));
        }

        // When
        List<BookingDTO> response = bookingService.getAll();

        // Then
        Assertions.assertThat(response)
                .isNotNull()
                .hasSameSizeAs(bookings);

        for (int i = 0; i < response.size(); i++)
            Assertions.assertThat(response.get(i).getId()).isEqualTo(bookings.get(i).getId());

        verify(bookingRepository, times(bookings.size())).findById(any(UUID.class));
    }

    @Test
    void BookingService_GetBookingById_ReturnsBookingDTO() {
        // Given
        Booking baseBooking = bookings.get(0);
        UUID id = baseBooking.getId();
        when(bookingRepository.findById(any(UUID.class))).thenReturn(Optional.of(baseBooking));

        BookingDTO bookingDTO = modelMapper.map(baseBooking, BookingDTO.class);

        // When
        BookingDTO response = bookingService.getById(id);

        Assertions.assertThat(response)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(bookingDTO);
    }

    @Test
    void BookingService_UpdateBooking_ReturnsUpdatedBookingDTO() {
        // Given
        Booking baseBooking = bookings.get(0);
        UUID id = baseBooking.getId();

        int newGuests = 5;
        Booking updated = bookings.get(0);
        updated.setGuests(newGuests);

        when(bookingRepository.findById(any(UUID.class))).thenReturn(Optional.of(baseBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(updated);

        BookingDTO bookingDTO = modelMapper.map(updated, BookingDTO.class);

        // When
        BookingDTO newBooking = bookingService.update(id, bookingDTO);

        // Then
        Assertions.assertThat(newBooking)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(bookingDTO);
        Assertions.assertThat(newBooking.getGuests()).isEqualTo(newGuests);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void BookingService_DeleteBooking_ReturnsVoidAndDeletesBooking() {
        // Given
        Booking bookingToUse = bookings.get(0);
        when(bookingRepository.findById(any(UUID.class))).thenReturn(Optional.of(bookingToUse));

        // When
        bookingService.delete(bookingToUse.getId());

        // Then
        verify(bookingRepository, times(1)).deleteById(bookingToUse.getId());
    }
}