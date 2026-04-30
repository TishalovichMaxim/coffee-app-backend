package by.tishalovichm.coffee.config;

import by.tishalovichm.coffee.entities.Authority;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) {
        http.authorizeHttpRequests(req ->
            req.requestMatchers(HttpMethod.GET, "/coffees").permitAll()
                .anyRequest().authenticated()
            );

        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
            new User("maxim", "maximspassword", List.of(Authority.READ, Authority.CREATE)),
            new User("vasya", "vasyaspassword", List.of(Authority.READ))
        );
    }

    @Bean
    AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        return new AuthenticationProvider() {
            @Override
            public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
                if (!supports(authentication.getClass())) {
                    return null;
                }

                String username = (String) authentication.getPrincipal();
                String password = (String) authentication.getCredentials();

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (!password.equals(userDetails.getPassword())) {
                    throw new BadCredentialsException("Credentials are wrong");
                }

                return new Authentication() {
                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        return userDetails.getAuthorities();
                    }

                    @Override
                    public @Nullable Object getCredentials() {
                        return userDetails.getPassword();
                    }

                    @Override
                    public @Nullable Object getDetails() {
                        return null;
                    }

                    @Override
                    public @Nullable Object getPrincipal() {
                        return userDetails.getUsername();
                    }

                    @Override
                    public boolean isAuthenticated() {
                        return true;
                    }

                    @Override
                    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
                    }

                    @Override
                    public String getName() {
                        return userDetails.getUsername();
                    }
                };
            }

            @Override
            public boolean supports(Class<?> authentication) {
                return authentication == UsernamePasswordAuthenticationToken.class;
            }
        };
    }

    @Bean
    AuthenticationManager authenticationManager(Collection<AuthenticationProvider> providers) {
        return authentication -> {
            for (AuthenticationProvider provider : providers) {
                Authentication resAuthentication = provider.authenticate(authentication);
                if (resAuthentication != null) {
                    return resAuthentication;
                }
            }

            throw new AuthenticationException("No provider found") {
            };
        };
    }
}
