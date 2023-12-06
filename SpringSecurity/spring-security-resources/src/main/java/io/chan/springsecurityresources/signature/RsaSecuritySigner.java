package io.chan.springsecurityresources.signature;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import org.springframework.security.core.userdetails.UserDetails;

public class RsaSecuritySigner extends SecuritySigner {

    @Override
    public String getJwtToken(UserDetails userDetails, JWK jwk) throws JOSEException {

        RSASSASigner jwsSigner = new RSASSASigner(jwk.toRSAKey());
        return super.getJwtTokenInternal(jwsSigner, userDetails, jwk);
    }
}
