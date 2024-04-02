package web.mates.arriendatufinca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "review")
@SQLDelete(sql = "UPDATE review SET deleted = true WHERE id=?")
@SQLRestriction("deleted = false")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @DecimalMin("0.5")
    @DecimalMax("5.0")
    @NotNull(message = "Rating is required")
    private double rating;

    @NotEmpty(message = "Review comment is required")
    @Size(max = 1000, message = "Comment is too long")
    private String comment;

    @OneToOne
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "author", nullable = false)
    private User author;

    private boolean deleted = Boolean.FALSE;
}