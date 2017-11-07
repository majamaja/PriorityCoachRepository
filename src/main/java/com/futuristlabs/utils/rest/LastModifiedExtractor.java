package com.futuristlabs.utils.rest;

import java.time.LocalDateTime;

public interface LastModifiedExtractor<T> {
    LocalDateTime getLastModified(T entry);
}
