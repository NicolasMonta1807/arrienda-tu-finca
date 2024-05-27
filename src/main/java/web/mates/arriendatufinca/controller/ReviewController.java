package web.mates.arriendatufinca.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.mates.arriendatufinca.model.review.dto.MyReviewsDTO;
import web.mates.arriendatufinca.model.review.dto.NewReviewDTO;
import web.mates.arriendatufinca.model.review.dto.PopulatedReviewDTO;
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

    @GetMapping()
    public ResponseEntity<List<PopulatedReviewDTO>> getAll() {
        return new ResponseEntity<>(reviewService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PopulatedReviewDTO> getById(@NonNull @PathVariable UUID id) {
        return new ResponseEntity<>(reviewService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<MyReviewsDTO> getByUser() {
        return new ResponseEntity<>(reviewService.getByUser(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<PopulatedReviewDTO> create(@NonNull @Valid @RequestBody NewReviewDTO review) {
        return new ResponseEntity<>(reviewService.create(review), HttpStatus.CREATED);
    }
}
