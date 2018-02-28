package com.futuristlabs.func.admins;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminUser {
    private UUID id;

    @JsonProperty(access = READ_ONLY)
    @ApiModelProperty(readOnly = true)
    private LocalDateTime createdAt;

    @JsonProperty(access = READ_ONLY)
    @ApiModelProperty(readOnly = true)
    private LocalDateTime updatedAt;

    @JsonProperty(access = READ_ONLY)
    @ApiModelProperty(readOnly = true)
    private LocalDateTime lastPasswordChange;

    @JsonProperty(access = READ_ONLY)
    @ApiModelProperty(readOnly = true)
    private LocalDateTime lastLogin;

    @NotNull
    private String name;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String username;

    @JsonIgnore
    private String password;

    @JsonProperty(access = READ_ONLY)
    @ApiModelProperty(readOnly = true)
    private Boolean isBlocked;
}
