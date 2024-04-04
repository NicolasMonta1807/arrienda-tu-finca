package web.mates.arriendatufinca.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import web.mates.arriendatufinca.ArriendatufincaApplication;
import web.mates.arriendatufinca.dto.ReviewDTO;
import web.mates.arriendatufinca.helper.TestVariables;

@SpringBootTest(classes = ArriendatufincaApplication.class)
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class ReviewModelTests {
    @Autowired
    ModelMapper modelmapper;

    private static final Review testReview = TestVariables.reviews.get(0);
    private static ReviewDTO testReviewDTO;

    @BeforeAll
    public static void setup() {
        testReviewDTO = ReviewDTO.builder()
                .id(testReview.getId())
                .rating(testReview.getRating())
                .comment(testReview.getComment())
                .bookingId(testReview.getBooking().getId())
                .authorId(testReview.getAuthor().getId())
                .build();
    }

    @Test
    void ReviewModel_TestProperties() {
        Review review = Review.builder()
                .id(testReview.getId())
                .rating(testReview.getRating())
                .comment(testReview.getComment())
                .booking(testReview.getBooking())
                .author(testReview.getAuthor())
                .build();

        Assertions.assertThat(review)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(testReview);
    }

    @Test
    void ReviewDTO_TestMapping() {
        ReviewDTO reviewDTO = modelmapper.map(testReview, ReviewDTO.class);
        reviewDTO.setAuthorId(testReview.getAuthor().getId());
        reviewDTO.setBookingId(testReview.getBooking().getId());

        Assertions.assertThat(reviewDTO)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(testReviewDTO);
    }
}
