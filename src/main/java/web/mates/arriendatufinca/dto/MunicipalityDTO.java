package web.mates.arriendatufinca.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MunicipalityDTO {
    private UUID id;

    @NotNull(message = "Department is required")
    private UUID departmentId;

    @NotEmpty(message = "Municipality name is required")
    @Size(min = 1, max = 128, message = "Name is too long")
    private String name;
}
