package com.futuristlabs.func.auth;

import com.futuristlabs.func.admins.AdminUserDetails;
import com.futuristlabs.func.admins.AdminUsersRepository;
import com.futuristlabs.func.users.CustomUserDetails;
import com.futuristlabs.func.users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.futuristlabs.func.auth.AuthType.*;

@Component
public class AuthService {
	@Autowired
	private AdminUsersRepository admins;

	@Autowired
	private UsersRepository users;

	@Autowired
	private AuthenticationManager authenticationManager;

	@SuppressWarnings("unchecked")
	private <T extends UserDetails> T authenticate(final String username, final String password) {
		final UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

		final Authentication auth = authenticationManager.authenticate(authRequest);
		final SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(auth);

		return (T) auth.getPrincipal();
	}

	@Transactional
	public CustomUserDetails authenticateUser(final String username, final String password) {
		final CustomUserDetails details = authenticate(USER.getToken() + username, password);
		users.loginById(details.getId());
		return details;
	}

	@Transactional
	public AdminUserDetails authenticateAdmin(final String username, final String password) {
		final AdminUserDetails details = authenticate(ADMIN.getToken() + username, password);
		admins.loginById(details.getAdminUser().getId());
		return details;
	}
}
