package web.mates.arriendatufinca.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.mates.arriendatufinca.model.booking.dto.MyBookingsDTO;
import web.mates.arriendatufinca.model.booking.dto.NewBookingDTO;
import web.mates.arriendatufinca.model.booking.dto.SimpleBookingDTO;
import web.mates.arriendatufinca.model.status.Status;
import web.mates.arriendatufinca.model.status.dto.StatusUpdateDTO;
import web.mates.arriendatufinca.service.BookingService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/booking")
@Tag(name = "Bookings", description = "Bookings' information")
@ApiResponse(
        responseCode = "403",
        description = "No Authorization Token",
        content = @Content
)
public class BookingController {
    private final BookingService bookingService;

    BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Operation(
            summary = "All available bookings",
            description = "Get a list of the details for every existing booking"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleBookingDTO.class))
            )
    })
    @GetMapping()
    public ResponseEntity<List<SimpleBookingDTO>> getAll() {
        return new ResponseEntity<>(bookingService.getAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "Booking by id",
            description = "Get a single Booking knowing its ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SimpleBookingDTO.class))}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Booking with given ID does not exist",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<SimpleBookingDTO> getById(
            @Parameter(
                    name = "id",
                    in = ParameterIn.PATH,
                    description = "ID of user to be retrieved",
                    required = true,
                    schema = @Schema(implementation = UUID.class)
            )
            @NonNull @PathVariable UUID id
    ) {
        return new ResponseEntity<>(bookingService.getById(id), HttpStatus.OK);
    }

    @Operation(
            summary = "All bookings from user",
            description = "Get all the bookings where user is involved either as lessee or lessor"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "A set of two pairs. One containing the properties as lessee and another containing as lessor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MyBookingsDTO.class))
            )
    })
    @GetMapping("/me")
    public ResponseEntity<MyBookingsDTO> getByUser() {
        return new ResponseEntity<>(bookingService.getByUser(), HttpStatus.OK);
    }

    @Operation(
            summary = "New booking",
            description = "Creates a booking for current authenticated user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Created booking information. Booking lessee is set to current authenticated user given in Authorization Token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleBookingDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "One or more fields do not comply with current constraints",
                    content = @Content
            )
    })
    @PostMapping()
    public ResponseEntity<SimpleBookingDTO> create(
            @Parameter(
                    name = "booking",
                    description = "New booking information",
                    required = true,
                    schema = @Schema(implementation = NewBookingDTO.class)
            )
            @NonNull @Valid @RequestBody NewBookingDTO booking
    ) {
        return new ResponseEntity<>(bookingService.create(booking), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update booking",
            description = "Updates given ID booking information"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleBookingDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Given token does not belong to either booking's lessee or lessor",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<SimpleBookingDTO> update(
            @Parameter(
                    name = "id",
                    description = "ID of booking to be updated",
                    schema = @Schema(implementation = UUID.class),
                    required = true
            )
            @NonNull @PathVariable UUID id,
            @Parameter(
                    name = "property",
                    in = ParameterIn.QUERY,
                    description = "Updated property information",
                    schema = @Schema(implementation = NewBookingDTO.class),
                    required = true
            )
            @NonNull @Valid @RequestBody NewBookingDTO booking
    ) {
        return new ResponseEntity<>(bookingService.update(id, booking), HttpStatus.OK);
    }

    @Operation(
            summary = "Updated booking status",
            description = "Updates the given ID booking status"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Booking status was successfully updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SimpleBookingDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Booking not found",
                    content = @Content
            )
    })
    @PutMapping("/status/{id}")
    public ResponseEntity<SimpleBookingDTO> updateStatus(
            @Parameter(
                    name = "id",
                    description = "ID of booking to be updated",
                    schema = @Schema(implementation = UUID.class),
                    required = true
            )
            @NonNull @PathVariable UUID id,
            @Parameter(
                    name = "status",
                    description = "Booking's new status",
                    schema = @Schema(implementation = StatusUpdateDTO.class),
                    required = true,
                    in = ParameterIn.QUERY
            )
            @NonNull @RequestBody StatusUpdateDTO statusUpdate
    ) {
        bookingService.updateStatus(id, statusUpdate.getStatus());
        return new ResponseEntity<>(bookingService.getById(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete booking",
            description = "Mark the given booking as deleted"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Operation was completed",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Given token does not belong to either booking's lessee or lessor",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(
                    name = "id",
                    description = "ID of booking to be deleted",
                    schema = @Schema(implementation = UUID.class),
                    required = true
            )
            @NonNull @PathVariable UUID id
    ) {
        bookingService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
