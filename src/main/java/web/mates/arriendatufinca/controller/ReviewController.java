package web.mates.arriendatufinca.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import web.mates.arriendatufinca.model.booking.dto.NewBookingDTO;
import web.mates.arriendatufinca.model.review.dto.MyReviewsDTO;
import web.mates.arriendatufinca.model.review.dto.NewReviewDTO;
import web.mates.arriendatufinca.model.review.dto.PopulatedReviewDTO;
import web.mates.arriendatufinca.service.ReviewService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/review")
@Tag(name = "Reviews", description = "Reviews' information")
@ApiResponse(
        responseCode = "403",
        description = "No Authorization Token",
        content = @Content
)
public class ReviewController {

    private final ReviewService reviewService;

    ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(
            summary = "All available reviews",
            description = "Get a list of the details for every existing review"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PopulatedReviewDTO.class))
            )
    })
    @GetMapping()
    public ResponseEntity<List<PopulatedReviewDTO>> getAll() {
        return new ResponseEntity<>(reviewService.getAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "Review by id",
            description = "Get a single review knowing its ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PopulatedReviewDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Review with given ID does not exist"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<PopulatedReviewDTO> getById(@NonNull @PathVariable UUID id) {
        return new ResponseEntity<>(reviewService.getById(id), HttpStatus.OK);
    }

    @Operation(
            summary = "All reviews from user",
            description = "Get all the reviews where user is involved either as author or rated"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "A set of lists. One containing the reviews where user is author, and the other where user is rated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MyReviewsDTO.class)
                    )
            )
    })
    @GetMapping("/me")
    public ResponseEntity<MyReviewsDTO> getByUser() {
        return new ResponseEntity<>(reviewService.getByUser(), HttpStatus.OK);
    }

    @Operation(
            summary = "New review",
            description = "Creates a review with current authenticated user as author"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Created review",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PopulatedReviewDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "One or more fields are bad formatted or were not included. OR Booking is not in state for review"
            )
    })
    @PostMapping()
    public ResponseEntity<PopulatedReviewDTO> create(
            @Parameter(
                    name = "review",
                    description = "New review object",
                    required = true,
                    schema = @Schema(implementation = NewReviewDTO.class)
            )
            @NonNull @Valid @RequestBody NewReviewDTO review
    ) {
        return new ResponseEntity<>(reviewService.create(review), HttpStatus.CREATED);
    }
}
