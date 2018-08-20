package com.futuristlabs.p2p.rest.v1;


import com.futuristlabs.p2p.func.auth.*;
import com.futuristlabs.p2p.func.sync.UserSyncData;
import com.futuristlabs.p2p.func.userprofile.ChangePasswordRequest;
import com.futuristlabs.p2p.func.userprofile.UserProfile;
import com.futuristlabs.p2p.func.userprofile.UserProfiles;
import com.futuristlabs.p2p.rest.common.RestException;
import com.futuristlabs.p2p.utils.Utils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/v1")
public class LoginController {

    private Authentication auth;
    private UserProfiles userProfiles;

    @Autowired
    public LoginController(final Authentication auth, final UserProfiles userProfiles) {
        this.auth = auth;
        this.userProfiles = userProfiles;
    }

    @RequestMapping(value = "/register", method = POST)
    @ResponseBody
    public UserSession register(@RequestBody AuthenticationRequest registrationRequest, HttpSession session) {
        final SessionUser user = auth.register(registrationRequest);

        if (user == null) {
            throw new RestException(HttpStatus.UNPROCESSABLE_ENTITY, "USER_ALREADY_EXISTS");
        }

        final UserSession userSession = new UserSession();
        userSession.setUserId(user.getId());
        userSession.setAccessToken(session.getId());
        return userSession;
    }

    @RequestMapping(value = "/login", method = POST)
    @ResponseBody
    public UserSession login(@RequestBody AuthenticationRequest authenticationRequest, HttpSession session) {
        final SessionUser user = auth.login(authenticationRequest);

        if (user == null) {
            throw new RestException(HttpStatus.UNAUTHORIZED, "WRONG_CREDENTIALS");
        }

        final UserSession userSession = new UserSession();
        userSession.setUserId(user.getId());
        userSession.setAccessToken(session.getId());
        return userSession;
    }

    @RequestMapping(value = "/reset-password", method = POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        if (!auth.resetPassword(resetPasswordRequest)) {
            throw new RestException(HttpStatus.BAD_REQUEST, "INVALID_EMAIL");
        }
    }

    @RequestMapping(value = "/user/profile", method = GET)
    @ResponseBody
    public UserProfile fetchUserProfile(@AuthenticationPrincipal SessionUser user) {
        return userProfiles.read(user);
    }

    @RequestMapping(value = "/user/profile", method = POST)
    @ResponseBody
    public UserProfile updateUserProfile(@AuthenticationPrincipal SessionUser user, @RequestBody UserProfile userProfile) {
        userProfile.setId(user.getId());
        return userProfiles.update(userProfile);
    }


    @RequestMapping(value = "/user/change-password", method = POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@AuthenticationPrincipal SessionUser user, @RequestBody ChangePasswordRequest request) {
        request.setUserId(user.getId());
        userProfiles.changePassword(request);
    }

}
