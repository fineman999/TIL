package io.chan.userservice.domain.model;

import io.chan.userservice.domain.enums.UserRole;
import io.chan.userservice.domain.vo.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @DisplayName("Member 객체 생성 테스트")
    @Test
    void create() {
        Member member = Member.create(
                1L,
                IDName.create("1", "name"),
                Password.create("password"),
                Email.create("test@gamil.com"),
                List.of(Authority.create(UserRole.USER)),
                Point.createZero());

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
}