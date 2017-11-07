package com.futuristlabs.func.resources;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CreateResourceResponse {
	private final UUID resourceId;
}
