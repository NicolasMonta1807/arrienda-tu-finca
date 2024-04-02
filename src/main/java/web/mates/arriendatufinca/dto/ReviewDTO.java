package web.mates.arriendatufinca.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewDTO {
    private UUID id;

    @DecimalMin(value = "0.5", message = "Should have at least half a star")
    @DecimalMax(value = "5.0", message = "Should have at most 5 starts")
    @NotNull(message = "Rating is required")
    private double rating;

    @NotEmpty(message = "Review comment is required")
    @Size(max = 1000, message = "Comment is too long")
    private String comment;

    private UUID bookingId;

    private UUID authorId;
}
