package web.mates.arriendatufinca.model.municipality;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import web.mates.arriendatufinca.model.department.Department;
import web.mates.arriendatufinca.model.property.Property;

import java.util.Set;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "municipality")
@SQLDelete(sql = "UPDATE municipality SET deleted = true WHERE id=?")
@SQLRestriction("deleted = false")
public class Municipality {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotEmpty(message = "Municipality name is required")
    @Size(min = 1, max = 128, message = "Name is too long")
    private String name;

    @ManyToOne
    @JoinColumn(name = "department", nullable = false)
    private Department department;

    @OneToMany(mappedBy = "municipality")
    private Set<Property> properties;

    private Boolean deleted = Boolean.FALSE;
}
