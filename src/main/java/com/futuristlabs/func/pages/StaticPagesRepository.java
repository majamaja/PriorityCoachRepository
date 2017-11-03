package com.futuristlabs.func.pages;

import java.time.LocalDateTime;

public interface StaticPagesRepository {
	StaticPage getPageByName(String pageName, LocalDateTime lastModifiedSince);
}
