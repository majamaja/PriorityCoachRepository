package com.futuristlabs.func.resources;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ResourceData {
    private UUID id;
    private LocalDateTime createdAt;
    private String mimeType;
    private byte[] content;
}
