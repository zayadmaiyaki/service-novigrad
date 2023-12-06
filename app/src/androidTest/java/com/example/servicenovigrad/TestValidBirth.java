package com.example.servicenovigrad;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

// This Class test the validity of the date of birth format
public class TestValidBirth {
    @Test
    public void validDateOfBirth() {
        String dateOfBirth = "1990-01-01";
        assertTrue(isValidDateOfBirth(dateOfBirth));
    }

    @Test
    public void invalidDateOfBirthAsInteger() {
        String dateOfBirth = "19801231";
        assertFalse(isValidDateOfBirth(dateOfBirth));
    }

    private boolean isValidDateOfBirth(String dateOfBirth) {
        try {
            Integer.parseInt(dateOfBirth);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }
}
