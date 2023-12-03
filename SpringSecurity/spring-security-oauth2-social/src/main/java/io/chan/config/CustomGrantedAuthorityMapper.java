package io.chan.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

import java.util.Collection;
import java.util.HashSet;

public class CustomGrantedAuthorityMapper implements GrantedAuthoritiesMapper {
    private static final String DOT = ".";
    private static final String SCOPE = "SCOPE_";
    private static final String PREFIX = "ROLE_";


    @Override
    public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        HashSet<GrantedAuthority> mapped = new HashSet<>(authorities.size());
        for (GrantedAuthority authority : authorities) {
            mapped.add(mapAuthority(authority.getAuthority()));
        }

        return mapped;
    }

    /**
     * 권한을 매핑한다.
     */
    private GrantedAuthority mapAuthority(String name) {
        if (name.lastIndexOf(DOT) > 0) {
            name = SCOPE + name.substring(name.lastIndexOf(DOT) + 1); // 마지막 점 이후의 문자열을 가져온다.
        }
        else if (!name.startsWith(PREFIX)) {
            name = PREFIX + name;
        }
        return new SimpleGrantedAuthority(name);
    }
}
