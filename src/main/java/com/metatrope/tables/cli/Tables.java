/*******************************************************************************
 * Copyright (c) 2024 Lawrence McAlpin
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Lawrence McAlpin - initial implementation
 *******************************************************************************/
package com.metatrope.tables.cli;

import com.metatrope.tables.Etl;
import com.metatrope.tables.Sinks;
import com.metatrope.tables.Sources;

import java.io.File;
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
            Etl.source(Sources.fromFileExtension(in)).sink(Sinks.fromFileExtension(out)).toFile(out);
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
