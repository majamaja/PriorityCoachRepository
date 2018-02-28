package com.futuristlabs.rest.v1_0;

import com.futuristlabs.func.auth.ChangePasswordRequest;
import com.futuristlabs.func.users.CustomUserDetails;
import com.futuristlabs.func.users.UserSession;
import com.futuristlabs.func.users.ForgottenPasswordRequest;
import com.futuristlabs.func.users.User;
import com.futuristlabs.func.users.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/v1.0/users")
public class UsersEndpoint {
    private final Users users;

    @Autowired
    public UsersEndpoint(Users users) {
        this.users = users;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public User getProfile(final @AuthenticationPrincipal CustomUserDetails userDetails) {
        return users.readById(userDetails.getId());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public UserSession register(final @RequestBody @Valid User user,
                                final HttpSession session) {
        return users.create(user, session);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProfile(final @RequestBody @Valid User user,
                              final @AuthenticationPrincipal CustomUserDetails userDetails) {
        users.update(userDetails.getId(), user);
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(final @RequestBody @Valid ChangePasswordRequest request,
                               final @AuthenticationPrincipal CustomUserDetails userDetails) {
        users.changePassword(userDetails.getId(), request);
    }

    @RequestMapping(value = "/forgotten-password", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void forgottenPassword(final @RequestBody @Valid ForgottenPasswordRequest request) {
        users.forgottenPassword(request);
    }
}
