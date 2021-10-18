package com.rapidnotes.heimdall.util;

import com.rapidnotes.heimdall.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.security.cert.TrustAnchor;
import java.util.Date;


@Component
@Slf4j
public class JWTTokenUtil {

    private final String jwtSecret = "jdahhddsfdsfytyvbczxvfsdiuwerdsfsdhfgsdhgewruewsdaksdjshasjfkgdshfgsdfuewyge";
    private final Key signingKey = new SecretKeySpec(DatatypeConverter.parseBase64Binary(jwtSecret), SignatureAlgorithm.HS512.getJcaName());
    private final String jwtIssuer = "rapid-notes.com";
    private static final long HOUR = 3600*1000; // in milli seconds

    public boolean validate(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature - {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token - {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token - {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token - {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty - {}", e.getMessage());
        }
        return false;
    }

    public String generateJWT(User user) {
        return Jwts.builder()
                .setSubject(String.format("%s", user.getUsername()))
                .setIssuer(jwtIssuer)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + HOUR))
                .signWith(signingKey)
                .compact();
    }

    public String getUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
