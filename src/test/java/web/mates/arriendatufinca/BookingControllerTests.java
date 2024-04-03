package web.mates.arriendatufinca;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import web.mates.arriendatufinca.controller.BookingController;
import web.mates.arriendatufinca.dto.BookingDTO;
import web.mates.arriendatufinca.model.Booking;
import web.mates.arriendatufinca.service.BookingService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ArriendatufincaApplication.class
)
@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class BookingControllerTests {
    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @Autowired
    private ModelMapper modelMapper;

    private final List<Booking> bookings = TestVariables.bookings;

    @Test
    void BookingController_CreateBooking_ReturnsBookingDTO() {
        Booking bookingToCompare = this.bookings.get(0);

        BookingDTO bookingDTO = modelMapper.map(bookingToCompare, BookingDTO.class);

        given(bookingService.create(Mockito.any(BookingDTO.class))).willReturn(bookingDTO);

        ResponseEntity<BookingDTO> response = bookingController.createBooking(bookingDTO);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        BookingDTO savedBooking = response.getBody();

        Assertions.assertThat(savedBooking)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(bookingDTO);
    }

    @Test
    void PropertyController_GetAllProperties_ReturnsListPropertyDTO() {
        List<BookingDTO> bookingsToCompare = new ArrayList<>();
        for (Booking b : bookings) {
            bookingsToCompare.add(modelMapper.map(b, BookingDTO.class));
        }

        given(bookingService.getAll()).willReturn(bookingsToCompare);

        ResponseEntity<List<BookingDTO>> response = bookingController.getAllBookings();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<BookingDTO> propertiesResponse = response.getBody();

        Assertions.assertThat(propertiesResponse).isNotNull();

        for (BookingDTO bookingDTO : bookingsToCompare) {
            Assertions.assertThat(propertiesResponse).contains(bookingDTO);
        }
    }

    @Test
    void PropertyController_GetSinglePropertyById_ReturnsPropertyDTO() {
        Booking bookingToCompare = this.bookings.get(0);

        BookingDTO expectedResult = modelMapper.map(bookingToCompare, BookingDTO.class);

        given(bookingService.getById(Mockito.any(UUID.class))).willReturn(expectedResult);

        ResponseEntity<BookingDTO> response = bookingController.getBookingById(bookingToCompare.getId());

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        BookingDTO booking = response.getBody();

        Assertions.assertThat(booking)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResult);
    }

    @Test
    void PropertyController_UpdateProperty_ReturnsUpdatedPropertyDTO() {
        Booking bookingToCompare = this.bookings.get(0);

        int newGuests = 5;

        BookingDTO previousBooking = modelMapper.map(bookingToCompare, BookingDTO.class);
        BookingDTO updatedBooking = modelMapper.map(bookingToCompare, BookingDTO.class);
        updatedBooking.setGuests(newGuests);

        given(bookingService.update(Mockito.any(UUID.class), Mockito.any(BookingDTO.class))).willReturn(updatedBooking);

        ResponseEntity<BookingDTO> response = bookingController.updateBooking(bookingToCompare.getId(), updatedBooking);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        BookingDTO bookingDTO = response.getBody();

        Assertions.assertThat(bookingDTO)
                .isNotNull()
                .isEqualTo(updatedBooking);
        Assertions.assertThat(bookingDTO.getGuests()).isNotEqualTo(previousBooking.getGuests());
    }

    @Test
    void PropertyController_DeleteProperty_ReturnsOk() {
        UUID randomId = UUID.randomUUID();

        ResponseEntity<Void> response = bookingController.deleteBooking(randomId);
        verify(bookingService).delete(randomId);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
