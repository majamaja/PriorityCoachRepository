package com.futuristlabs.func.users;

import com.futuristlabs.repos.jdbc.common.InsertResult;
import com.futuristlabs.utils.repository.Page;
import com.futuristlabs.utils.repository.PageData;
import com.futuristlabs.utils.repository.SortBy;
import com.futuristlabs.utils.repository.SortOrder;

import java.util.UUID;

public interface UsersRepository {
    PageData<User> findPaged(Page page, SortBy<User> sortBy, SortOrder sortOrder, UserSearch search);

    User findById(UUID userId);

    InsertResult insert(User user);

    void update(User user);

    boolean changePassword(UUID userId, String oldPassword, String newPassword);

    boolean resetPassword(String email, String newPassword);

    void deleteById(UUID userId);

    User findByEmail(String email);

    void loginById(UUID userId);
}
