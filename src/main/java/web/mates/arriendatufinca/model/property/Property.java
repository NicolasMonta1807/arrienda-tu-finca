package web.mates.arriendatufinca.model.property;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import web.mates.arriendatufinca.model.municipality.Municipality;
import web.mates.arriendatufinca.model.user.User;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "property")
@SQLDelete(sql = "UPDATE property SET deleted = true WHERE id=?")
@SQLRestriction("deleted = false")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @Min(1)
    @NotNull(message = "Number of rooms is required")
    private int rooms;

    @Min(1)
    @NotNull(message = "Number of bathrooms is required")
    private int bathrooms;

    private boolean petFriendly;

    private boolean pool;

    private boolean bbq;

    @Min(1)
    @NotNull(message = "Price per night is required")
    private int pricePerNight;

    @ManyToOne
    @JoinColumn(name = "owner", nullable = false)
    private User owner;

    @ManyToOne
    @JoinColumn(name = "municipality", nullable = false)
    private Municipality municipality;

    private boolean deleted = Boolean.FALSE;
}
