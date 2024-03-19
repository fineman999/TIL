package io.chan.userservice.domain.model;

import io.chan.userservice.domain.enums.UserRole;
import io.chan.userservice.domain.vo.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member {
    private Long memberNo;
    private IDName idName;
    private Password password;
    private Email email;
    private List<Authority> authorities = new ArrayList<>();
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
