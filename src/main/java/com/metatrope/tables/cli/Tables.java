package com.metatrope.tables.cli;

import com.metatrope.tables.Etl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Option;

public class Tables implements Callable<Integer> {
    @Option(names = "--in", description = "The input file")
    File in;

    @Option(names = "--out", description = "The output file")
    File out;

    @Override
    public Integer call() throws Exception {
        try {
            if (in != null && !in.exists()) {
                System.out.println("File not found: " + in.getName());
                return 2;
            }
            try (FileOutputStream fos = new FileOutputStream(out)) {
                Etl.source(ImporterFactory.fromFileExtension(in)).sink(ExporterFactory.fromFileExtension(out)).convert(fos);
            }
        } catch (Exception e) {
            System.out.println("Failed to convert: " + e.getMessage());
            return 1;
        }
        return 0;
    }

    public static void main(String[] args) {
        new CommandLine(new Tables()).execute(args);
    }
}
