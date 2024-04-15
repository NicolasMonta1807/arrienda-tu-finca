package web.mates.arriendatufinca.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SignInDTO {
    private String email;
    private String password;
}
