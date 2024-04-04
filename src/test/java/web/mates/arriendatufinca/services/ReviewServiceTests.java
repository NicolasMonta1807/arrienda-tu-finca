package web.mates.arriendatufinca.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import web.mates.arriendatufinca.ArriendatufincaApplication;
import web.mates.arriendatufinca.dto.BookingDTO;
import web.mates.arriendatufinca.dto.ReviewDTO;
import web.mates.arriendatufinca.dto.UserDTO;
import web.mates.arriendatufinca.helper.TestVariables;
import web.mates.arriendatufinca.model.Review;
import web.mates.arriendatufinca.repository.ReviewRepository;
import web.mates.arriendatufinca.service.BookingService;
import web.mates.arriendatufinca.service.ReviewService;
import web.mates.arriendatufinca.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ArriendatufincaApplication.class)
@RunWith(MockitoJUnitRunner.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class ReviewServiceTests {
    @Mock
    ReviewRepository reviewRepository;

    @Mock
    UserService userService;

    @Mock
    BookingService bookingService;

    @Spy
    ModelMapper modelMapper;

    @InjectMocks
    ReviewService reviewService;

    private final List<Review> reviews = TestVariables.reviews;

    @Test
    void ReviewService_CreateReview_ReturnsReviewDTO() {
        // Given
        Review baseReview = reviews.get(0);

        ReviewDTO reviewDTO = modelMapper.map(baseReview, ReviewDTO.class);
        reviewDTO.setBookingId(baseReview.getBooking().getId());
        reviewDTO.setAuthorId(baseReview.getAuthor().getId());

        UserDTO authorDTO = modelMapper.map(baseReview.getAuthor(), UserDTO.class);
        BookingDTO bookingDTO = modelMapper.map(baseReview.getBooking(), BookingDTO.class);

        when(userService.getUserById(any(UUID.class))).thenReturn(authorDTO);
        when(bookingService.getById(any(UUID.class))).thenReturn(bookingDTO);
        when(reviewRepository.save(any(Review.class))).thenReturn(baseReview);
        when(reviewRepository.findById(any(UUID.class))).thenReturn(Optional.of(baseReview));

        // When
        ReviewDTO savedReview = reviewService.create(reviewDTO);

        // Then
        Assertions.assertThat(savedReview)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(reviewDTO);

        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void ReviewService_GetAllReviews_ReturnsListReviewDTO() {
        // Given
        when(reviewRepository.findAll()).thenReturn(reviews);
        for (Review r : reviews)
            when(reviewRepository.findById(r.getId())).thenReturn(Optional.of(r));

        // When
        List<ReviewDTO> response = reviewService.getAll();

        // Then
        Assertions.assertThat(response)
                .isNotNull()
                .hasSameSizeAs(reviews);

        for (int i = 0; i < response.size(); i++)
            Assertions.assertThat(response.get(i).getId()).isEqualTo(reviews.get(i).getId());

        verify(reviewRepository, times(reviews.size())).findById(any(UUID.class));
    }

    @Test
    void ReviewService_GetReviewById_ReturnsReviewDTO() {
        // Given
        Review baseReview = reviews.get(0);
        UUID id = baseReview.getId();
        when(reviewRepository.findById(any(UUID.class))).thenReturn(Optional.of(baseReview));

        ReviewDTO reviewDTO = modelMapper.map(baseReview, ReviewDTO.class);

        // When
        ReviewDTO response = reviewService.getById(id);

        // Then
        Assertions.assertThat(response)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(reviewDTO);
    }

    @Test
    void ReviewService_UpdateReview_ReturnsReviewDTO() {
        // Given
        Review baseReview = reviews.get(0);
        UUID id = baseReview.getId();

        double newRating = 4;
        String newComment = "Muy buena pasadía. Todo muy limpio y agradable. Buen ambiente de los vecinos";
        Review updated = reviews.get(0);
        updated.setRating(newRating);
        updated.setComment(newComment);

        when(reviewRepository.findById(any(UUID.class))).thenReturn(Optional.of(baseReview));
        when(reviewRepository.save(any(Review.class))).thenReturn(updated);

        ReviewDTO reviewDTO = modelMapper.map(updated, ReviewDTO.class);

        // When
        ReviewDTO newReview = reviewService.update(id, reviewDTO);

        // Then
        Assertions.assertThat(newReview)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(reviewDTO);
        Assertions.assertThat(newReview.getRating()).isEqualTo(newRating);
        Assertions.assertThat(newReview.getComment()).isEqualTo(newComment);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void ReviewService_DeleteReview_ReturnsReviewDTO() {
        // Given
        Review reviewToUse = reviews.get(0);
        when(reviewRepository.findById(any(UUID.class))).thenReturn(Optional.of(reviewToUse));

        // When
        reviewService.delete(reviewToUse.getId());

        // Then
        verify(reviewRepository, times(1)).deleteById(reviewToUse.getId());
    }
}
