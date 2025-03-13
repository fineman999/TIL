package io.chan.queuingsystemforjava.global.security;

import io.chan.queuingsystemforjava.common.LoginMember;
import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.member.MemberRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.ServletWebRequest;

import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class MemberContextArgumentResolverTest {
    private MemberContextArgumentResolver resolver;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        resolver = new MemberContextArgumentResolver();
        request = new MockHttpServletRequest();

        // SecurityContext에 인증 정보 설정
        Member admin = Member.create("admin@admin.com", "password", MemberRole.ADMIN);
        MemberContext context = MemberContext.createAuth(admin, List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        AuthenticationToken auth = new AuthenticationToken(context, null, context.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Nested
    @DisplayName("supportsParameter 메서드 실행 시")
    class RunResolveArgument {

        @DisplayName("MemberContext 타입의 파라미터에 LoginMember 어노테이션이 붙어있는 경우 유저 정보를 반환한다.")
        @Test
        void resolveArgumentWithLoginMember() throws Exception {
            // given
            Method method = TestController.class.getMethod("testResolver", MemberContext.class);
            MethodParameter parameter = new MethodParameter(method, 0);
            ServletWebRequest webRequest = new ServletWebRequest(request);

            // when
            Object result = resolver.resolveArgument(parameter, null, webRequest, null);

            // then
            assertThat(result).isInstanceOf(MemberContext.class);
            assertThat(result).isNotNull();
            assertThat(((MemberContext) result).getUsername()).isEqualTo("admin@admin.com");
        }

    }

    static class TestController {
        public String testResolver(@LoginMember MemberContext member) {
            return member.getUsername();
        }
    }
}