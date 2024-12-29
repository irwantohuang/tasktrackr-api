package com.veedo.tsk.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration
public class JwtTokenConfig {



    public static final long JWT_TOKEN_VALIDITY = 12L * 60 * 60;
    private final String secret = "tsktasktrackr";

    private Claims getAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private <T> T getClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String getEmailFromToken(String token) {
        return getClaims(token, Claims::getSubject);
    }

    public String getSessionIdFromToken(String token) {
        return getClaims(token, Claims::getId);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaims(token, Claims::getExpiration);
    }

    public Boolean isTokenExpired(String token) {
        final Date exp = getExpirationDateFromToken(token);
        return exp.before(new Date());
    }

    public Boolean validateToken(String token, String email) {
        Map<String, Object> claims = getAllClaims(token);
        return (!isTokenExpired(token) && getEmailFromToken(token).equals(email));
    }

    public String generateToken(String username, String sessionId) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username,  sessionId, JWT_TOKEN_VALIDITY);
    }

    public String createToken(Map<String, Object> claims, String subject, String sessionId, Long duration) {
        final Date createdDate = new Date();
        final Date expDate = new Date(createdDate.getTime() + duration * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setId(sessionId)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }


}
