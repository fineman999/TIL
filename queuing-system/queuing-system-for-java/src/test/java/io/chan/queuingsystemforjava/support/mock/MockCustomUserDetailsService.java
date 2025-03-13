package io.chan.queuingsystemforjava.support.mock;

import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.member.MemberRole;
import io.chan.queuingsystemforjava.global.security.MemberContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class MockCustomUserDetailsService implements UserDetailsService {

    // JwtProviderTestImpl과 동일한 고정 토큰 사용
    private static final String ADMIN_TOKEN = "mocked-jwt-token-for-admin";
    private static final String MEMBER_TOKEN = "mocked-jwt-token-for-member";

    @Override
    public UserDetails loadUserByUsername(String jwt) throws UsernameNotFoundException {
        if (ADMIN_TOKEN.equals(jwt)) {
            // 관리자용 Mock Member 생성
            Member admin = Member.builder()
                    .memberId(1L)
                    .email("admin@example.com") // 필요 시 추가 필드 설정
                    .memberRole(MemberRole.ADMIN)
                    .build();
            List<GrantedAuthority> roles = new ArrayList<>();
            roles.add(new SimpleGrantedAuthority(admin.getMemberRole().getValue()));
            return MemberContext.createAuth(admin, roles);
        } else if (MEMBER_TOKEN.equals(jwt)) {
            // 멤버용 Mock Member 생성
            Member member = Member.builder()
                    .memberId(2L)
                    .email("member@example.com") // 필요 시 추가 필드 설정
                    .memberRole(MemberRole.USER)
                    .build();
            List<GrantedAuthority> roles = new ArrayList<>();
            roles.add(new SimpleGrantedAuthority(member.getMemberRole().getValue()));
            return MemberContext.createAuth(member, roles);
        } else {
            throw new UsernameNotFoundException("User not found for token: " + jwt);
        }
    }
}
