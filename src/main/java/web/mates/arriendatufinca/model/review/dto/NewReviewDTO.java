package web.mates.arriendatufinca.model.review.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NewReviewDTO {
    @DecimalMin("0.5")
    @DecimalMax("5.0")
    @NotNull(message = "Rating is required")
    private double rating;

    @NotEmpty(message = "Review comment is required")
    @Size(max = 1024, message = "Comment is too long")
    private String comment;

    @NotNull(message = "Booking is required")
    private UUID bookingId;
}
