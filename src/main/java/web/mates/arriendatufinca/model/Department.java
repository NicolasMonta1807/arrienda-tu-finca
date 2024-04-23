package web.mates.arriendatufinca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.Set;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "department")
@SQLDelete(sql = "UPDATE department SET deleted = true WHERE id=?")
@SQLRestriction("deleted = false")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotEmpty(message = "Department name is required")
    @Size(min = 1, max = 128, message = "Name is too long")
    private String name;

    @OneToMany(mappedBy = "department")
    private Set<Municipality> municipalities;

    private Boolean deleted = Boolean.FALSE;
}
