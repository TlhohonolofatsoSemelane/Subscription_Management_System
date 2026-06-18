package com.subtrack.client.util;

public final class ResponseParser {

    private ResponseParser() {}

    public static boolean isOk(String response, String expectedPrefix) {
        return response != null && response.startsWith(expectedPrefix + "_OK");
    }

    public static String message(String response) {
        if (response == null) return "Unknown response";
        String[] parts = response.split(":", 2);
        return parts.length > 1 ? parts[1] : response;
    }
}
