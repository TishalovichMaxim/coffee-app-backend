package by.tishalovichm.coffee.auth;

import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEvents {

    @EventListener
    public void onFailure(AuthorizationDeniedEvent failure) {
        System.out.println("Authorization failed for user: %s".formatted(failure.getAuthentication().get().getPrincipal()));
    }
}
