package io.chan.userservice.domain.model;

import io.chan.userservice.domain.enums.UserRole;
import io.chan.userservice.domain.vo.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNo;
    @Embedded
    private IDName idName;
    @Embedded
    private Password password;
    @Embedded
    private Email email;
    @ElementCollection(fetch = FetchType.LAZY)
    private List<Authority> authorities = new ArrayList<>();
    @Embedded
    private Point point;

    public static Member create(Long memberNo, IDName idName, Password password, Email email, List<Authority> authorities, Point point) {
        return new Member(memberNo, idName, password, email, authorities, point);
    }

    public static Member register(IDName idName,Password password,Email email) {
        return new Member(null,
                idName,
                password,
                email,
                List.of(Authority.create(UserRole.USER)),
                Point.createZero());
    }

    public void savePoint(Long value) {
        this.point = this.point.add(value);
    }

    public void usePoint(Long value) {
        this.point = this.point.subtract(value);
    }
}
