package web.mates.arriendatufinca.model;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

    @Email
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

    private boolean deleted = Boolean.FALSE;
}
