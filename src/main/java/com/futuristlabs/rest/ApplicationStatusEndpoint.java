package com.futuristlabs.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class ApplicationStatusEndpoint {
	@AllArgsConstructor
	@Data
	private static class ApplicationStatusResponse {
		private final String status;
		private final String apiVersion;
		private final LocalDateTime timestamp;
	}

	@RequestMapping(value = "/status", method = RequestMethod.GET)
	public ApplicationStatusResponse getApplicationStatus() {
		return new ApplicationStatusResponse("ok", "v1.0", LocalDateTime.now());
	}
}
