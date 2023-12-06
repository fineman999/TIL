package io.chan.springsecurityresources.signature;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.PrivateKey;

public class RsaPublicKeySecuritySigner extends SecuritySigner{

    private final PrivateKey privateKey;

    public RsaPublicKeySecuritySigner(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public String getJwtToken(UserDetails userDetails, JWK jwk) throws JOSEException {

        RSASSASigner jwsSigner = new RSASSASigner(privateKey);
        return super.getJwtTokenInternal(jwsSigner, userDetails, jwk);
    }
}
