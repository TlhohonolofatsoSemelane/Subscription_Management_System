package com.subtrack.server.security;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class OtpService {
    private static class OtpRecord {
        String code;
        long expiresAt;
        OtpRecord(String code, long expiresAt) { this.code = code; this.expiresAt = expiresAt; }
    }

    private final Map<String, OtpRecord> otpMap = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public String generateOtp(String username, int ttlSeconds) {
        String code = String.format("%06d", random.nextInt(1_000_000));
        long expiresAt = System.currentTimeMillis() + (ttlSeconds * 1000L);
        otpMap.put(username, new OtpRecord(code, expiresAt));
        return code;
    }

    public boolean verifyOtp(String username, String otp) {
        OtpRecord rec = otpMap.get(username);
        if (rec == null) return false;
        if (System.currentTimeMillis() > rec.expiresAt) {
            otpMap.remove(username);
            return false;
        }
        boolean ok = rec.code.equals(otp);
        if (ok) otpMap.remove(username); // one-time use
        return ok;
    }
}
