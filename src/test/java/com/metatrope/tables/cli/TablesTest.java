package com.metatrope.tables.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.metatrope.tables.importer.ParquetImporterTest;

import java.io.File;
import java.net.URL;

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

    /*
    @Test
    public void testConvert() {
        String parquetFile = ParquetImporterTest.class.getResource("/test.parquet").toString();
        new CommandLine(new Tables()).execute("--in", parquetFile, "--out", "test2.csv");
    }
    */
}
