package net.purplegoose.didnb.utils;

import java.util.Random;

public class StringUtil {

    public final static String EMPTY_STRING = "";
    public final static String NEW_LINE = "\n";
    public final static String FORMATTED_MESSAGE = "```";
    public final static String FAILED_MESSAGE = "FAILED!";

    public static boolean isStringSingleDashWithDigits(String string) {
        return string.matches("-[0-9]+");
    }

    public static boolean isStringNotInTimePattern(String string) {
        return !string.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$");
    }

    public static String removeAllNonNumericCharacters(String value) {
        return value.replaceAll("[^\\d]", "");
    }

    public static boolean isStringOnlyContainingNumbers(String string) {
        return string.matches("[0-9]+");
    }

    public static String generateRandomID() {
        return new Random().ints(4, 0, 62)
                .mapToObj("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"::charAt)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
    }

}
