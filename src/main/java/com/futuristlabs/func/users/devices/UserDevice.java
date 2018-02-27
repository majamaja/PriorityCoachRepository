package com.futuristlabs.func.users.devices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDevice {
    @NotNull
    private UUID installationId;

    @NotBlank
    private String type;

    @NotBlank
    private String token;
}
