package web.mates.arriendatufinca.dto;

import java.util.UUID;

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
    private String name;
    private String description;
    private int rooms;
    private int bathrooms;
    private boolean petFriendly;
    private boolean pool;
    private boolean bbq;
    private int pricePerNight;
    private UUID ownerID;
    private UUID municipalityID;
}
