package by.tishalovichm.coffee.services;

import by.tishalovichm.coffee.dtos.in.UserDtoIn;
import by.tishalovichm.coffee.entities.User;
import by.tishalovichm.coffee.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
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

    public User createNewUser(UserDtoIn userDtoIn) {
        String passwordsHash = passwordEncoder.encode(userDtoIn.password());
        var user = new User(null, userDtoIn.login(), passwordsHash, List.of());
        return repository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByLogin(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
