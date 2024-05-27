package web.mates.arriendatufinca.model.review.dto;

import lombok.*;
import web.mates.arriendatufinca.model.booking.dto.SimpleBookingDTO;
import web.mates.arriendatufinca.model.user.dto.SimpleUserDTO;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PopulatedReviewDTO {
    private UUID id;
    private double rating;
    private String comment;
    private SimpleBookingDTO booking;
    private SimpleUserDTO author;
    private SimpleUserDTO rated;
}
