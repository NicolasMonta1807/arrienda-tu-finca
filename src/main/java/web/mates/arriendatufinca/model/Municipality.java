package web.mates.arriendatufinca.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.Set;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "municipality")
@SQLDelete(sql = "UPDATE municipality SET deleted = true WHERE id=?")
@SQLRestriction("deleted = false")
public class Municipality {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String department;

    private String name;

    @OneToMany(mappedBy = "municipality")
    private Set<Property> properties;

    private Boolean deleted = Boolean.FALSE;
}
