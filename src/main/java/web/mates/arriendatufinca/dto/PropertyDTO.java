package web.mates.arriendatufinca.dto;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PropertyDTO {
    private UUID id;

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

    @NotNull(message = "Owner is required")
    private UUID ownerID;

    @NotNull(message = "Property location is required")
    private UUID municipalityID;
}
