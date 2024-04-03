package web.mates.arriendatufinca.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RequestUserDTO {
    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 100, message = "Name is too long")
    private String name;

    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 100, message = "Last name is too long")
    private String lastName;

    @Email(message = "Email is not valid")
    @NotBlank(message = "Email is required")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8)
    private String password;

    @Column(unique = true)
    @Size(min = 7, max = 10, message = "Phone number is not valid")
    private String phoneNumber;
}
