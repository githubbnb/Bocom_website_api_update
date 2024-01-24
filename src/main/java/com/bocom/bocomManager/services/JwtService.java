package com.bocom.bocomManager.services;

import com.bocom.bocomManager.dto.AuthenticationResponse;
import com.bocom.bocomManager.dto.appUser.AppUserResponse;
import com.bocom.bocomManager.models.AppUser;
import com.google.common.base.Strings;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${security.keys}")
    private String SECRET_KEY;

    @Value("${token.refresh.time}")
    private int refresh_token_time;

    @Value("${token.access.time}")
    private int access_token_time;

    public String extractUserEmail(String token) {
        String subject = extractSubject(token);
        return Strings.isNullOrEmpty(subject) ? null : subject.split(";")[1];
    }


    public String extractUserName(String token) {
        String subject = extractSubject(token);
        return Strings.isNullOrEmpty(subject) ? null : subject.split(";")[0];
    }

    private String extractSubject(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (Exception e) {
            throw new SignatureException(e.getMessage());
        }
    }

    public Date extractExpiration(String token) {
        try {
            return extractClaim(token, Claims::getExpiration);
        } catch (Exception e) {
            throw new SignatureException(e.getMessage());
        }
    }

    public Date extractIssuedAt(String token) {
        try {
            return extractClaim(token, Claims::getIssuedAt);
        } catch (Exception e) {
            throw new SignatureException(e.getMessage());
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            throw new SignatureException(e.getMessage());
        }
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build()
                    .parseClaimsJws(token).getBody();
        } catch (Exception e) {
            throw new SignatureException(e.getMessage());
        }
    }

    private boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            throw new SignatureException(e.getMessage());
        }
    }

    public Date addMinute(int minute) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, minute);
        return c.getTime();
    }

    public AuthenticationResponse generatedTokens(AppUser user) {
        return AuthenticationResponse
                .builder()
                .user( AppUserResponse
                        .builder()
                        .avatar(user.getAvatar())
                        .block(user.isBlock())
                        .email(user.getEmail())
                        .nom(user.getNom())
                        .prenom(user.getPrenom())
                        .telephone(user.getTelephone())
                        .role(user.getRole())
                        .id(user.getId())
                        .build()
                )
                .accessToken(doGenerateToken(user.getAuthorities(), user.getNom() + " " + user.getPrenom() + ";" + user.getEmail()))
                .refreshToken(doGenerateRefreshToken(user.getAuthorities(), user.getNom() + " " + user.getPrenom() + ";" + user.getEmail()))
                .build();
    }

    public String doGenerateRefreshToken(Collection<? extends GrantedAuthority> claims, String email) {
        Date expiration = addMinute(refresh_token_time);

        return Jwts.builder()
                .setSubject(email)
                .claim("authorities", claims)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS512)
                .compact();
    }

    private String doGenerateToken(Collection<? extends GrantedAuthority> claims, String email) {
        Date expiration = addMinute(access_token_time);

        return Jwts.builder()
                .setSubject(email)
                .claim("authorities", claims)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String authToken) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

}
