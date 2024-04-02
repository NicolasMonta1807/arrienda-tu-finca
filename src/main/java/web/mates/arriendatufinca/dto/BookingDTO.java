package web.mates.arriendatufinca.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import web.mates.arriendatufinca.model.Status;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingDTO {
    private UUID id;

    @NotNull(message = "A start date is required")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date startDate;

    @NotNull(message = "A end date is required")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date endDate;

    @Min(value = 1, message = "At least one guest is required")
    @NotNull(message = "Number of guests is required")
    private int guests;

    @NotNull(message = "Lessee requester is required")
    private UUID lesseeId;

    @NotNull(message = "Property requested is required")
    private UUID propertyId;

    @Enumerated(value = EnumType.STRING)
    private Status status = Status.ACTIVE;
}
