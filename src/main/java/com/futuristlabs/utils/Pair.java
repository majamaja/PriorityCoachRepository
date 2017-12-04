package com.futuristlabs.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pair<First, Second> {
    private final First first;
    private final Second second;
}
