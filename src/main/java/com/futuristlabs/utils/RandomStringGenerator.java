package com.futuristlabs.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomStringGenerator {
    private static final int DEFAULT_PASSWORD_LENGTH = 8;
    private static final String DEFAULT_PASSWORD_SYMBOLS =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789+-*/_!@#$%&";

    private final Random random;

    public RandomStringGenerator() {
        random = new Random();
    }

    public String generatePassword() {
        return generate(DEFAULT_PASSWORD_SYMBOLS, DEFAULT_PASSWORD_LENGTH);
    }

    public String generate(final String symbols, final int len) {
        final StringBuilder sb = new StringBuilder();
        random.ints(len, 0, symbols.length()).mapToObj(symbols::charAt).forEach(sb::append);
        return sb.toString();
    }
}
