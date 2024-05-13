package web.mates.arriendatufinca.model.property.dto;

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
public class NewPropertyDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @Min(value = 1, message = "Need at least one room")
    @NotNull(message = "Number of rooms is required")
    private int rooms;

    @Min(value = 1, message = "Need at least one bathroom")
    @NotNull(message = "Number of bathrooms is required")
    private int bathrooms;

    private boolean petFriendly;
    private boolean pool;
    private boolean bbq;

    @Min(value = 1, message = "Price per night is required")
    @NotNull(message = "Price per night is required")
    private int pricePerNight;

    @NotNull(message = "Property location is required")
    private UUID municipalityId;
}
