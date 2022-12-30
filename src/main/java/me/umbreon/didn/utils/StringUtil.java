package me.umbreon.didn.utils;

public class StringUtil {

    public final static String EMPTY_STRING = "";
    public final static String NEW_LINE = "\n";

    public static String convertEmojiToUnicode(String emoji) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < emoji.length(); i++) {
            if (Character.isSurrogate(emoji.charAt(i))) {
                int res = Character.codePointAt(emoji, i);
                i++;
                sb.append("U+").append(Integer.toHexString(res).toUpperCase());
            } else {
                sb.append(emoji.charAt(i));
            }
        }

        return sb.toString();
    }

    public static boolean isStringSingleDashWithDigits(String string) {
        return string.matches("-[0-9]+");
    }

    public static boolean isStringInTimePattern(String string) {
        return string.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$");
    }

    public static String removeAllNonNumericCharacters(String value) {
        return value.replaceAll("[^\\d]", "");
    }
}
