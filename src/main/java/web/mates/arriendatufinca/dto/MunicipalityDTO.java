package web.mates.arriendatufinca.dto;

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
    private String department;
    private String name;
}
