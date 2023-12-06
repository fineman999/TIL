package io.chan.springsecurityresources.signature;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.List;

import static io.chan.springsecurityresources.utils.JwtUtils.*;

public abstract class SecuritySigner {


    protected String getJwtTokenInternal(JWSSigner jwsSigner, UserDetails userDetails, JWK jwk) throws JOSEException {
        JWSHeader jwsHeader = new JWSHeader.Builder((JWSAlgorithm) jwk.getAlgorithm())
                .keyID(jwk.getKeyID())
                .build();

        List<String> authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(SUB)
                .issuer(ISSUER)
                .claim(CLAIM_USERNAME, userDetails.getUsername())
                .claim(CLAIM_AUTHORITIES, authorities)
                .expirationTime(new Date(System.currentTimeMillis() + 1000 * 60 * 5))
                .build();

        SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
        // 서명 생성
        signedJWT.sign(jwsSigner);

        // 서명된 JWT 반환
        return signedJWT.serialize();
    }

    public abstract String getJwtToken(UserDetails userDetails, JWK jwk) throws JOSEException;
}
