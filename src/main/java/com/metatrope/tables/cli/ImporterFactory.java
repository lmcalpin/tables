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

import com.metatrope.tables.exception.TableImporterException;
import com.metatrope.tables.importer.CsvImporter;
import com.metatrope.tables.importer.Importer;
import com.metatrope.tables.importer.ParquetImporter;
import com.metatrope.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ImporterFactory {
    public static Importer fromFileExtension(File file) {
        try {
            String extension = FileUtils.getExtension(file);
            switch (extension) {
            case "csv":
                return new CsvImporter(Files.readString(file.toPath()));
            case "parquet":
                return new ParquetImporter(file.getPath());
            default:
                throw new TableImporterException("Unsupported extension: " + extension);
            }
        } catch (IOException e) {
            throw new TableImporterException(e);
        }
    }

}
