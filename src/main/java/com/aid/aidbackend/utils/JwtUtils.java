package com.aid.aidbackend.utils;

import com.aid.aidbackend.controller.dto.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtils {

    private static final Key key = Keys.hmacShaKeyFor(System.getenv("AID_JWT_SECRET").getBytes());
    private static final long expiration = Long.parseLong(System.getenv("AID_JWT_EXPIRATION"));

    public static TokenDto generateToken(String id) {
        long now = new Date().getTime();
        Date exp = new Date(now + expiration);

        String jwt = Jwts.builder()
                .setSubject(id)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return new TokenDto(jwt);
    }

    public static Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

}
