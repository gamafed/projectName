package com.companyName.projectName.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
public class JWTService {
    @Autowired
    private AuthenticationManager authenticationManager;

    private final String KEY = "TestKeyTestKeyTestKeyTestKeyTestKeyTestKeyTestKey";
    //The specified key byte array is 112 bits which is not secure enough for any JWT HMAC-SHA algorithm
    //HMAC-SHA algorithms MUST have a size >= 256 bits
    // have to be loooooooooooooooooooooooong

    public String generateToken(AuthRequest request) {
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        //AuthenticationManager驗證傳入的使用者帳密
        authentication = authenticationManager.authenticate(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        ZonedDateTime expirationDateTime = now.plusMinutes(2);

        Claims claims = Jwts.claims();
        claims.put("username", userDetails.getUsername());
        claims.setExpiration(Date.from(expirationDateTime.toInstant()));
        claims.setIssuer("TestIssuer");

        Key secretKey = Keys.hmacShaKeyFor(KEY.getBytes());
//        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // strong

        return Jwts.builder()
                .setClaims(claims)
                .signWith(secretKey)
                .compact();
    }

    public Map<String, Object> parseToken(String token) {
        //密鑰
        Key secretKey = Keys.hmacShaKeyFor(KEY.getBytes());

        //解析器
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build();

        //JWS取出
        Claims claims = parser
                .parseClaimsJws(token)
                .getBody();

        return claims.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
