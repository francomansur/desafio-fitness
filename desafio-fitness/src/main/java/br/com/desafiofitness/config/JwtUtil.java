package br.com.desafiofitness.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtUtil {
    private static final String SECRET = "segredo-super-secreto-do-desafio-fitness";
    private static final String ISSUER = "desafio-fitness-api";
    private static final long EXPIRATION_TIME = 86400000; // 24 horas em milissegundos
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);

    public static String gerarToken(Integer usuarioId, String email) {
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(String.valueOf(usuarioId))
                .withClaim("email", email)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(ALGORITHM);
    }

    public static DecodedJWT validarToken(String token) {
        JWTVerifier verifier = JWT.require(ALGORITHM)
                .withIssuer(ISSUER)
                .build();
        return verifier.verify(token);
    }
}
