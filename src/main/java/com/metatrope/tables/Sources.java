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
package com.metatrope.tables;

import com.metatrope.tables.exception.TableImporterException;
import com.metatrope.tables.sources.CsvSource;
import com.metatrope.tables.sources.ParquetSource;
import com.metatrope.tables.sources.Source;
import com.metatrope.util.FileUtils;

import java.io.File;
import java.io.IOException;

public class Sources {
    public static CsvSource fromCsv(String csvContents) {
        return new CsvSource(csvContents);
    }

    public static CsvSource fromCsv(File file) {
        try {
            return new CsvSource(file);
        } catch (IOException e) {
            throw new TableImporterException(e);
        }
    }

    public static ParquetSource fromParquet(File file) {
        return new ParquetSource(file);
    }

    public static Source fromFileExtension(File file) {
        try {
            String extension = FileUtils.getExtension(file);
            switch (extension) {
            case "csv":
                return new CsvSource(file);
            case "parquet":
                return new ParquetSource(file);
            default:
                throw new TableImporterException("Unsupported extension: " + extension);
            }
        } catch (IOException e) {
            throw new TableImporterException(e);
        }
    }
}
