package com.futuristlabs.rest.v1_0;

import com.futuristlabs.func.auth.AuthService;
import com.futuristlabs.func.users.CustomUserDetails;
import com.futuristlabs.func.users.LoginRequest;
import com.futuristlabs.func.users.UserSession;
import com.futuristlabs.func.users.User;
import com.futuristlabs.func.users.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;


@RestController
@RequestMapping(value = "/v1.0/sessions")
public class SessionsEndoint {
    private final AuthService authService;
    private final Users users;

    @Autowired
    public SessionsEndoint(AuthService authService, Users users) {
        this.authService = authService;
        this.users = users;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    final UserSession login(final @RequestBody @Valid LoginRequest login,
                            final HttpSession session) {
        final CustomUserDetails userDetails = authService.authenticateUser(login.getUsername(), login.getPassword());
        final User user = users.readById(userDetails.getId());
        return new UserSession(session.getId(), user);
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(final HttpSession session) {
        session.invalidate();
    }
}
