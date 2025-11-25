package com.rising.Distributor.util;
import java.util.regex.Pattern;

public final class IdentifierUtils {

    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");


    private IdentifierUtils() {}

    public static boolean isEmail(String s) {
        if (s == null) return false;
        return EMAIL.matcher(s.trim()).matches();
    }

    public static String normalizePhone(String s) {
        if (s == null) return null;
        String digits = s.replaceAll("\\D", "");
        if (digits.length() < 10) return null;
        // If exactly 10 digits assume local number -> add +91 (adjust for your app)
        if (digits.length() == 10) {
            return "+91" + digits;
        }
        // If already includes country code (e.g. 911234567890 or 12345678901), prefix plus
        return "+" + digits;
    }
}