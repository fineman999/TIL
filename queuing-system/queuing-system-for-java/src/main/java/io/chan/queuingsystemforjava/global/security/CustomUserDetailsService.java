package io.chan.queuingsystemforjava.global.security;
import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.member.repository.MemberRepository;
import io.chan.queuingsystemforjava.global.cache.CacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    public static final String ID = "id";
    private final MemberRepository memberRepository;
    private final CacheRepository cacheRepository;


    @Override
    public UserDetails loadUserByUsername(String jwt) throws UsernameNotFoundException {
        String id = cacheRepository.getValueHashes(jwt, ID);
        if (id == null) {
            throw new UsernameNotFoundException("User not found");
        }
        Long userId = Long.parseLong(id);
        Member member = memberRepository.getById(userId);
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(member.getMemberRole().getValue()));
        return MemberContext.createAuth(member, roles);
    }
}
