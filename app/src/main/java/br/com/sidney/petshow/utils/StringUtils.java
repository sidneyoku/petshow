package br.com.sidney.petshow.utils;

/**
 * @author Sidney Toshidi Oku
 */
public class StringUtils {

    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }

        return str.trim().length() == 0;
    }

    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }

        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }

        return true;
    }

    public static boolean numberIsBlank(final Integer number) {
        if (number == null) {
            return true;
        }

        return false;
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        } else if(str.equals("")) {
            return false;
        }

        char[] chars = str.toCharArray();
        for (char c : chars) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        return true;
    }
}
