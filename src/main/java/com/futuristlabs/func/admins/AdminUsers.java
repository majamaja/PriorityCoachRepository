package com.futuristlabs.func.admins;

import com.futuristlabs.func.auth.ChangePasswordRequest;
import com.futuristlabs.utils.RandomStringGenerator;
import com.futuristlabs.utils.emails.EmailSender;
import com.futuristlabs.utils.repository.Page;
import com.futuristlabs.utils.repository.PageData;
import com.futuristlabs.utils.repository.SortBy;
import com.futuristlabs.utils.repository.SortOrder;
import com.futuristlabs.utils.velocity.VelocityTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AdminUsers {
    private final AdminUsersRepository repository;
    private final RandomStringGenerator generator;
    private final EmailSender emailSender;
    private final VelocityTemplate template;

    @Autowired
    public AdminUsers(AdminUsersRepository repository, RandomStringGenerator generator, EmailSender emailSender, VelocityTemplate template) {
        this.repository = repository;
        this.generator = generator;
        this.emailSender = emailSender;
        this.template = template;
    }

    public PageData<AdminUser> readPaged(final Page page, final SortBy<AdminUser> sortBy, SortOrder sortOrder, AdminUserSearch search) {
        return repository.findPaged(page, sortBy, sortOrder, search);
    }

    public AdminUser readById(final UUID adminId) {
        return repository.findById(adminId);
    }

    @Transactional
    public AdminUser create(final AdminUser admin) {
        final String password = generator.generatePassword();
        admin.setPassword(password);
        final UUID adminId = repository.insert(admin);
        admin.setId(adminId);

        final Map<String, Object> params = new HashMap<>();
        params.put("admin", admin);

        final String subject = template.merge("emails/create_admin_subject.vm", params);
        final String body = template.merge("emails/create_admin_body.vm", params);

        emailSender.sendEmail(admin.getEmail(), subject, body);

        return admin;
    }

    @Transactional
    public AdminUser update(final AdminUser admin) {
        repository.update(admin);
        return admin;
    }

    @Transactional
    public void changePassword(final UUID adminId, final ChangePasswordRequest request) {
        repository.changePasswordById(adminId, request.getOldPassword(), request.getNewPassword());
    }

    @Transactional
    public void forgottenPassword(final ForgottenPasswordRequest request) {
        final AdminUser admin = repository.findByUsername(request.getUsername());
        if (admin != null) {
            final String newPassword = generator.generatePassword();
            repository.resetPasswordById(admin.getId(), newPassword);

            final Map<String, Object> params = new HashMap<>();
            params.put("newPassword", newPassword);

            final String subject = template.merge("emails/reset_admin_password_subject.vm", params);
            final String body = template.merge("emails/reset_admin_password_body.vm", params);
            emailSender.sendEmail(admin.getEmail(), subject, body);
        }
    }

    @Transactional
    public void blockById(final UUID adminId) {
        repository.setBlockedById(adminId, true);
    }

    @Transactional
    public void unblockById(final UUID adminId) {
        repository.setBlockedById(adminId, false);
    }

    @Transactional
    public void deleteById(final UUID adminId) {
        repository.deleteById(adminId);
    }
}
