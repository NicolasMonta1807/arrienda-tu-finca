package web.mates.arriendatufinca.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import web.mates.arriendatufinca.dto.ReviewDTO;
import web.mates.arriendatufinca.service.ReviewService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping(value = {"", "/"})
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        return new ResponseEntity<>(reviewService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@NonNull @PathVariable UUID id) {
        return new ResponseEntity<>(reviewService.getById(id), HttpStatus.OK);
    }

    @PostMapping(value = {"", "/"})
    public ResponseEntity<ReviewDTO> createReview(@NonNull @Valid @RequestBody ReviewDTO review) {
        return new ResponseEntity<>(reviewService.create(review), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(@NonNull @PathVariable UUID id, @NonNull @Valid @RequestBody ReviewDTO review) {
        ReviewDTO updatedReview = reviewService.update(id, review);
        if (updatedReview == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "review not found");
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@NonNull @PathVariable UUID id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
