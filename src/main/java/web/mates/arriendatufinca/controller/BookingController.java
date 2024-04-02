package web.mates.arriendatufinca.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import web.mates.arriendatufinca.dto.BookingDTO;
import web.mates.arriendatufinca.service.BookingService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping(value = {"", "/"})
    public List<BookingDTO> getAllBookings() {
        return bookingService.getAll();
    }

    @GetMapping("/{id}")
    public BookingDTO getBookingById(@NonNull @PathVariable UUID id) {
        BookingDTO booking = bookingService.getById(id);
        if (booking == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "booking not found");
        }
        return booking;
    }

    @PostMapping(value = {"", "/"})
    public BookingDTO createBooking(@NonNull @Valid @RequestBody BookingDTO booking) {
        return bookingService.create(booking);
    }

    @PutMapping("/{id}")
    public BookingDTO updateBooking(@NonNull @PathVariable UUID id, @NonNull @Valid @RequestBody BookingDTO booking) {
        return bookingService.update(id, booking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(@NonNull @PathVariable UUID id) {
        bookingService.delete(id);
        return ResponseEntity.ok().build();
    }
}
