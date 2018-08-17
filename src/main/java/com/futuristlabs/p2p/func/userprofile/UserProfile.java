package com.futuristlabs.p2p.func.userprofile;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class UserProfile {
    private UUID id;
    private String name;
    private String email;
}
