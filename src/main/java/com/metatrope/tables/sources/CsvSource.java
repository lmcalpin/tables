/*******************************************************************************
 * Copyright (c) 2015 Lawrence McAlpin
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Lawrence McAlpin - initial API and implementation
 *******************************************************************************/
package com.metatrope.tables.sources;

import com.metatrope.tables.exception.TableImporterException;
import com.metatrope.tables.model.Column;
import com.metatrope.tables.model.DataType;
import com.metatrope.tables.model.Format;
import com.metatrope.tables.model.Row;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class CsvSource extends PeekableSource {
    private Stream<String> stream;
    private Iterator<String> iterator;
    private boolean loadedHeader = false;

    public CsvSource(File file) throws IOException {
        this(Files.readString(file.toPath()));
    }

    public CsvSource(String csv) {
        stream = Pattern.compile("\n").splitAsStream(csv);
        iterator = stream.iterator();
    }

    public CsvSource(Format format, String csv) {
        this(csv);
        loadedHeader = true;
    }

    public static List<String> splitCSV(String line) {
        List<String> elements = new ArrayList<String>();
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("(?:^|,)(\"(?:[^\"]|\"\")*\"|[^,]*)").matcher(line);
        while (m.find()) {
            elements.add(m.group().replaceAll("^,", "") // remove first comma if any
                    .replaceAll("^?\"(.*)\"$", "$1") // remove outer quotations if any
                    .replaceAll("\"\"", "\"")); // replace double inner quotations if any
        }
        return elements;
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }

    private String nextLine() {
        try {
            String line = iterator.next();
            if (line == null)
                return null;
            if (line.isEmpty()) {
                do {
                    line = iterator.next();
                } while (line != null && line.isEmpty());
            }
            if (line == null)
                return null;
            return line;
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    protected Row readRow() {
        String line = nextLine();
        if (line == null)
            return null;
        List<String> values = splitCSV(line);
        if (loadedHeader) {
            Row datarow = new Row(format);
            int i = 0;
            for (String value : values) {
                Column header = format.findColumn(i++);
                if (header != null) {
                    datarow.setValue(header, value);
                } else {
                    throw new TableImporterException("Column not in file format: " + header);
                }
            }
            return datarow;
        } else {
            this.format = new Format();
            for (String column : values) {
                format.addColumn(column, DataType.UNKNOWN);
            }
            loadedHeader = true;
            return next();
        }
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }
}
