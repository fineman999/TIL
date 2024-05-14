package io.chan.springbatchtestservice;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.datafaker.Faker;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Customer{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    String firstName;
    @Column(nullable = false)
    String lastName;
    @Column(nullable = false)
    LocalDateTime birthDate;

    public static Customer from(final Faker faker) {
        return Customer.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .birthDate(LocalDateTime.now())
                .build();
    }
}
