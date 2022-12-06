package com.example.appcommunicationcompany.config;

import com.example.appcommunicationcompany.entity.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

@Component
public class JwtProvider {
    private static final long expireTime = 1000 * 60 * 60 * 24; //bir kun
    private static final String secretKey = "farhod";

    public String generateToken(String userName, Set<Role> roles) {
        Date date = new Date(System.currentTimeMillis() + expireTime);
        return Jwts
                .builder()
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(date)
                .claim("roles", roles)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
}
