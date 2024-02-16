package com.yuk.wazzangstudyrestapi1.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtComponent {
    @Value("${jwt.secret}")
    private String jwtSecret;

    private final long tokenValidTime = 24 * 60 * 60 * 1000L;

    private String claimUserid;
    private String claimRole;

    public String createToken(String userPk, String roles) {
        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecret))
                    .parseClaimsJws(token).getBody();
            this.claimUserid = claims.getSubject();
            this.claimRole = claims.get("roles",String.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getClaimUserid() {
        return this.claimUserid;
    }

    public void getTokenInfo() {
        return;
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
}