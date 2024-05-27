package web.mates.arriendatufinca.model.review;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import web.mates.arriendatufinca.model.booking.Booking;
import web.mates.arriendatufinca.model.user.User;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
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
    @Size(max = 1024, message = "Comment is too long")
    private String comment;

    @OneToOne
    private Booking booking;

    @OneToOne
    private User author;

    @OneToOne
    private User rated;

    private boolean deleted = Boolean.FALSE;
}
