package by.tishalovichm.coffee.config;

import by.tishalovichm.coffee.auth.CustomAccessDeniedHandler;
import by.tishalovichm.coffee.auth.CustomBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(req ->
                req.requestMatchers(HttpMethod.GET, "/api/coffees", "/api/coffees/*").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/auth/sign-up", "/api/coffees/proposal/json").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/coffees").hasAuthority("CREATE")
                    .requestMatchers(HttpMethod.PUT, "/api/coffees/*").hasAuthority("UPDATE")
                    .requestMatchers(HttpMethod.DELETE, "/api/coffees/*").hasAuthority("DELETE")
                    .anyRequest().authenticated()
            );


        http.httpBasic(c ->
            c.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint())
        );

        http.exceptionHandling(c ->
            c.accessDeniedHandler(new CustomAccessDeniedHandler()));

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
