package com.futuristlabs.func.pages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StaticPage {
	private UUID id;
	private String name;
	private String header;
	private String content;
	private LocalDateTime lastModified;
}
