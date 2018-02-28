package com.futuristlabs.func.admins;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminLoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
