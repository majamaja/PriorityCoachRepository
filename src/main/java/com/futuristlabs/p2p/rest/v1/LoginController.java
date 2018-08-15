package com.futuristlabs.p2p.rest.v1;


import com.futuristlabs.p2p.func.auth.Authentication;
import com.futuristlabs.p2p.func.auth.AuthenticationRequest;
import com.futuristlabs.p2p.func.auth.ResetPasswordRequest;
import com.futuristlabs.p2p.func.auth.UserSession;
import com.futuristlabs.p2p.rest.common.RestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/v1")
public class LoginController {

    private Authentication auth;

    @Autowired
    public LoginController(Authentication auth) {
        this.auth = auth;
    }

    @RequestMapping(value = "/login", method = POST)
    @ResponseBody
    public UserSession login(@RequestBody AuthenticationRequest authenticationRequest) {
        final UserSession user = auth.login(authenticationRequest);

        if (user == null) {
            throw new RestException(HttpStatus.UNAUTHORIZED, "WRONG_CREDENTIALS");
        }

        return user;
    }

    @RequestMapping(value = "/register", method = POST)
    @ResponseBody
    public UserSession register(@RequestBody AuthenticationRequest registrationRequest) {
        final UserSession registeredUser = auth.register(registrationRequest);

        if (registeredUser == null) {
            throw new RestException(HttpStatus.UNPROCESSABLE_ENTITY, "USER_ALREADY_EXISTS");
        }

        return registeredUser;
    }

    @RequestMapping(value = "/reset-password", method = POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        if (!auth.resetPassword(resetPasswordRequest)) {
            throw new RestException(HttpStatus.BAD_REQUEST, "INVALID_EMAIL");
        }
    }

}
