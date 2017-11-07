package com.futuristlabs.rest.v1_0;

import com.futuristlabs.func.resources.CreateResourceResponse;
import com.futuristlabs.func.resources.ResourceData;
import com.futuristlabs.func.resources.ResourceDataRepository;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@RequestMapping(value = "/v1.0/resources")
public class ResourcesEndpoint {
    private final ResourceDataRepository resourceDataRepository;

    @Autowired
    public ResourcesEndpoint(ResourceDataRepository resourceDataRepository) {
        this.resourceDataRepository = resourceDataRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public CreateResourceResponse createResource(@RequestHeader(value = "Content-Type") String contentType,
                                                 @ApiParam(name = "body", value = "Resource content")
                                                 @RequestBody final byte[] content) {
        ResourceData newResource = new ResourceData();
        newResource.setMimeType(contentType);
        newResource.setContent(content);

        final UUID resourceId = resourceDataRepository.create(newResource);
        return new CreateResourceResponse(resourceId);
    }

    @RequestMapping(value = "/{resourceId}", method = RequestMethod.GET)
    public byte[] getResource(@PathVariable(name = "resourceId") final UUID resourceId,
                              final HttpServletResponse httpServletResponse) {

        ResourceData resourceData = resourceDataRepository.getById(resourceId);
        httpServletResponse.setHeader("Content-Type", resourceData.getMimeType());
        return resourceData.getContent();
    }
}
