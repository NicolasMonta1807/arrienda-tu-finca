package web.mates.arriendatufinca.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MunicipalityInfoDTO {
    private UUID id;
    private String name;
    private String departmentName;
}
