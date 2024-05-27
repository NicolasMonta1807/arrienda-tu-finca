package web.mates.arriendatufinca.model.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import web.mates.arriendatufinca.model.property.Property;
import web.mates.arriendatufinca.model.review.Review;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "user")
@SQLDelete(sql = "UPDATE user SET deleted = true WHERE id=?")
@SQLRestriction("deleted = false")
public class User {
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

    @NotBlank(message = "Password is required")
    @Size(min = 8)
    private String password;

    @Column(unique = true)
    @Size(min = 7, max = 10, message = "Phone number is not valid")
    private String phoneNumber;

    @OneToMany(mappedBy = "owner")
    private Set<Property> properties;

    @NotBlank
    private String role;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    @OneToMany(mappedBy = "author")
    private Set<Review> reviewsAsAuthor;

    @OneToMany(mappedBy = "rated")
    private Set<Review> reviewsAsRated;

    private boolean activated = Boolean.FALSE;

    private boolean deleted = Boolean.FALSE;
}
