package com.futuristlabs.func.admins;

import com.futuristlabs.utils.repository.*;

import java.util.UUID;

public interface AdminUsersRepository {
    PageData<AdminUser> findPaged(Page page, SortBy<AdminUser> sortBy, SortOrder sortOrder, AdminUserSearch search);
    AdminUser findById(UUID id);
    AdminUser findByUsername(String username);
    UUID insert(AdminUser admin);
    void update(AdminUser admin);
    void changePasswordById(UUID id, String oldPassword, String newPassword);
    void resetPasswordById(UUID id, String newPassword);
    void loginById(UUID id);
    void setBlockedById(UUID id, boolean blocked);
    void deleteById(UUID id);
}
