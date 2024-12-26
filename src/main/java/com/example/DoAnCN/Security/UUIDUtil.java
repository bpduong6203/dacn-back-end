package com.example.DoAnCN.Security;

import java.util.UUID;
import java.nio.ByteBuffer;
import java.util.Base64;

public class UUIDUtil {
    public static String generateShortUUID() {
        UUID uuid = UUID.randomUUID();
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bb.array());
    }
}
