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
package com.metatrope.tables.importer;

import com.metatrope.tables.model.Format;
import com.metatrope.tables.model.Row;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A representation of tabular data in memory.
 *
 * @author Lawrence McAlpin (admin@lmcalpin.com)
 */
public class ListImporter implements Importer {
    private Format format;
    private List<Row> rows = new ArrayList<>();
    private boolean startIterator = false;
    private Iterator<Row> it;

    public ListImporter(List<List<?>> data) {
        this.format = format;
    }

    public ListImporter(Format format) {
        this.format = format;
    }

    public Row addRow() {
        Row row = new Row(format);
        rows.add(row);
        return row;
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public Format getFormat() {
        return format;
    }

    @Override
    public Row next() {
        if (!startIterator) {
            it = rows.iterator();
            startIterator = true;
        }
        return it.next();
    }
}
