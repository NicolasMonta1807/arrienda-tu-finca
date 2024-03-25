package web.mates.arriendatufinca.model;

import java.util.UUID;

import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "property")
@SQLDelete(sql = "UPDATE property SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String description;

    private int rooms;

    private int bathrooms;

    private boolean petFriendly;

    private boolean pool;

    private boolean bbq;

    private int pricePerNight;

    private boolean deleted = Boolean.FALSE;

    @ManyToOne
    @JoinColumn(name = "owner", nullable = false)
    private User owner;

    @ManyToOne
    @JoinColumn(name = "municipality")
    private Municipality municipality;
}
