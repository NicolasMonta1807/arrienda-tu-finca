package web.mates.arriendatufinca.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public List<ReviewDTO> getAllReviews() {
        return reviewService.getAll();
    }

    @GetMapping("/{id}")
    public ReviewDTO getReviewById(@NonNull @PathVariable UUID id) {
        return reviewService.getById(id);
    }

    @PostMapping(value = {"", "/"})
    public ReviewDTO createReview(@NonNull @Valid @RequestBody ReviewDTO review) {
        return reviewService.create(review);
    }

    @PutMapping("/{id}")
    public ReviewDTO updateReview(@NonNull @PathVariable UUID id, @NonNull @Valid @RequestBody ReviewDTO review) {
        return reviewService.update(id, review);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReview(@NonNull @PathVariable UUID id) {
        reviewService.delete(id);
        return ResponseEntity.ok().build();
    }
}
