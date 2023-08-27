package net.purplegoose.didnb.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilTests {

    @Test
    void isStringNotInTimePattern_Test() {
        List<String> validCombinations = new ArrayList<>();

        String timeHour;
        String timeMinute;
        String time;

        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 60; j++) {

                if (i < 10) {
                    timeHour = "0" + i;
                } else {
                    timeHour = String.valueOf(i);
                }

                if (j < 10) {
                    timeMinute = "0" + j;
                } else {
                    timeMinute = String.valueOf(j);
                }

                time = timeHour + ":" + timeMinute;
                validCombinations.add(time);
                assertFalse(StringUtil.isStringNotInTimePattern(time));
            }
        }

        for (int i = 0; i < 99; i++) {
            for (int j = 0; j < 99; j++) {

                if (i < 10) {
                    timeHour = "0" + i;
                } else {
                    timeHour = String.valueOf(i);
                }

                if (j < 10) {
                    timeMinute = "0" + j;
                } else {
                    timeMinute = String.valueOf(j);
                }

                time = timeHour + ":" + timeMinute;

                if (validCombinations.contains(time)) {
                    assertFalse(StringUtil.isStringNotInTimePattern(time));
                } else {
                    Assertions.assertTrue(StringUtil.isStringNotInTimePattern(time));
                }
            }
        }
    }

    @Test
    void testIsStringSingleDashWithDigits() {
        assertTrue(StringUtil.isStringSingleDashWithDigits("-123"));
        assertFalse(StringUtil.isStringSingleDashWithDigits("123"));
        assertFalse(StringUtil.isStringSingleDashWithDigits("-12a"));
    }

    @Test
    void testRemoveAllNonNumericCharacters() {
        assertEquals("12345", StringUtil.removeAllNonNumericCharacters("12abc3d45"));
        assertEquals("6789", StringUtil.removeAllNonNumericCharacters("6ef7g89"));
    }

    @Test
    void testIsStringOnlyContainingNumbers() {
        assertTrue(StringUtil.isStringOnlyContainingNumbers("12345"));
        assertFalse(StringUtil.isStringOnlyContainingNumbers("12a345"));
        assertFalse(StringUtil.isStringOnlyContainingNumbers("abc"));
    }

    @Test
    void testGenerateRandomID() {
        String randomID = StringUtil.generateRandomID();
        assertNotNull(randomID);
        assertEquals(4, randomID.length());
    }

}
