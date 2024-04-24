package web.mates.arriendatufinca.dto;

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
