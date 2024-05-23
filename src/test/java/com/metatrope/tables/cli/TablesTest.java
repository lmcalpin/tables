package com.metatrope.tables.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.File;

import org.junit.jupiter.api.Test;

import picocli.CommandLine;
import picocli.CommandLine.ParseResult;

public class TablesTest {
    @Test
    public void testCli() {
        ParseResult res = new CommandLine(new Tables()).parseArgs("--in", "test.csv", "--out", "test.json");
        File inFile = res.matchedOption("--in").getValue();
        assertEquals("test.csv", inFile.getName());
        File outFile = res.matchedOption("--out").getValue();
        assertEquals("test.json", outFile.getName());
    }

    @Test
    public void testConvertFileNotFound() {
        int ret = new CommandLine(new Tables()).execute("--in", "test.doesnotexist", "--out", "test-output.csv");
        assertEquals(2, ret); // file not found
        // make sure we did not create the output file since we did not do anything
        File f = new File("test-output.csv");
        assertFalse(f.exists());
    }
}
