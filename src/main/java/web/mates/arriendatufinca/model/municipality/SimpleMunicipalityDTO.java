package web.mates.arriendatufinca.model.municipality;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SimpleMunicipalityDTO {
    private UUID id;
    private String name;
    private String departmentName;
}
