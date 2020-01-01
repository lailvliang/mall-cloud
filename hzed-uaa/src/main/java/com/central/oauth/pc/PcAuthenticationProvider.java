package com.central.oauth.pc;

import com.central.oauth.service.HzedUserDetailsService;
import com.central.oauth2.common.token.MobileAuthenticationToken;
import com.central.oauth2.common.token.PcAuthenticationToken;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author hzed
 */
@Setter
public class PcAuthenticationProvider implements AuthenticationProvider {
    private HzedUserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) {
        PcAuthenticationToken authenticationToken = (PcAuthenticationToken) authentication;
        String userName = (String) authenticationToken.getPrincipal();
        String password = (String) authenticationToken.getCredentials();
        int serviceType = authenticationToken.getServicetype();
        UserDetails user = userDetailsService.loadUserByUserName(userName,serviceType);
        if (user == null) {
            throw new InternalAuthenticationServiceException("手机号或密码错误");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("手机号或密码错误");
        }
        PcAuthenticationToken authenticationResult = new PcAuthenticationToken(user, password, user.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PcAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
