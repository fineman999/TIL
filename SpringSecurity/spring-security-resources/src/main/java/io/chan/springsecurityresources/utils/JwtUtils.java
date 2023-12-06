package io.chan.springsecurityresources.utils;

import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER;

public final class JwtUtils {
    private JwtUtils() {
    }
    public static String BEARER_SPACE = BEARER.getValue() + " ";
    public static String BEARER_REPLACEMENT = "";
    public static String CLAIM_USERNAME = "username";
    public static String CLAIM_AUTHORITIES = "authorities";
    public static String ISSUER = "http://localhost:8081";
    public static String SUB = "user";

}
