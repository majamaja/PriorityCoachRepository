package com.futuristlabs.rest.admin;

import com.futuristlabs.func.admins.*;
import com.futuristlabs.func.admins.AdminUserDetails;
import com.futuristlabs.func.auth.ChangePasswordRequest;
import com.futuristlabs.utils.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(value = "/admin/admin-users")
public class AdminUsersAdminEndpoint {
    private final AdminUsers admins;

    @Autowired
    public AdminUsersAdminEndpoint(AdminUsers admins) {
        this.admins = admins;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public PageData<AdminUser> listAdmins(final @Valid Page page,
                                          final @Valid AdminUserSearch search,
                                          final @RequestParam(name = "sortBy", required = false, defaultValue = "NAME") AdminUserSortBy sortBy,
                                          final @RequestParam(name = "sortOrder", required = false, defaultValue = "ASC") SortOrder sortOrder) {
        return admins.readPaged(page, sortBy, sortOrder, search);
    }

    @RequestMapping(value = "/{adminId}", method = RequestMethod.GET)
    public AdminUser getProfile(final @PathVariable(name = "adminId") UUID adminId) {
        return admins.readById(adminId);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public AdminUser create(final @RequestBody @Valid AdminUser admin) {
        return admins.create(admin);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProfile(final @RequestBody @Valid AdminUser admin) {
        admins.update(admin);
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(final @RequestBody @Valid ChangePasswordRequest request,
                               final @AuthenticationPrincipal AdminUserDetails userDetails) {
        admins.changePassword(userDetails.getAdminUser().getId(), request);
    }

    @RequestMapping(value = "/forgotten-password", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void forgottenPassword(final @RequestBody @Valid ForgottenPasswordRequest request) {
        admins.forgottenPassword(request);
    }

    @RequestMapping(value = "/{adminId}/block", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void block(final @PathVariable(name = "adminId") UUID adminId) {
        admins.blockById(adminId);
    }

    @RequestMapping(value = "/{adminId}/unblock", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unblock(final @PathVariable(name = "adminId") UUID adminId) {
        admins.unblockById(adminId);
    }

    @RequestMapping(value = "/{adminId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(final @PathVariable(name = "adminId") UUID adminId) {
        admins.deleteById(adminId);
    }
}
