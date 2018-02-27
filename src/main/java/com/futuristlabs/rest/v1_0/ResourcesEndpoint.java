package com.futuristlabs.rest.v1_0;

import com.futuristlabs.func.resources.ResourceData;
import com.futuristlabs.func.resources.ResourceDataService;
import com.futuristlabs.utils.rest.LastModifiedHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping(value = "/v1.0/resources")
public class ResourcesEndpoint {
    private final ResourceDataService resources;

    @Autowired
    public ResourcesEndpoint(ResourceDataService resources) {
        this.resources = resources;
    }

    @RequestMapping(value = "/{resourceId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadResource(@RequestHeader(value = "If-Modified-Since", required = false)
                                                   @DateTimeFormat(pattern = "EEE, dd MMM yyyy HH:mm:ss zzz") final LocalDateTime lastModifiedSince,
                                                   @PathVariable(name = "resourceId") final UUID resourceId,
                                                   HttpServletResponse response) {

        final ResourceData resourceData = new LastModifiedHeader<>(ResourceData::getCreatedAt)
                .checkSetAndReturn(response, resources.readById(resourceId), lastModifiedSince);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(resourceData.getMimeType()));

        return new ResponseEntity<>(resourceData.getContent(), headers, HttpStatus.OK);
    }
}
