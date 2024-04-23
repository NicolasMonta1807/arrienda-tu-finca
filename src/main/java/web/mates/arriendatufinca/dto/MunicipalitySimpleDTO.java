package web.mates.arriendatufinca.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MunicipalitySimpleDTO {
    private UUID id;
    private String name;
}
