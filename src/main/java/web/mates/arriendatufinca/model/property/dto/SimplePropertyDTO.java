package web.mates.arriendatufinca.model.property.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SimplePropertyDTO {
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

    @NotNull(message = "Municipality is required")
    private String municipalityName;

    @NotNull(message = "Department is required")
    private String departmentName;
}
