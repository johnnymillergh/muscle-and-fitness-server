package com.jmsoftware.maf.common.util;

import java.util.Random;
import java.util.regex.Pattern;

/**
 * <h1>CaseConversionUtil</h1>
 * The text conversion class helps changing the cases of an existing text. Get
 * more information and online tools for this implementation on: <a href=
 * "https://en.toolpage.org/cat/text-conversion">https://en.toolpage.org/cat/text-conversion</a>
 *
 * @author Yves Sorge <yves.sorge@toolpage.org>
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com
 * @date 2/27/20 9:45 AM
 * @see
 * <a href='https://github.com/toolpage/java-case-converter/blob/master/src/org/toolpage/util/text/CaseConverter.java'>Source</a>
 */
@SuppressWarnings("unused")
public class CaseConversionUtil {
    /**
     * Filter non-alphabet and non-number string.
     *
     * @param value the value
     * @return filtered string.
     */
    public static String filterNonAlphabetAndNonNumber(final String value) {
        return value.replaceAll("[^0-9a-zA-Z]", "");
    }

    /**
     * Converts a text into uppercase. The converted text will only
     * consist of uppercase letters.
     *
     * @param value the value
     * @return converted string.
     */
    public static String convertToUpperCase(final String value) {
        return value.toUpperCase();
    }

    /**
     * Converts a text into lowercase. The converted text will only
     * consist of lowercase letters.
     *
     * @param value the value
     * @return converted string.
     */
    public static String convertToLowerCase(final String value) {
        return value.toLowerCase();
    }

    /**
     * Converts a text into start case / title case.
     *
     * @param value the value
     * @return converted string.
     */
    public static String convertToStartCase(final String value) {
        StringBuilder returnValue = new StringBuilder();
        var lowerCaseValue = value.toLowerCase();
        boolean makeNextUppercase = true;
        for (char c : lowerCaseValue.toCharArray()) {
            if (Character.isSpaceChar(c) || Character.isWhitespace(c) || "()[]{}\\/".indexOf(c) != -1) {
                makeNextUppercase = true;
            } else if (makeNextUppercase) {
                c = Character.toTitleCase(c);
                makeNextUppercase = false;
            }

            returnValue.append(c);
        }
        return returnValue.toString();
    }

    /**
     * Converts a text into alternating case.
     * Example: "Alternating CASE" into "AlTeRnAtInG cAsE".
     *
     * @param value the value
     * @return converted string.
     */
    public static String convertToAlternatingCase(final String value) {
        return CaseConversionUtil.convertToAlternatingCase(value, true);
    }

    /**
     * Converts a text into alternating case.
     * Example #1: "Alternating CASE" into "AlTeRnAtInG cAsE" (with startWithCapitalLetter = true).
     * Example #2: "Alternating CASE" into "aLtErNaTiNg CaSe" (with startWithCapitalLetter = false).
     *
     * @param value                  the value
     * @param startWithCapitalLetter the start with capital letter
     * @return converted string.
     */
    public static String convertToAlternatingCase(final String value, boolean startWithCapitalLetter) {
        StringBuilder returnValue = new StringBuilder();
        var lowerCaseValue = value.toLowerCase();
        int index = 0;
        if (!startWithCapitalLetter) {
            index = 1;
        }
        for (char c : lowerCaseValue.toCharArray()) {
            if (Character.isAlphabetic(c)) {
                if (index % 2 == 0) {
                    c = Character.toUpperCase(c);
                }
                index++;
            }
            returnValue.append(c);
        }
        return returnValue.toString();
    }

    /**
     * Converts a text into camel case.
     * Example: "camel case" into "CamelCase".
     *
     * @param value the value
     * @return converted string.
     */
    public static String convertToCamelCase(final String value) {
        String throwAwayChars = "()[]{}=?!.:,-_+\\\"#~/";
        var replacedValue = value.replaceAll("[" + Pattern.quote(throwAwayChars) + "]", " ");
        replacedValue = CaseConversionUtil.convertToStartCase(replacedValue);
        return replacedValue.replaceAll("\\s+", "");
    }

    /**
     * Converts a text into snake case.
     * Example: "snake case" into "Snake_Case".
     *
     * @param value the value
     * @return converted string.
     */
    public static String convertToSnakeCase(final String value) {
        String throwAwayChars = "()[]{}=?!.:,-_+\\\"#~/";
        var replacedValue = value.replaceAll("[" + Pattern.quote(throwAwayChars) + "]", " ");
        replacedValue = CaseConversionUtil.convertToStartCase(replacedValue);
        return replacedValue.trim().replaceAll("\\s+", "_");
    }

    /**
     * Converts a text into kebab case.
     * Example: "Kebab Case" into "kebab-case".
     *
     * @param value the value
     * @return converted string.
     */
    public static String convertToKebabCase(final String value) {
        String throwAwayChars = "()[]{}=?!.:,-_+\\\"#~/";
        var replacedValue = value.replaceAll("[" + Pattern.quote(throwAwayChars) + "]", " ");
        replacedValue = replacedValue.toLowerCase();
        return replacedValue.trim().replaceAll("\\s+", "-");
    }

    /**
     * Converts a text into studly caps. Studly caps is a text case where the
     * capitalization of letters varies randomly.
     * Example: "Studly Caps" into "stuDLY CaPS" or "STudLy CAps".
     *
     * @param value the value
     * @return converted string.
     */
    public static String convertToStudlyCaps(final String value) {
        StringBuilder returnValue = new StringBuilder();
        var lowerCaseValue = value.toLowerCase();
        int numOfFollowingUppercase = 0;
        int numOfFollowingLowercase = 0;
        boolean doCapitalLetter;
        Random randomizer = new Random();
        for (char c : lowerCaseValue.toCharArray()) {
            if (Character.isAlphabetic(c)) {
                if (numOfFollowingUppercase < 2) {
                    if (randomizer.nextInt(100) % 2 == 0) {
                        doCapitalLetter = true;
                        numOfFollowingUppercase++;
                    } else {
                        doCapitalLetter = false;
                        numOfFollowingUppercase = 0;
                    }
                } else {
                    doCapitalLetter = false;
                    numOfFollowingUppercase = 0;
                }
                if (!doCapitalLetter) {
                    numOfFollowingLowercase++;
                }
                if (numOfFollowingLowercase > 3) {
                    doCapitalLetter = true;
                    numOfFollowingLowercase = 0;
                    numOfFollowingUppercase++;
                }
                if (doCapitalLetter) {
                    c = Character.toUpperCase(c);
                }
            }
            returnValue.append(c);
        }
        return returnValue.toString();
    }

    /**
     * Inverts the case of a given text.
     * Converts the spelling of each letter in the reverse order:
     * lowercase letters are converted to uppercase and vice versa.
     * Example: "Inverted Case" into "iNVERTED cASE".
     *
     * @param value the value
     * @return converted string.
     */
    public static String invertCase(final String value) {
        StringBuilder returnValue = new StringBuilder();
        for (char c : value.toCharArray()) {
            if (Character.isAlphabetic(c)) {
                if (Character.isLowerCase(c)) {
                    c = Character.toUpperCase(c);
                } else {
                    c = Character.toLowerCase(c);
                }
            }
            returnValue.append(c);
        }
        return returnValue.toString();
    }
}
