package ru.cft.coi.coordinator;

import java.util.Collection;

public class UserClaims {

    private final String realm;
    private final String username;
    private final Collection<String> roles;

    UserClaims(String realm, String username, Collection<String> roles) {
        this.realm = realm;
        this.username = username;
        this.roles = roles;
    }

    public String getRealm() {
        return realm;
    }

    public String getUsername() {
        return username;
    }

    public Collection<String> getRoles() {
        return roles;
    }
}
