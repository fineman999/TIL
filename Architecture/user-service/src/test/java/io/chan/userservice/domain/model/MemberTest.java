package io.chan.userservice.domain.model;

import io.chan.userservice.domain.enums.UserRole;
import io.chan.userservice.domain.vo.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @DisplayName("Member 객체 생성 테스트")
    @Test
    void create() {
        Member member = getMember();

        assertThat(member.getMemberNo()).isEqualTo(1L);
    }

    @DisplayName("Member 객체 등록 테스트")
    @Test
    void register() {
        Member member = Member.register(
                IDName.create("1", "name"),
                Password.create("password"),
                Email.create("test@gamil.com"));

        Assertions.assertAll(
                () -> assertThat(member).isNotNull(),
                () -> assertThat(member.getPoint().getPointValue()).isZero(),
                () -> assertThat(member.getAuthorities().get(0).getRole()).isEqualTo(UserRole.USER)
        );
    }

    @DisplayName("Point 객체 더하기 테스트")
    @Test
    void savePoint() {
        Member member = getMember();

        member.savePoint(100L);

        assertThat(member.getPoint().getPointValue()).isEqualTo(100L);
    }

    @DisplayName("Point 객체 빼기 테스트")
    @Test
    void usePoint() {
        Member member = getMember();

        member.savePoint(100L);
        member.usePoint(50L);

        assertThat(member.getPoint().getPointValue()).isEqualTo(50L);
    }

    @NotNull
    private static Member getMember() {
        return Member.create(
                1L,
                IDName.create("1", "name"),
                Password.create("password"),
                Email.create("test@gamil.com"),
                List.of(Authority.create(UserRole.USER)),
                Point.createZero());
    }
}