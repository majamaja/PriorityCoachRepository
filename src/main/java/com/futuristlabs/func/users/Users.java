package com.futuristlabs.func.users;

import com.futuristlabs.func.auth.AuthService;
import com.futuristlabs.func.auth.ChangePasswordRequest;
import com.futuristlabs.func.exceptions.ForbiddenException;
import com.futuristlabs.repos.jdbc.common.InsertResult;
import com.futuristlabs.utils.RandomStringGenerator;
import com.futuristlabs.utils.emails.EmailSender;
import com.futuristlabs.utils.repository.*;
import com.futuristlabs.utils.velocity.VelocityTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class Users {
    private final AuthService authService;
    private final UsersRepository users;
    private final RandomStringGenerator generator;
    private final EmailSender emailSender;
    private final VelocityTemplate template;

    @Autowired
    public Users(AuthService authService, UsersRepository users, RandomStringGenerator generator, EmailSender emailSender, VelocityTemplate template) {
        this.authService = authService;
        this.users = users;
        this.generator = generator;
        this.emailSender = emailSender;
        this.template = template;
    }

    @Transactional
    public void changePassword(final UUID userId, final ChangePasswordRequest request) {
        final boolean updated = users.changePassword(userId, request.getOldPassword(), request.getNewPassword());
        if (!updated) {
            throw new ForbiddenException("INCORRECT_PASSWORD");
        }
    }

    @Transactional
    public void forgottenPassword(final ForgottenPasswordRequest request) {
        final String newPassword = generator.generatePassword();
        final boolean updated = users.resetPassword(request.getEmail(), newPassword);
        if (updated) {
            final Map<String, Object> params = new HashMap<>();
            params.put("newPassword", newPassword);

            final String subject = template.merge("emails/reset_user_password_subject.vm", params);
            final String body = template.merge("emails/reset_user_password_body.vm", params);
            emailSender.sendEmail(request.getEmail(), subject, body);
        }
    }

    public PageData<User> readPaged(final Page page, final SortBy<User> sortBy, final SortOrder sortOrder, final UserSearch search) {
        return users.findPaged(page, sortBy, sortOrder, search);
    }

    public User readById(final UUID userId) {
        return users.findById(userId);
    }

    @Transactional
    public UserSession create(final User user, final HttpSession session) {
        final InsertResult res = users.insert(user);
        user.setId(res.getId());

        final CustomUserDetails details = authService.authenticateUser(user.getEmail(), user.getPassword());
        return new UserSession(session.getId(), user);
    }

    @Transactional
    public void update(final UUID userId, final User user) {
        user.setId(userId);
        users.update(user);
    }

    @Transactional
    public void deleteById(final UUID userId) {
        users.deleteById(userId);
    }
}
