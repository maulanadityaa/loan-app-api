package org.enigma.livecodeloan.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.enigma.livecodeloan.model.entity.AppUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    @Value("${app.livecode-loan.jwt.jwt-secret}")
    private String jwtSecret;

    @Value("${app.livecode-loan.jwt.app-name}")
    private String appName;

    @Value("${app.livecode-loan.jwt.jwt-expired}")
    private Long jwtExpiredAt;

    public String generateToken(AppUser appUser) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));

            return JWT.create()
                    .withIssuer(appName) // information about app name
                    .withSubject(appUser.getId()) // determine object created from (usually userId)
                    .withExpiresAt(Instant.now().plusSeconds(jwtExpiredAt))
                    .withIssuedAt(Instant.now()) // determine when the token created
                    .withClaim("roles", appUser.getRoles().stream().map(Enum::toString).toList()) // JWT payload
                    .sign(algorithm); // sign the token with defined algorithm
        } catch (JWTCreationException e) {
            throw new RuntimeException();
        }
    }

    public Boolean verifyJwtToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            return decodedJWT.getIssuer().equals(appName);
        } catch (JWTVerificationException e) {
            throw new RuntimeException();
        }
    }

    public Map<String, String> getUserInfoByToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("userId", decodedJWT.getSubject());
            userInfo.put("roles", decodedJWT.getClaim("roles").asString());

            return userInfo;
        } catch (JWTVerificationException e) {
            throw new RuntimeException();
        }
    }
}
