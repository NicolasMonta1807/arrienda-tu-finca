package web.mates.arriendatufinca;

import web.mates.arriendatufinca.model.*;

import java.util.*;

public class TestVariables {
    public static final List<User> users = getUsers();
    public static final List<Municipality> municipalities = getMunicipalities();
    public static final List<Property> properties = getProperties();
    public static final List<Booking> bookings = getBookings();
    public static final List<Review> reviews = getReviews();

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

    private static List<Booking> getBookings() {
        List<Booking> bookings = new ArrayList<>();

        bookings.add(Booking.builder()
                .id(UUID.randomUUID())
                .startDate(new GregorianCalendar(2024, Calendar.APRIL, 21).getTime())
                .endDate(new GregorianCalendar(2024, Calendar.APRIL, 23).getTime())
                .guests(4)
                .lessee(users.get(0))
                .property(properties.get(0))
                .build());

        return bookings;
    }

    private static List<Review> getReviews() {
        List<Review> reviews = new ArrayList<>();

        reviews.add(Review.builder()
                .id(UUID.randomUUID())
                .rating(3.5)
                .comment("Muy buena pasadía. Todo muy limpio y agradable")
                .booking(bookings.get(0))
                .author(users.get(1))
                .build());

        return reviews;
    }
}
