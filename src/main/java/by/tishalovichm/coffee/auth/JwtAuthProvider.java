package by.tishalovichm.coffee.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthProvider implements AuthenticationProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }

        DecodedJWT decodedJWT;
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                .build();

            decodedJWT = verifier.verify((String) authentication.getPrincipal());
        } catch (JWTVerificationException exception){
            throw new BadCredentialsException(exception.getMessage());
        }

        return new JwtAuthenticationOut(
            decodedJWT.getClaim("id").asLong(),
            decodedJWT.getClaim("name").asString(),
            decodedJWT.getClaim("authorities").asList(String.class)
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationIn.class.isAssignableFrom(authentication);
    }
}
