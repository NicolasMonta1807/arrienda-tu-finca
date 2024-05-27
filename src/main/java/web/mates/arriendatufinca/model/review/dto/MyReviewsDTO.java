package web.mates.arriendatufinca.model.review.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MyReviewsDTO {
    private List<SimpleReviewDTO> asAuthor;
    private List<SimpleReviewDTO> asRated;
}
