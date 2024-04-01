package web.mates.arriendatufinca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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
@Table(name = "municipality", uniqueConstraints = {@UniqueConstraint(columnNames = {"department", "name"})})
@SQLDelete(sql = "UPDATE municipality SET deleted = true WHERE id=?")
@SQLRestriction("deleted = false")
public class Municipality {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    @NotEmpty(message = "Department is required")
    @Size(min = 1, max = 64, message = "Department is too long")
    private String department;

    @NotEmpty(message = "Municipality name is required")
    @Size(min = 1, max = 128, message = "Name is too long")
    private String name;

    @OneToMany(mappedBy = "municipality")
    private Set<Property> properties;

    private Boolean deleted = Boolean.FALSE;
}
