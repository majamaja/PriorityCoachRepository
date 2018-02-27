package com.futuristlabs.rest.admin;

import com.futuristlabs.func.pages.StaticPage;
import com.futuristlabs.func.pages.StaticPages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/admin/pages")
public class AdminPagesEndpoint {
	private final StaticPages staticPages;

	@Autowired
	public AdminPagesEndpoint(StaticPages staticPages) {
		this.staticPages = staticPages;
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<StaticPage> list() {
		return staticPages.readAll();
	}

	@RequestMapping(value = "/{pageId}", method = RequestMethod.GET)
	public StaticPage read(final @PathVariable(name = "pageId") UUID pageId) {
		return staticPages.readById(pageId);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public StaticPage create(final @RequestBody @Valid StaticPage page) {
		return staticPages.create(page);
	}

	@RequestMapping(value = "", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void update(final @RequestBody @Valid StaticPage page) {
		staticPages.update(page);
	}

	@RequestMapping(value = "/{pageId}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(final @PathVariable(name = "pageId") UUID pageId) {
		staticPages.deleteById(pageId);
	}
}
