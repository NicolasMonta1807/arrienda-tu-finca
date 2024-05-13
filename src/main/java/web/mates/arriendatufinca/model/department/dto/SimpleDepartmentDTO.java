package web.mates.arriendatufinca.model.department.dto;

import lombok.*;
import web.mates.arriendatufinca.model.municipality.SimpleMunicipalityDTO;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SimpleDepartmentDTO {
    private UUID id;
    private String name;
    private Set<SimpleMunicipalityDTO> municipalities;
}
