package com.futuristlabs.func.properties;


import java.util.Optional;

public interface SystemPropertiesRepository {
    Optional<String> findByName(String name);
}
