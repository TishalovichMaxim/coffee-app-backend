package by.tishalovichm.coffee.config;

import by.tishalovichm.coffee.auth.CustomAccessDeniedHandler;
import by.tishalovichm.coffee.auth.CustomBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
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
            .authorizeHttpRequests(req ->
            req.requestMatchers(HttpMethod.GET, "/coffees").authenticated()
                .requestMatchers( "/auth/sign-up", "/error").permitAll()
                .anyRequest().authenticated()
            );

        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(Customizer.withDefaults());
        http.httpBasic(httpBasic ->
            httpBasic.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        http.exceptionHandling(c ->
            c.accessDeniedHandler(new CustomAccessDeniedHandler()));
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
