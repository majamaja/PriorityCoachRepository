package com.futuristlabs.rest.admin;

import com.futuristlabs.func.users.*;
import com.futuristlabs.utils.excel.ExcelBuilder;
import com.futuristlabs.utils.excel.ExcelFile;
import com.futuristlabs.utils.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(value = "/admin/users")
public class UsersAdminEndpoint {
    private final Users users;
    private final ExcelBuilder<User> usersExcelBuilder;

    @Autowired
    public UsersAdminEndpoint(Users users, ExcelBuilder<User> usersExcelBuilder) {
        this.users = users;
        this.usersExcelBuilder = usersExcelBuilder;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public PageData<User> listUsers(final @Valid Page page,
                                    final @Valid UserSearch search,
                                    final @RequestParam(name = "sortBy", required = false, defaultValue = "NAME") UserSortBy sortBy,
                                    final @RequestParam(name = "sortOrder", required = false, defaultValue = "ASC") SortOrder sortOrder) {
        return users.readPaged(page, sortBy, sortOrder, search);
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = ExcelFile.MIME_TYPE)
    public ResponseEntity<byte[]> generateUsersReport(final @Valid UserSearch search,
                                                      final @RequestParam(name = "sortBy", required = false, defaultValue = "NAME") UserSortBy sortBy,
                                                      final @RequestParam(name = "sortOrder", required = false, defaultValue = "ASC") SortOrder sortOrder) {
        final PageData<User> page = users.readPaged(Page.getAll(), sortBy, sortOrder, search);
        return usersExcelBuilder.createResponse("users-report", page.getItems());
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public User getUserProfile(final @PathVariable(name = "userId") UUID userId) {
        return users.readById(userId);
    }

    @RequestMapping(value = "/forgotten-password", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void forgottenPassword(final @RequestBody @Valid ForgottenPasswordRequest request) {
        users.forgottenPassword(request);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(final @PathVariable(name = "userId") UUID userId) {
        users.deleteById(userId);
    }
}
