package com.futuristlabs.repos.jdbc.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Parameter<T> {
    private final String param;
    private final T value;
}
