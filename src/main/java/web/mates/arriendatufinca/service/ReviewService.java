package web.mates.arriendatufinca.service;

import jakarta.validation.Valid;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import web.mates.arriendatufinca.exceptions.EntityNotFoundException;
import web.mates.arriendatufinca.exceptions.InvalidBookingStatusException;
import web.mates.arriendatufinca.model.booking.Booking;
import web.mates.arriendatufinca.model.booking.dto.SimpleBookingDTO;
import web.mates.arriendatufinca.model.property.Property;
import web.mates.arriendatufinca.model.review.Review;
import web.mates.arriendatufinca.model.review.dto.MyReviewsDTO;
import web.mates.arriendatufinca.model.review.dto.NewReviewDTO;
import web.mates.arriendatufinca.model.review.dto.PopulatedReviewDTO;
import web.mates.arriendatufinca.model.review.dto.SimpleReviewDTO;
import web.mates.arriendatufinca.model.status.Status;
import web.mates.arriendatufinca.model.user.User;
import web.mates.arriendatufinca.model.user.dto.SimpleUserDTO;
import web.mates.arriendatufinca.repository.PropertyRepository;
import web.mates.arriendatufinca.repository.ReviewRepository;
import web.mates.arriendatufinca.security.jwt.JWTFilter;

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
    private final JWTFilter jwtFilter;
    private final PropertyRepository propertyRepository;

    ReviewService(ReviewRepository reviewRepository, ModelMapper modelMapper, UserService userService, BookingService bookingService, JWTFilter jwtFilter, PropertyRepository propertyRepository) {
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.bookingService = bookingService;
        this.jwtFilter = jwtFilter;
        this.propertyRepository = propertyRepository;
    }

    public List<PopulatedReviewDTO> getAll() {
        Iterable<Review> reviews = reviewRepository.findAll();
        List<PopulatedReviewDTO> reviewDTOS = new ArrayList<>();

        for (Review r : reviews) {
            PopulatedReviewDTO reviewDTO = modelMapper.map(r, PopulatedReviewDTO.class);
            reviewDTO.setAuthor(modelMapper.map(r.getAuthor(), SimpleUserDTO.class));
            reviewDTO.setRated(modelMapper.map(r.getRated(), SimpleUserDTO.class));
            reviewDTO.setBooking(modelMapper.map(r.getBooking(), SimpleBookingDTO.class));
            reviewDTOS.add(reviewDTO);
        }

        return reviewDTOS;
    }

    public PopulatedReviewDTO getById(@NonNull UUID id) {
        Optional<Review> review = reviewRepository.findById(id);

        if (review.isEmpty())
            throw new EntityNotFoundException("Review not found");

        Review r = review.get();

        PopulatedReviewDTO reviewDTO = modelMapper.map(r, PopulatedReviewDTO.class);
        reviewDTO.setAuthor(userService.getById(r.getAuthor().getId()));
        reviewDTO.setRated(userService.getById(r.getRated().getId()));
        reviewDTO.setBooking(bookingService.getById(r.getBooking().getId()));
        return reviewDTO;
    }

    public PopulatedReviewDTO create(@NonNull @Valid NewReviewDTO review) {
        UUID authId = jwtFilter.getAuthId();

        SimpleBookingDTO booking = bookingService.getById(review.getBookingId());

        if (booking.getStatus() != Status.REVIEW)
            throw new InvalidBookingStatusException("Booking is not in REVIEW status");

        Review newReview = modelMapper.map(review, Review.class);

        newReview.setBooking(modelMapper.map(
                booking,
                Booking.class));

        newReview.setAuthor(modelMapper.map(
                userService.getById(authId),
                User.class
        ));

        Optional<Property> property = propertyRepository.findById(booking.getPropertyId());

        if (property.isEmpty())
            throw new EntityNotFoundException("You are reviewing for a non existing property");

        if (authId.equals(booking.getLesseeId())) {
            newReview.setRated(property.get().getOwner());
        } else {
            newReview.setRated(
                    modelMapper.map(userService.getById(booking.getLesseeId()),
                            User.class
                    ));
        }

        Review saved = reviewRepository.save(newReview);
        bookingService.updateStatus(booking.getId(), Status.CLOSED);
        return getById(saved.getId());
    }

    public MyReviewsDTO getByUser() {
        UUID authId = jwtFilter.getAuthId();
        Iterable<Review> reviewsAsAuthor = reviewRepository.findByUserAsAuthor(authId);
        Iterable<Review> reviewsAsRated = reviewRepository.findByUserAsRated(authId);

        List<SimpleReviewDTO> reviewDTOSAsAuthor = mapToSimpleDTO(reviewsAsAuthor);
        List<SimpleReviewDTO> reviewDTOSAsRated = mapToSimpleDTO(reviewsAsRated);

        return MyReviewsDTO.builder()
                .asAuthor(reviewDTOSAsAuthor)
                .asRated(reviewDTOSAsRated)
                .build();
    }

    private List<SimpleReviewDTO> mapToSimpleDTO(Iterable<Review> reviewsAsRated) {
        List<SimpleReviewDTO> reviewDTOS = new ArrayList<>();
        for (Review r : reviewsAsRated) {
            SimpleReviewDTO reviewDTO = modelMapper.map(r, SimpleReviewDTO.class);
            reviewDTO.setAuthorId(r.getAuthor().getId());
            reviewDTO.setBookingId(r.getBooking().getId());
            reviewDTO.setRatedId(r.getRated().getId());
            reviewDTOS.add(reviewDTO);
        }
        return reviewDTOS;
    }
}
