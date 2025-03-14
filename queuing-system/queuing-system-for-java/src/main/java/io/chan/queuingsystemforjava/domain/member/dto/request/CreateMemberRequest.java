package io.chan.queuingsystemforjava.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CreateMemberRequest(
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @Length(max = 50, message = "이메일은 50자 이하로 입력해주세요.")
        String email,
        @NotBlank(message = "비밀번호는 필수입니다.")
        @Length(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
        String password
) {
}
