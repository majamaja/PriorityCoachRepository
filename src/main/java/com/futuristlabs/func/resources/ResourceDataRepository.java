package com.futuristlabs.func.resources;

import java.util.UUID;

public interface ResourceDataRepository {
    UUID create(final ResourceData resourceData);

    ResourceData getById(final UUID id);
}
