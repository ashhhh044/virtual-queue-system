package com.queue.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    
    // Read from application.properties (jwt.secret). Falls back to a dev-only
    // default so the app still runs locally if it's not set - but for any real
    // deployment, set JWT_SECRET as an environment variable.
    @Value("${jwt.secret:ds6djx7e5795jaw5u82ule29q2osea9u}")
    private String secretKey;

    // Access token validity: 15 minutes. Short on purpose - the refresh token is what stays valid for days, not this one.

    private static final long EXPIRATION_TIME = 15*60*1000;

    // Generate signing key
    private Key getSigningKey(){
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }


    // Extract username/email from token
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    // Extract role from token
    public String extractRole(String token){
        return extractAllClaims(token).get("role", String.class);
    }

    // Extract expiration date
    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract Specific Claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    // Check if token has expired
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    // Generate token for users
    public String generateToken(String email, String role){
        Map<String,Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, email);
    }

    // Create Token
    private String createToken(Map<String,Object> claims, String subject){
        return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(subject)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
    }

    // Validate Token
    public Boolean validateToken(String token, String username){
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // Validate token without username
    public Boolean validateToken(String token){
        return !isTokenExpired(token);
    }

}

