package web.mates.arriendatufinca.model.booking;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.format.annotation.DateTimeFormat;
import web.mates.arriendatufinca.model.property.Property;
import web.mates.arriendatufinca.model.status.Status;
import web.mates.arriendatufinca.model.user.User;

import java.util.Date;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "booking")
@SQLDelete(sql = "UPDATE booking SET deleted = true WHERE id=?")
@SQLRestriction("deleted = false")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "A start date is required")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date startDate;

    @NotNull(message = "A end date is required")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date endDate;

    @Min(value = 1, message = "At least one guest is required")
    @NotNull(message = "Number of guests is required")
    private int guests;

    @ManyToOne
    @JoinColumn(name = "lessee", nullable = false)
    private User lessee;

    @ManyToOne
    @JoinColumn(name = "property", nullable = false)
    private Property property;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    private boolean deleted = Boolean.FALSE;
}
