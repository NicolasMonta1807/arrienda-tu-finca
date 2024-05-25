package web.mates.arriendatufinca.model.user.dto;

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
