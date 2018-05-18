package com.futuristlabs.func.auth;

import com.futuristlabs.func.admins.AdminUser;
import com.futuristlabs.func.admins.AdminUserDetails;
import com.futuristlabs.func.admins.AdminUsersRepository;
import com.futuristlabs.func.exceptions.UnauthorizedException;
import com.futuristlabs.func.users.CustomUserDetails;
import com.futuristlabs.func.users.User;
import com.futuristlabs.func.users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import static com.futuristlabs.func.auth.AuthType.ADMIN;
import static com.futuristlabs.func.auth.AuthType.USER;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private AdminUsersRepository admins;
    @Autowired
    private UsersRepository users;

    @Override
    public UserDetails loadUserByUsername(final String uname) throws UsernameNotFoundException {
        // @TODO: Ugly if/else logic. Maybe move into the CustomUserDetails.Type enum somehow?
        final String roleToken = uname.substring(0, 1);
        final String username = uname.substring(1);

        if (roleToken.equals(USER.getToken())) {
            final User user = users.findByEmail(username);
            if (user == null) {
                throw new UsernameNotFoundException(username);
            }
            return new CustomUserDetails(user.getId(), user.getEmail(), user.getPassword());
        } else if (roleToken.equals(ADMIN.getToken())) {
            final AdminUser adminUser = admins.findByUsername(username);
            if (adminUser == null) {
                throw new UsernameNotFoundException(username);
            }
            return new AdminUserDetails(adminUser);
        } else {
            throw new UnauthorizedException();
        }
    }
}
