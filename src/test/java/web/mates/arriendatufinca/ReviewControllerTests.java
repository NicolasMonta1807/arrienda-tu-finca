package web.mates.arriendatufinca;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import web.mates.arriendatufinca.controller.ReviewController;
import web.mates.arriendatufinca.dto.BookingDTO;
import web.mates.arriendatufinca.dto.ReviewDTO;
import web.mates.arriendatufinca.model.Booking;
import web.mates.arriendatufinca.model.Review;
import web.mates.arriendatufinca.service.ReviewService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ArriendatufincaApplication.class
)
@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class ReviewControllerTests {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    @Autowired
    private ModelMapper modelMapper;

    private final List<Review> reviews = TestVariables.reviews;

    @Test
    void ReviewController_CreateReview_ReturnsReviewDTO() {
        Review reviewToCompare = this.reviews.get(0);

        ReviewDTO reviewDTO = modelMapper.map(reviewToCompare, ReviewDTO.class);

        given(reviewService.create(Mockito.any(ReviewDTO.class))).willReturn(reviewDTO);

        ResponseEntity<ReviewDTO> response = reviewController.createReview(reviewDTO);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ReviewDTO savedReview = response.getBody();

        Assertions.assertThat(savedReview)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(reviewDTO);
    }

    @Test
    void ReviewController_GetAllReviews_ReturnsListReviewDTO() {
        List<ReviewDTO> reviewsToCompare = new ArrayList<>();
        for (Review r : reviews) {
            reviewsToCompare.add(modelMapper.map(r, ReviewDTO.class));
        }

        given(reviewService.getAll()).willReturn(reviewsToCompare);

        ResponseEntity<List<ReviewDTO>> response = reviewController.getAllReviews();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<ReviewDTO> reviewsResponse = response.getBody();

        Assertions.assertThat(reviewsResponse).isNotNull();
        for (ReviewDTO reviewDTO : reviewsToCompare) {
            Assertions.assertThat(reviewsResponse).contains(reviewDTO);
        }
    }

    @Test
    void ReviewController_GetReviewById_ReturnsReviewDTO() {
        Review reviewToCompare = this.reviews.get(0);

        ReviewDTO expectedResult = modelMapper.map(reviewToCompare, ReviewDTO.class);

        given(reviewService.getById(Mockito.any(UUID.class))).willReturn(expectedResult);

        ResponseEntity<ReviewDTO> response = reviewController.getReviewById(reviewToCompare.getId());

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ReviewDTO review = response.getBody();

        Assertions.assertThat(review)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResult);
    }

    @Test
    void ReviewController_UpdateReview_ReturnsUpdatedReviewDTO() {
        Review reviewToCompare = this.reviews.get(0);

        double newRating = 4;
        String newComment = "Muy buena pasadía. Todo muy limpio y agradable. Buen ambiente de los vecinos";

        ReviewDTO previousReview = modelMapper.map(reviewToCompare, ReviewDTO.class);
        ReviewDTO updatedReview = modelMapper.map(reviewToCompare, ReviewDTO.class);
        updatedReview.setComment(newComment);
        updatedReview.setRating(newRating);

        given(reviewService.update(Mockito.any(UUID.class), Mockito.any(ReviewDTO.class))).willReturn(updatedReview);

        ResponseEntity<ReviewDTO> response = reviewController.updateReview(reviewToCompare.getId(), updatedReview);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ReviewDTO reviewDTO = response.getBody();

        Assertions.assertThat(reviewDTO)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updatedReview);
        Assertions.assertThat(reviewDTO.getComment()).isNotEqualTo(previousReview.getComment());
        Assertions.assertThat(reviewDTO.getRating()).isNotEqualTo(previousReview.getRating());
    }

    @Test
    void ReviewController_DeleteReview_ReturnsOk() {
        UUID randomId = UUID.randomUUID();

        ResponseEntity<Void> response = reviewController.deleteReview(randomId);
        verify(reviewService).delete(randomId);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
