package web.mates.arriendatufinca.service;

import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import web.mates.arriendatufinca.dto.BookingDTO;
import web.mates.arriendatufinca.dto.ReviewDTO;
import web.mates.arriendatufinca.exceptions.InvalidBookingStateException;
import web.mates.arriendatufinca.model.Booking;
import web.mates.arriendatufinca.model.Review;
import web.mates.arriendatufinca.model.Status;
import web.mates.arriendatufinca.model.User;
import web.mates.arriendatufinca.repository.ReviewRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final BookingService bookingService;

    ReviewService(ReviewRepository reviewRepository, ModelMapper modelMapper, UserService userService, BookingService bookingService) {
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.bookingService = bookingService;
    }

    public List<ReviewDTO> getAll() {
        Iterable<Review> reviews = reviewRepository.findAll();
        List<ReviewDTO> reviewDTOS = new ArrayList<>();

        for (Review r : reviews) {
            reviewDTOS.add(getById(r.getId()));
        }
        return reviewDTOS;
    }

    public ReviewDTO getById(@NonNull UUID id) {
        Optional<Review> review = reviewRepository.findById(id);
        if (review.isPresent()) {
            ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);
            reviewDTO.setAuthorId(review.get().getAuthor().getId());
            reviewDTO.setBookingId(review.get().getBooking().getId());
            return reviewDTO;
        }
        return null;
    }

    public ReviewDTO create(@NonNull ReviewDTO reviewDTO) {
        BookingDTO booking = bookingService.getById(reviewDTO.getBookingId());
        if (booking.getStatus() != Status.REVIEW) {
            throw new InvalidBookingStateException("Booking is not in state of review");
        }

        Review newReview = modelMapper.map(reviewDTO, Review.class);

        newReview.setAuthor(
                modelMapper.map(
                        userService.getUserById(reviewDTO.getAuthorId()),
                        User.class));

        newReview.setBooking(
                modelMapper.map(
                        bookingService.getById(reviewDTO.getBookingId()),
                        Booking.class));

        reviewRepository.save(newReview);
        return getById(newReview.getId());
    }

    public ReviewDTO update(@NonNull UUID id, @NonNull ReviewDTO reviewDTO) {
        Optional<Review> review = reviewRepository.findById(id);
        if (review.isPresent()) {
            Review foundReview = review.get();
            foundReview.setComment(reviewDTO.getComment());
            foundReview.setRating(reviewDTO.getRating());
            reviewRepository.save(foundReview);
            return getById(foundReview.getId());
        } else
            return null;
    }

    public void delete(@NonNull UUID id) {
        reviewRepository.deleteById(id);
    }

}
