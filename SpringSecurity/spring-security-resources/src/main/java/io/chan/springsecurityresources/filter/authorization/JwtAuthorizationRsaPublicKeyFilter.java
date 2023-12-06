package io.chan.springsecurityresources.filter.authorization;

import com.nimbusds.jose.JWSVerifier;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.io.IOException;
import java.util.List;

import static io.chan.springsecurityresources.utils.JwtUtils.CLAIM_AUTHORITIES;
import static io.chan.springsecurityresources.utils.JwtUtils.CLAIM_USERNAME;

public class JwtAuthorizationRsaPublicKeyFilter extends JwtAuthorizationFilter {

    private final JwtDecoder jwtDecoder;

    public JwtAuthorizationRsaPublicKeyFilter(JwtDecoder jwtDecoder) {
        super(null);
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (tokenResolve(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (jwtDecoder != null) {

            String token = getToken(request);

            // 서명된 JWT를 파싱한다. - 서명 검증
            Jwt jwt = jwtDecoder.decode(token);

            String username = jwt.getClaimAsString(CLAIM_USERNAME);
            List<String> authorities = jwt.getClaimAsStringList(CLAIM_AUTHORITIES);

            // JWT에서 UserDetails를 추출한다. - 유저 정보 추출
            UserDetails userDetails = getUserDetails(username, authorities);

            // SecurityContext에 인증된 사용자 정보를 저장한다.
            setSecurityContextAuthentication(userDetails);
        }
    }
}
