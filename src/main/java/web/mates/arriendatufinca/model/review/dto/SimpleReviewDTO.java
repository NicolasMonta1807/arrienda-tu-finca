package web.mates.arriendatufinca.model.review.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SimpleReviewDTO {
    private UUID id;
    private double rating;
    private String comment;
    private UUID bookingId;
    private UUID authorId;
    private UUID ratedId;
}
