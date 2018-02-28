package com.futuristlabs.func.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
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

    @NotBlank
    private String name;

    @NotNull
    @Email
    private String email;

    @JsonIgnore
    private String password;
}
