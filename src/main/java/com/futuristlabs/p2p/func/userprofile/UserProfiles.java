package com.futuristlabs.p2p.func.userprofile;

import com.futuristlabs.p2p.func.auth.SessionUser;
import com.futuristlabs.p2p.func.sync.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserProfiles {

    private UsersRepository repository;

    @Autowired
    public UserProfiles(final UsersRepository repository) {
        this.repository = repository;
    }

    public UserProfile read(SessionUser user) {
        return repository.findProfileById(user.getId());
    }

    public UserProfile update(UserProfile user) {
        repository.updateProfile(user);
        return user;
    }

    public void changePassword(final ChangePasswordRequest request) {
        repository.updatePassword(request);
    }
}
