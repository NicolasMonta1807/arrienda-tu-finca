package web.mates.arriendatufinca.dto;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PropertyInfoDTO {
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
    private String municipalityName;
    private String departmentName;
}
