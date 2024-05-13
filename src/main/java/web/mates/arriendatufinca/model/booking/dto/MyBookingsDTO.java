package web.mates.arriendatufinca.model.booking.dto;

import lombok.*;


import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MyBookingsDTO {
    private List<SimpleBookingDTO> asLessee;
    private List<SimpleBookingDTO> asLessor;
}
