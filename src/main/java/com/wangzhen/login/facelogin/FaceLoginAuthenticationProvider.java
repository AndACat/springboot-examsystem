package com.wangzhen.login.facelogin;

import com.wangzhen.configuration.PublicSession;
import com.wangzhen.utils.Utils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.Part;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/4/4 18:29
 */
public class FaceLoginAuthenticationProvider implements AuthenticationProvider {
    public FaceLoginAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    UserDetailsService userDetailsService;
    @Override
    public Authentication authenticate(Authentication authentication) throws IllegalArgumentException , AuthenticationException {
        FaceLoginAuthenticationToken faceLoginAuthenticationToken = (FaceLoginAuthenticationToken) authentication;
        Part face = faceLoginAuthenticationToken.getCredentials();
        String randomStr = Utils.randomUuid();
        PublicSession.add(randomStr,face);
        UserDetails userDetails = userDetailsService.loadUserByUsername(randomStr);
        FaceLoginAuthenticationToken faceLoginAuthenticationToken1 = new FaceLoginAuthenticationToken(face,userDetails.getAuthorities());
        faceLoginAuthenticationToken.setDetails(userDetails);
        return faceLoginAuthenticationToken1;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return FaceLoginAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
