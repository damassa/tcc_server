package br.edu.ifsul.cstsi.tcc_server.api.infra.security;

import br.edu.ifsul.cstsi.tcc_server.api.infra.exception.TokenInvalidoException;
import br.edu.ifsul.cstsi.tcc_server.api.users.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service //indica que essa classe deve ser adicionada ao Contexto do aplicativo como um Bean da camada de serviço de segurança JWT
public class TokenService {

    @Value(value = "${api.security.token.secret}") //vem de application.properties, em api.security.token.secret
    private String secret;

    public String geraToken(User user){
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                .withIssuer("API World of Tokusatsu")
                .withSubject(user.getUsername())
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(Duration.ofHours(2)))
                .sign(algorithm);
        } catch (JWTCreationException exception){
            // Invalid Signing configuration / Couldn't convert Claims.
            throw new RuntimeException("Erro ao gerar o token JWT.", exception);
        }
    }

    public String getSubject(String tokenJWT){
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                .withIssuer("API World of Tokusatsu")
                .build()
                .verify(tokenJWT)
                .getSubject();
        } catch (JWTVerificationException exception){
            // Invalid signature/claims
            throw new TokenInvalidoException("Token JWT inválido ou expirado.");
        }
    }
}
