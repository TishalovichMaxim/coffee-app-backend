package by.tishalovichm.coffee.entities;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {
    READ,
    CREATE;

    public String getAuthority() {
        return this.name();
    }
}
