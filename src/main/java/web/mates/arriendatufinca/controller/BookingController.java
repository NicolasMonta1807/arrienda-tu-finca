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
@CrossOrigin
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping(value = {"", "/"})
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        return new ResponseEntity<>(bookingService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBookingById(@NonNull @PathVariable UUID id) {
        BookingDTO booking = bookingService.getById(id);
        if (booking == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "booking not found");
        }
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }

    @PostMapping(value = {"", "/"})
    public ResponseEntity<BookingDTO> createBooking(@NonNull @Valid @RequestBody BookingDTO booking) {
        return new ResponseEntity<>(bookingService.create(booking), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingDTO> updateBooking(@NonNull @PathVariable UUID id, @NonNull @Valid @RequestBody BookingDTO booking) {
        return new ResponseEntity<>(bookingService.update(id, booking), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@NonNull @PathVariable UUID id) {
        bookingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
