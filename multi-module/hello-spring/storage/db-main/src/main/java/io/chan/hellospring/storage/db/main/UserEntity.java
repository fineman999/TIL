package io.chan.hellospring.storage.db.main;

import io.chan.hellospring.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public static UserEntity fromDomain(final User user) {
        return new UserEntity(user.getId(), user.getName());
    }

    public User entityToDomain() {
        return User.of(id, name);
    }
}
