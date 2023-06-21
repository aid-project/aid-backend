package com.aid.aidbackend.utils;

import com.aid.aidbackend.controller.dto.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {
    public static final String BEARER = "Bearer ";
    public static final String CURRENT_MEMBER = "Current-Member";

    private final Key key;
    private final long expiration;


    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = expiration;
    }

    public TokenDto generateToken(String id) {
        long now = new Date().getTime();
        Date exp = new Date(now + expiration);

        String jwt = Jwts.builder()
                .setSubject(id)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return new TokenDto(jwt);
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

}
