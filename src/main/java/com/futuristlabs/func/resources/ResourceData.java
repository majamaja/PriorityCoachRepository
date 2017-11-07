package com.futuristlabs.func.resources;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ResourceData {
	private UUID id;
	private String mimeType;
	private LocalDateTime createdOn;
	private byte[] content;
}
