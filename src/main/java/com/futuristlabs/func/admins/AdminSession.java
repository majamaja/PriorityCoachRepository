package com.futuristlabs.func.admins;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminSession {
    private String sessionId;
    private AdminUser user;
}
