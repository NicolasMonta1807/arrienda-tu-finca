package web.mates.arriendatufinca.model.user.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SimpleUserDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

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

    @Column(unique = true)
    @Size(min = 7, max = 10, message = "Phone number is not valid")
    private String phoneNumber;
}
