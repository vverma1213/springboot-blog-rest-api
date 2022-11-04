package com.springboot.blog.security;

import com.springboot.blog.exception.BlogApiException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app.jwt-expiration-milliseconds}")
    private String jwtExpiration;

    //generate token

    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime()+Long.valueOf(jwtExpiration));

        String generateToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512,jwtSecret)
                .compact();

        return generateToken;
    }

    //get username from token

    public String getUsernameFromJwt(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // validate jwt token
    public Boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        }catch(SignatureException e){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Invalid Jwt signature");
        } catch(MalformedJwtException e){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Invalid Jwt token");
        } catch(ExpiredJwtException e){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Expired Jwt token");
        } catch(UnsupportedJwtException e){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Unsupported JWT token");
        } catch (IllegalArgumentException e){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Jwt claims string is empty");
        }
    }
}
