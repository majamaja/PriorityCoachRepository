package com.futuristlabs.func.pages;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StaticPage {

    private UUID id;

    @JsonProperty(access = READ_ONLY)
    @ApiModelProperty(readOnly = true)
    private LocalDateTime createdAt;

    @JsonProperty(access = READ_ONLY)
    @ApiModelProperty(readOnly = true)
    private LocalDateTime updatedAt;

    @NotBlank
    private String name;

    @NotNull
    private String header;

    @NotNull
    private String content;
}
