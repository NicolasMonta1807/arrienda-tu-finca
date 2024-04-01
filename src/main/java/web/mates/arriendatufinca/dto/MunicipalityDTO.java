package web.mates.arriendatufinca.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MunicipalityDTO {
    private UUID id;

    @Column(unique = true)
    @NotEmpty(message = "Department is required")
    @Size(min = 1, max = 64, message = "Department is too long")
    private String department;

    @NotEmpty(message = "Municipality name is required")
    @Size(min = 1, max = 128, message = "Name is too long")
    private String name;
}
