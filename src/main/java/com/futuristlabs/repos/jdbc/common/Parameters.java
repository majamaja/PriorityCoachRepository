package com.futuristlabs.repos.jdbc.common;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Data
@NoArgsConstructor
public class Parameters {
    private final List<Parameter<?>> params = new ArrayList<>();

    public static Parameters fromList(Parameter<?> ... parameters) {
        final Parameters res = new Parameters();
        res.params.addAll(Arrays.asList(parameters));
        return res;
    }

    public Parameters set(String name, Object value) {
        final Parameter<?> param = new Parameter<>(name, value);
        params.add(param);
        return this;
    }

    public Map<String, ?> asMap() {
        return params.stream().collect(toMap(Parameter::getParam, Parameter::getValue));
    }
}

