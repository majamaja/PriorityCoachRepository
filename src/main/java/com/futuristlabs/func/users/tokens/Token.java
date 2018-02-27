package com.futuristlabs.func.users.tokens;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Token {
    public enum Type {
        RESET_PASSWORD,
    }

    private String token;
    private UUID userId;
    private Type type;
    private LocalDateTime expiryDate;
    private boolean isUsed;
}
