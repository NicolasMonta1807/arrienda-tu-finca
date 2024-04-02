package web.mates.arriendatufinca.model;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.SQLDelete;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

    @OneToMany(mappedBy = "property")
    private Set<Booking> bookings;

    private boolean deleted = Boolean.FALSE;
}
