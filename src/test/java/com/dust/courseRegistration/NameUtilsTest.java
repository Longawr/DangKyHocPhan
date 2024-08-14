package com.dust.courseRegistration;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.dust.courseRegistration.util.NameUtils;

public class NameUtilsTest {

    // Phương thức kiểm thử cho combineName
    @Test
    public void testCombineName_normalName() {
        // Các trường hợp kiểm thử
        assertEquals("John Doe", NameUtils.combineName("John", "Doe"));
    }

    @Test
    public void testCombineName_blankWithin() {
        assertEquals("John Doe", NameUtils.combineName(" John ", " Doe "));
        assertEquals("John", NameUtils.combineName("John", "  "));
        assertEquals("Doe", NameUtils.combineName("  ", "Doe"));
    }

    @Test
    public void testCombineName_emptyString() {
        assertEquals("", NameUtils.combineName("", ""));
        assertEquals("John", NameUtils.combineName("John", ""));
        assertEquals("Doe", NameUtils.combineName("", "Doe"));
    }

    @Test
    public void testCombineName_nullWithin() {
        assertEquals("", NameUtils.combineName(null, null));
        assertEquals("John", NameUtils.combineName("John", null));
        assertEquals("Doe", NameUtils.combineName(null, "Doe"));
    }

    @Test
    public void testSplitFullName_empty() {
        assertArrayEquals(new String[] {"", ""}, NameUtils.splitFullName(""));
    }

    @Test
    public void testSplitFullName_singleName() {
        assertArrayEquals(new String[] {"", "Smith"}, NameUtils.splitFullName("Smith"));
    }

    @Test
    public void testSplitFullName_firstAndLastName() {
        assertArrayEquals(new String[] {"John", "Smith"}, NameUtils.splitFullName("John Smith"));
    }

    @Test
    public void testSplitFullName_middleName() {
        assertArrayEquals(new String[] {"John Michael", "Smith"}, NameUtils.splitFullName("John Michael Smith"));
    }

    @Test
    public void testSplitFullName_multipleSpaces() {
        assertArrayEquals(new String[] {"John", "Smith"}, NameUtils.splitFullName("  John   Smith  "));
    }

    @Test
    public void testSplitFullName_blankString() {
        assertArrayEquals(new String[] {"", ""}, NameUtils.splitFullName("   "));
    }

    @Test
    public void testSplitFullName_nullString() {
        assertArrayEquals(new String[] {"", ""}, NameUtils.splitFullName(null));
    }

    @Test
    public void testSplitFullName_specialCharacters() {
        assertArrayEquals(new String[] {"John", "@Smith"}, NameUtils.splitFullName("John @Smith"));
    }
}
