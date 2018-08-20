package com.futuristlabs.p2p.func.userprofile;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ChangePasswordRequest {
    private UUID userId;
    private String oldPassword;
    private String newPassword;
}
