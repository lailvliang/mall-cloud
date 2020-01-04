package com.central.oauth.mobile;

import com.central.common.constant.ServiceEnum;
import com.central.common.model.LoginAppUser;
import com.central.oauth.service.HzedUserDetailsService;
import com.central.oauth2.common.token.MobileAuthenticationToken;
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
public class MobileAuthenticationProvider implements AuthenticationProvider {
    private HzedUserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) {
        MobileAuthenticationToken authenticationToken = (MobileAuthenticationToken) authentication;
        String mobile = (String) authenticationToken.getPrincipal();
        String password = (String) authenticationToken.getCredentials();
        Class serviceClass = ServiceEnum.getServiceClassByType(authenticationToken.getServicetype());
        LoginAppUser user = userDetailsService.loadUserByMobile(mobile,serviceClass.getName());
        if (user == null) {
            throw new InternalAuthenticationServiceException("手机号或密码错误");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("手机号或密码错误");
        }
        user.setServicetype(authenticationToken.getServicetype());
        MobileAuthenticationToken authenticationResult = new MobileAuthenticationToken(user, password, user.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MobileAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
