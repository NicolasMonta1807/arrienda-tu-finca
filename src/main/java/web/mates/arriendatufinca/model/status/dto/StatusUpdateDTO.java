package web.mates.arriendatufinca.model.status.dto;

import lombok.*;
import web.mates.arriendatufinca.model.status.Status;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class StatusUpdateDTO {
    private Status status;
}
