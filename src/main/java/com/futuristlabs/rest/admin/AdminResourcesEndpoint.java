package com.futuristlabs.rest.admin;

import com.futuristlabs.func.resources.CreateResourceResponse;
import com.futuristlabs.func.resources.ResourceData;
import com.futuristlabs.func.resources.ResourceDataService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping(value = "/admin/resources")
public class AdminResourcesEndpoint {
    private final ResourceDataService resources;

    @Autowired
    public AdminResourcesEndpoint(ResourceDataService resources) {
        this.resources = resources;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public CreateResourceResponse uploadImage(@ApiParam(name = "body", value = "File content")
                                              @RequestParam("file") MultipartFile file) throws IOException {
        return resources.create(file.getContentType(), file.getBytes());
    }

    @RequestMapping(value = "/{resourceId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadImage(@PathVariable(name = "resourceId") final UUID resourceId) {
        final ResourceData resourceData = resources.readById(resourceId);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(resourceData.getMimeType()));
        return new ResponseEntity<>(resourceData.getContent(), headers, HttpStatus.OK);
    }
}
