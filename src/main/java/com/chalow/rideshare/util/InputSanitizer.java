package com.chalow.rideshare.util;

public class InputSanitizer {
    public static String sanitizeAndTrim(String s) {
        if (s == null) return null;
        return s.trim();
    }

    public static String sanitizeMessage(String s) {
        if (s == null) return null;
        return s.replaceAll("[\n\r]", " ");
    }

    public static String sanitizeAddress(String s) {
        if (s == null) return null;
        return s.trim();
    }
}
