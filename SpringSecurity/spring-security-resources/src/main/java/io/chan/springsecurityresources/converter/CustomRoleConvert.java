package io.chan.springsecurityresources.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;
import java.util.stream.Collectors;

public class CustomRoleConvert implements Converter<Jwt, Collection<GrantedAuthority>> {
    private final String ROLE_PREFIX = "ROLE_";
    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        String scope = source.getClaimAsString("scope");
        Map<String, Object> realmAccess = source.getClaimAsMap("realm_access");
        if (scope == null && realmAccess != null) {
             return Collections.emptyList();
        }
        assert scope != null;
        Collection<GrantedAuthority> authorities1 = Arrays.stream(scope.split(" "))
                .map(authority -> ROLE_PREFIX + authority)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        ((List<String>) realmAccess.get("roles")).stream()
                .map(role -> ROLE_PREFIX + role)
                .map(SimpleGrantedAuthority::new)
                .forEach(authorities1::add);

        return authorities1;
    }
}
