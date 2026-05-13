package by.tishalovichm.coffee.services;

import by.tishalovichm.coffee.dtos.in.UserDtoIn;
import by.tishalovichm.coffee.dtos.out.TokenDtoOut;
import by.tishalovichm.coffee.entities.Authority;
import by.tishalovichm.coffee.entities.User;
import by.tishalovichm.coffee.repositories.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    @Value("${jwt.secret}")
    private String secret;

    @SneakyThrows
    public String createAccessToken(User user) {
        Algorithm algorithm = Algorithm.HMAC512(secret);
        return JWT.create()
            .withClaim("id", user.getId())
            .withClaim("name", user.getLogin())
            .withClaim("authorities",
                user.getAuthorities().stream()
                    .map(Authority::getName)
                    .toList()
            )
            .sign(algorithm);
    }

    @SneakyThrows
    public TokenDtoOut signIn(UserDtoIn userDtoIn) {
        User user =  repository.findByLogin(userDtoIn.login()).orElseThrow();
        if (!passwordEncoder.matches(userDtoIn.password(), user.getPassword())) {
            throw new Exception("Wrong password");
        }

        return new TokenDtoOut(createAccessToken(user));
    }

    public TokenDtoOut signUp(UserDtoIn userDtoIn) {
        String passwordsHash = passwordEncoder.encode(userDtoIn.password());
        var user = new User(null, userDtoIn.login(), passwordsHash, List.of());
        return new TokenDtoOut(createAccessToken(user));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByLogin(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
