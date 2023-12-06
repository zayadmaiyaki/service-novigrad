package com.example.servicenovigrad;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TestFormatHours {
    @Test
    public void validWorkingHoursFormat() {
        String workingHours = "Monday 09AM";
        assertTrue(isValidWorkingHoursFormat(workingHours));
    }

    @Test
    public void invalidWorkingHoursFormatMissingDay() {
        String workingHours = "09AM";
        assertFalse(isValidWorkingHoursFormat(workingHours));
    }

    @Test
    public void invalidWorkingHoursFormatMissingTime() {
        String workingHours = "Monday";
        assertFalse(isValidWorkingHoursFormat(workingHours));
    }

    @Test
    public void invalidWorkingHoursFormatIncorrectDay() {
        String workingHours = "WrongDay 09AM";
        assertFalse(isValidWorkingHoursFormat(workingHours));
    }

    @Test
    public void invalidWorkingHoursFormatIncorrectTime() {
        String workingHours = "Monday 25PM";
        assertFalse(isValidWorkingHoursFormat(workingHours));
    }

    private boolean isValidWorkingHoursFormat(String workingHours) {
        // Implement your validation logic here
        String regex = "\\b(?:Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday)\\s(?:0[1-9]|1[0-2])(?:AM|PM)";
        return workingHours.matches(regex);
    }
}
