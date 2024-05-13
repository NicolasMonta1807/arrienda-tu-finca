package web.mates.arriendatufinca.model.property.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoginDTO {
    private String email;
    private String password;
}
