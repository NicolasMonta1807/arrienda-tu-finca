package web.mates.arriendatufinca.dto;

import jakarta.persistence.Entity;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DepartmentDTO {
    private UUID id;
    private String name;
    private Set<MunicipalitySimpleDTO> municipalities;
}
