package com.central.oauth.openid;

import com.central.common.constant.ServiceEnum;
import com.central.oauth.service.HzedUserDetailsService;
import com.central.oauth2.common.token.OpenIdAuthenticationToken;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.security.SocialUserDetailsService;

/**
 * @author hzed
 */
@Setter
public class OpenIdAuthenticationProvider implements AuthenticationProvider {

    private HzedUserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) {
        OpenIdAuthenticationToken authenticationToken = (OpenIdAuthenticationToken) authentication;
        String openId = (String) authenticationToken.getPrincipal();
        Class serviceClass = ServiceEnum.getServiceClassByType(authenticationToken.getServicetype());
        UserDetails user = userDetailsService.loadUserByUserId(openId,serviceClass.getName());
        if (user == null) {
            throw new InternalAuthenticationServiceException("openId错误");
        }
        OpenIdAuthenticationToken authenticationResult = new OpenIdAuthenticationToken(user, user.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OpenIdAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
