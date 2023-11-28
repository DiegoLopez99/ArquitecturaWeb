package com.gateway.gateway_new.util;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProviderUtil {

    private final Logger log = LoggerFactory.getLogger(TokenProviderUtil.class);

    private static final String INVALID_JWT_TOKEN = "Invalid JWT token.";
    private final String secret = "QJeKx+s7XIv1WbBlj7vJ9CD3Ozj1rB3qjlNZY9ofWKJSaBNBo5r1q9Rru/OWlYb+UHV1n4/LJl1OBYYZZ7rhJEnn5peyHCd+eLJfRdArE37pc+QDIsJlabQtR7tYRa+SnvGRyL01uZsK33+gezV+/GPXBnPTj8fOojDUzJiPAvE=";

    private final Key key;

    private final JwtParser jwtParser;


    public TokenProviderUtil() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        key = Keys.hmacShaKeyFor( keyBytes );
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }



    /**
     * Valida el token.
     * @param authToken
     * @return
     */
    public boolean validateToken( String authToken ) {
        try {
            jwtParser.parseClaimsJws( authToken );
            return true;
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException( e.getHeader(), e.getClaims(), e.getMessage() );
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            log.trace(INVALID_JWT_TOKEN, e);
        } catch (IllegalArgumentException e) {
            log.error("Token validation error {}", e.getMessage());
        }
        return false;
    }
}
