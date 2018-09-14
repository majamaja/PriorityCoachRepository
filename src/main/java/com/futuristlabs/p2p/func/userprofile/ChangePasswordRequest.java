package com.futuristlabs.p2p.func.userprofile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    private UUID userId;
    private String oldPassword;
    private String newPassword;
}
