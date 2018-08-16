package com.futuristlabs.func.users;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSession {
    private String sessionId;
    private User user;
}