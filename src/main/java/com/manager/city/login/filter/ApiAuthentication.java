package com.manager.city.login.filter;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class ApiAuthentication extends AbstractAuthenticationToken {

    private final UserDetails userDetails;

    public ApiAuthentication(UserDetails userDetails) {
        super(userDetails.getAuthorities());
        this.userDetails = userDetails;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userDetails;
    }
}
