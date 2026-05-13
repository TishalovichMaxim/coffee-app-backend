package by.tishalovichm.coffee.config;

import by.tishalovichm.coffee.auth.CustomAccessDeniedHandler;
import by.tishalovichm.coffee.auth.JwtFilter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authorization.AuthorizationEventPublisher;
import org.springframework.security.authorization.SpringAuthorizationEventPublisher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class SecurityConfig {
    @Bean
    public AuthorizationEventPublisher authorizationEventPublisher
        (ApplicationEventPublisher applicationEventPublisher) {
        return new SpringAuthorizationEventPublisher(applicationEventPublisher);
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(req ->
                req.requestMatchers(HttpMethod.GET, "/api/coffees", "/api/coffees/*").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/auth/sign-up",
                        "/api/auth/sign-in", "/api/coffees/proposal/json").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/coffees").hasAuthority("CREATE")
                    .requestMatchers(HttpMethod.PUT, "/api/coffees/*").hasAuthority("UPDATE")
                    .requestMatchers(HttpMethod.DELETE, "/api/coffees/*").hasAuthority("DELETE")
                    .anyRequest().authenticated()
            );

        http.addFilterAfter(new JwtFilter(authenticationManager), CorsFilter.class);

        http.exceptionHandling(c ->
            c.accessDeniedHandler(new CustomAccessDeniedHandler()));

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(List<AuthenticationProvider> providers) {
        return new ProviderManager(providers);
    }
}
