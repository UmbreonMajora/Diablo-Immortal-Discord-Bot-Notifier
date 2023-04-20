package net.purplegoose.didnb.utils;

import java.util.Random;

public class StringUtil {

    private StringUtil() {
        // static use only
    }

    public static final String EMPTY_STRING = "";
    public static final String NEW_LINE = "\n";
    public static final String FORMATTED_MESSAGE = "```";
    public static final String FAILED_MESSAGE = "FAILED!";
    public static final String ENABLED_MESSAGE = "Enabled";
    public static final String DISABLE_MESSAGE = "Disabled";

    private static final Random random = new Random();

    public static boolean isStringSingleDashWithDigits(String string) {
        return string.matches("-[\\d]]+");
    }

    public static boolean isStringNotInTimePattern(String string) {
        return !string.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$");
    }

    public static String removeAllNonNumericCharacters(String value) {
        return value.replaceAll("[^\\d]", "");
    }

    public static boolean isStringOnlyContainingNumbers(String string) {
        return string.matches("[\\d]]+");
    }

    public static String generateRandomID() {
        return random.ints(4, 0, 62)
                .mapToObj("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"::charAt)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
    }

}
