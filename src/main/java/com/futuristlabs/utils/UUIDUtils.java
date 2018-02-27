package com.futuristlabs.utils;

import java.util.UUID;

import static org.apache.commons.codec.digest.DigestUtils.md5;

public class UUIDUtils {
    public static UUID stringToUUID(String str) {
        return UUID.nameUUIDFromBytes(md5(str));
    }
}
