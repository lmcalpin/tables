package com.metatrope.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FileUtilsTest {
    @Test
    public void testExtension() {
        assertEquals("csv", FileUtils.getExtension("/tmp/OUTPUT.CSV"));
        assertEquals(null, FileUtils.getExtension("/tmp/OUTPUT_CSV"));
    }
}
