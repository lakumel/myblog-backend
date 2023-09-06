package com.example.backend.security.utils;

import com.example.backend.security.token.JsonAuthenticationToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtils {

    private Key secreteKey;

    private Long tokenValidityInMilliseconds;

    private String role = "role";

    public JwtUtils(@Value("${security.jwt.secret}") String secret,
                    @Value("${security.jwt.token-validity-in-seconds}") Long tokenValidityInMilliseconds) {
        String encodedSecrete = Encoders.BASE64.encode(secret.getBytes(StandardCharsets.UTF_8));

        this.secreteKey = Keys.hmacShaKeyFor(encodedSecrete.getBytes(StandardCharsets.UTF_8));
        this.tokenValidityInMilliseconds = tokenValidityInMilliseconds;
    }

    public String createToken(Authentication authentication) {

        String authorities = authentication.getAuthorities().stream()
                .map((authority) -> authority.getAuthority())
                .collect(Collectors.joining(","));

        Date expireDate = new Date(System.currentTimeMillis() + tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(role, authorities)
                .signWith(secreteKey, SignatureAlgorithm.HS512)
                .setExpiration(expireDate)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims body = Jwts.parserBuilder()
                .setSigningKey(secreteKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String[] roles = body.get(role).toString().split(",");
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(roles)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User user = new User(body.getSubject(), "", authorities);

        return new JsonAuthenticationToken(user, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secreteKey).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SignatureException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다. Message : {}", e.getMessage());
            throw e;
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다. Message : {}", e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다. Message : {}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다. Message : {}", e.getMessage());
            throw e;
        }
//        return false;
    }
}
