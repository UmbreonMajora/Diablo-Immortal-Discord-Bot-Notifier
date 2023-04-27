package net.purplegoose.didnb.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

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
                Assertions.assertFalse(StringUtil.isStringNotInTimePattern(time));
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
                    Assertions.assertFalse(StringUtil.isStringNotInTimePattern(time));
                } else {
                    Assertions.assertTrue(StringUtil.isStringNotInTimePattern(time));
                }
            }
        }
    }

}
