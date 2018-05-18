package com.futuristlabs.func.admins;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class AdminUserDetails implements UserDetails {
    private final AdminUser adminUser;
    private final Collection<? extends GrantedAuthority> authorities;

    public AdminUserDetails(final AdminUser adminUser) {
        this.adminUser = adminUser;
        this.authorities = Collections.singleton(() -> "ADMIN");
    }

    public AdminUser getAdminUser() {
        return adminUser;
    }

    @Override
    public String getUsername() {
        return adminUser.getUsername();
    }

    @Override
    public String getPassword() {
        return adminUser.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !adminUser.getIsBlocked();
    }
}
