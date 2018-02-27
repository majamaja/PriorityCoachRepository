package com.futuristlabs.func.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ResourceDataService {
    private final ResourceDataRepository repository;

    @Autowired
    public ResourceDataService(ResourceDataRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public CreateResourceResponse create(final String mimeType, final byte[] content) {
        ResourceData newResource = new ResourceData();
        newResource.setMimeType(mimeType);
        newResource.setContent(content);

        final UUID resourceId = repository.create(newResource);
        return new CreateResourceResponse(resourceId);
    }

    public ResourceData readById(final UUID id) {
        return repository.getById(id);
    }
}
