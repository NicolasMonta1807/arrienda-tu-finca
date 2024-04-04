package web.mates.arriendatufinca.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import web.mates.arriendatufinca.ArriendatufincaApplication;
import web.mates.arriendatufinca.dto.BookingDTO;
import web.mates.arriendatufinca.helper.TestVariables;

@SpringBootTest(classes = ArriendatufincaApplication.class)
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class BookingModelTests {
    @Autowired
    ModelMapper modelmapper;

    private static final Booking testBooking = TestVariables.bookings.get(0);
    private static BookingDTO testBookingDTO;

    @BeforeAll
    public static void setup() {
        testBookingDTO = BookingDTO.builder()
                .id(testBooking.getId())
                .startDate(testBooking.getStartDate())
                .endDate(testBooking.getEndDate())
                .guests(testBooking.getGuests())
                .lesseeId(testBooking.getLessee().getId())
                .propertyId(testBooking.getProperty().getId())
                .status(testBooking.getStatus())
                .build();
    }

    @Test
    void BookingModel_TestProperties() {
        Booking booking = Booking.builder()
                .id(testBooking.getId())
                .startDate(testBooking.getStartDate())
                .endDate(testBooking.getEndDate())
                .guests(testBooking.getGuests())
                .lessee(testBooking.getLessee())
                .property(testBooking.getProperty())
                .status(testBooking.getStatus())
                .build();

        Assertions.assertThat(booking)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(testBooking);
    }

    @Test
    void BookingDTO_TestMapping() {
        BookingDTO bookingDTO = modelmapper.map(testBooking, BookingDTO.class);
        bookingDTO.setLesseeId(testBooking.getLessee().getId());
        bookingDTO.setPropertyId(testBooking.getProperty().getId());

        Assertions.assertThat(bookingDTO)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(testBookingDTO);
    }
}
