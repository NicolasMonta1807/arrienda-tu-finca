package web.mates.arriendatufinca;

import web.mates.arriendatufinca.model.Municipality;
import web.mates.arriendatufinca.model.Property;
import web.mates.arriendatufinca.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestVariables {
    public static final List<User> users = getUsers();
    public static final List<Municipality> municipalities = getMunicipalities();
    public static final List<Property> properties = getProperties();

    private static List<User> getUsers() {
        List<User> users = new ArrayList<>();

        users.add(User.builder()
                .id(UUID.randomUUID())
                .name("John")
                .lastName("Doe")
                .email("mail@domain.com")
                .phoneNumber("3003898475")
                .password("pass1234")
                .build());

        users.add(User.builder()
                .id(UUID.randomUUID())
                .name("Jane")
                .lastName("Doe")
                .email("correo@mail.com")
                .phoneNumber("3123235206")
                .password("contra1234")
                .build());

        return users;
    }

    private static List<Municipality> getMunicipalities() {
        List<Municipality> municipalities = new ArrayList<>();
        municipalities.add(Municipality.builder()
                .id(UUID.randomUUID())
                .name("Bello")
                .department("Antioquia").build());
        municipalities.add(Municipality.builder()
                .id(UUID.randomUUID())
                .name("Ibagué")
                .department("Tolima").build());
        municipalities.add(Municipality.builder()
                .id(UUID.randomUUID())
                .name("Bogotá")
                .department("Bogotá").build());
        return municipalities;
    }

    private static List<Property> getProperties() {
        List<Property> properties = new ArrayList<>();

        properties.add(Property.builder()
                .id(UUID.randomUUID())
                .name("El paraiso")
                .description("La mejor finca de todas")
                .bbq(false)
                .pool(false)
                .rooms(2)
                .bathrooms(1)
                .petFriendly(true)
                .pricePerNight(25000)
                .municipality(municipalities.get(0))
                .owner(users.get(0))
                .build());

        properties.add(Property.builder()
                .id(UUID.randomUUID())
                .name("La caleta")
                .description("Disfruta de tus vacaciones")
                .bbq(true)
                .pool(true)
                .rooms(6)
                .bathrooms(3)
                .petFriendly(true)
                .pricePerNight(50000)
                .municipality(municipalities.get(1))
                .owner(users.get(1))
                .build());

        return properties;
    }
}