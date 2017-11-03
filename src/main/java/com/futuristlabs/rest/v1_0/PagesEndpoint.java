package com.futuristlabs.rest.v1_0;

import com.futuristlabs.func.pages.StaticPage;
import com.futuristlabs.func.pages.StaticPagesRepository;
import com.futuristlabs.utils.rest.LastModifiedHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/v1.0/pages")
public class PagesEndpoint {
	private final StaticPagesRepository staticPagesRepository;

	@Autowired
	public PagesEndpoint(final StaticPagesRepository staticPagesRepository) {
		this.staticPagesRepository = staticPagesRepository;
	}

	@RequestMapping(value = "/{pageName}", method = RequestMethod.GET)
	public StaticPage getPageContent(@RequestHeader(name = "User-Locale", defaultValue = "EN") final String locale,
                                     @RequestHeader(value = "If-Modified-Since", required = false)
                                     @DateTimeFormat(pattern = "EEE, dd MMM yyyy HH:mm:ss zzz") final LocalDateTime lastModifiedSince,
                                     final @PathVariable(name = "pageName") String pageName,
									 HttpServletResponse response) {
		return new LastModifiedHeader<>(StaticPage::getLastModified)
				.setAndReturn(response, staticPagesRepository.getPageByName(pageName, lastModifiedSince));
	}
}
