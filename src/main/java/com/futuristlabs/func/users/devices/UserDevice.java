package com.futuristlabs.func.users.devices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDevice {
    private UUID installationId;
    private String type;
    private String token;
}
