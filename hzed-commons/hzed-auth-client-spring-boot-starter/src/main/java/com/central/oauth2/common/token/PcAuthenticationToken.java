package com.central.oauth2.common.token;

import com.central.common.constant.ServiceEnum;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class PcAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private Object credentials;
    private int servicetype;

    public PcAuthenticationToken(String userName, String password, int servicetype) {
        super(null);
        this.principal = userName;
        this.credentials = password;
        this.servicetype = servicetype;
        setAuthenticated(false);
    }

    public PcAuthenticationToken(Object principal,Object credentials,Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public int getServicetype(){
        return this.servicetype;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }
}
