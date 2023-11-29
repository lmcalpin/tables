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
package com.metatrope.tables;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Format {
    private static final Logger LOGGER = LoggerFactory.getLogger(Format.class);

    // the data we capture for this file format
    private List<Column> columns;

    // an index to find columns by name
    private Map<String, Column> columnMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public Format() {

    }

    public Format(List<Column> columns) {
        this.columns = columns;
    }

    public Column addColumn(Column column) {
        getColumns().add(column);
        // update the index
        columnMap.put(column.getID(), column);
        return column;
    }

    public Column addColumn(String name, DataType dataType) {
        return addColumn(new Column(name, dataType));
    }

    public Column addColumn(String name, DataType dataType, String metadata) {
        return addColumn(new Column(name, dataType, metadata));
    }

    public Column findColumn(int idx) {
        return columns.get(idx);
    }

    // search for column in a case insensitive fashion
    public Column findColumn(String id) {
        return columnMap.get(id);
    }

    public List<Column> getColumns() {
        if (columns == null) {
            columns = new ArrayList<Column>();
        }
        return columns;
    }

    public int getNumberOfColumns() {
        return getColumns().size();
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }
}
