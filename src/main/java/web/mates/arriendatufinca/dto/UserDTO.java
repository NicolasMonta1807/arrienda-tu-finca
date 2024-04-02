package web.mates.arriendatufinca.dto;

import java.util.UUID;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDTO {
  private UUID id;
  private String name;
  private String lastName;
  private String email;
  private String phoneNumber;
}
