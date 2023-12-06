package com.example.servicenovigrad;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

// This cl
public class TestValidAdress {
    @Test
    public void validAdress() {
        String location = "123 Main St, City, Country";
        assertTrue(isValidLocation(location));
    }

    @Test
    public void invalidLocationEmpty() {
        String location = "";
        assertFalse(isValidLocation(location));
    }

    @Test
    public void invalidLocationWhitespace() {
        String location = "   ";
        assertFalse(isValidLocation(location));
    }

    private boolean isValidLocation(String location) {
        return !location.trim().isEmpty();
    }
}
