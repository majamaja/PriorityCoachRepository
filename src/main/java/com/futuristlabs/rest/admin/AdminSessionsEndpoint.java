package com.futuristlabs.rest.admin;

import com.futuristlabs.func.admins.AdminLoginRequest;
import com.futuristlabs.func.admins.AdminSession;
import com.futuristlabs.func.admins.AdminUserDetails;
import com.futuristlabs.func.auth.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RequestMapping("/admin/sessions")
@RestController
public class AdminSessionsEndpoint {
    @Autowired
    private AuthService authService;

    @RequestMapping(value = "", method = POST)
    public AdminSession login(final @RequestBody @Valid AdminLoginRequest login, final HttpSession session) {
        AdminUserDetails userDetails = authService.authenticateAdmin(login.getUsername(), login.getPassword());
        return new AdminSession(session.getId(), userDetails.getAdminUser());
    }

    @RequestMapping(value = "", method = DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(final HttpSession session) {
        session.invalidate();
    }
}
