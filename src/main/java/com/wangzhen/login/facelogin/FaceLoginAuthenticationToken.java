package com.wangzhen.login.facelogin;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import javax.servlet.http.Part;
import java.util.Collection;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/4/4 18:23
 */
public class FaceLoginAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal;
    private Part credentials;//就是face Part

    public FaceLoginAuthenticationToken(Part credentials) {
        super(null);
        this.principal = null;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    public FaceLoginAuthenticationToken(Part credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = null;
        this.credentials = credentials;
        super.setAuthenticated(true); // must use super, as we override
    }


    public Part getCredentials() {
        return this.credentials;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        credentials = null;
    }
}

