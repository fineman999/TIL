package io.chan.springsecurityresources.filter.authorization;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static io.chan.springsecurityresources.utils.JwtUtils.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public abstract class JwtAuthorizationFilter extends OncePerRequestFilter {


    private final JWSVerifier jwsVerifier;

    public JwtAuthorizationFilter(JWSVerifier jwsVerifier) {
        this.jwsVerifier = jwsVerifier;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(AUTHORIZATION);
        if (Objects.isNull(header) || !header.startsWith(BEARER_SPACE)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.replace(BEARER_SPACE, BEARER_REPLACEMENT);

        try {

            // 서명된 JWT를 파싱한다. - 서명 검증
            SignedJWT signedJWT = getSignedJWT(token);

            // JWT에서 UserDetails를 추출한다. - 유저 정보 추출
            UserDetails userDetails = getUserDetails(signedJWT);

            // SecurityContext에 인증된 사용자 정보를 저장한다.
            setSecurityContextAuthentication(userDetails);


        } catch (ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }

        filterChain.doFilter(request, response);
    }

    private void setSecurityContextAuthentication(UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private UserDetails getUserDetails(SignedJWT signedJWT) throws ParseException {
        JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
        String username = jwtClaimsSet.getStringClaim(CLAIM_USERNAME);
        List<String> authorities = jwtClaimsSet.getStringListClaim(CLAIM_AUTHORITIES);


        if (Objects.isNull(username)) {
            throw new RuntimeException("username is null");
        }

        List<SimpleGrantedAuthority> newAuthorities = authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        return User.withUsername(username)
            .password(UUID.randomUUID().toString())
            .authorities(newAuthorities)
            .build();
    }

    private SignedJWT getSignedJWT(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);

        boolean verify = signedJWT.verify(jwsVerifier);

        if (!verify) {
            throw new RuntimeException("JWT is not verified");
        }
        return signedJWT;
    }
}
