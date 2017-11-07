package com.futuristlabs.func.pn;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserNotification {
    private UUID targetUserId;
    private String payload;
}
