package com.futuristlabs.func.admins;

import com.futuristlabs.func.admins.AdminUser;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminSession {
    private String sessionId;
    private AdminUser user;
}
