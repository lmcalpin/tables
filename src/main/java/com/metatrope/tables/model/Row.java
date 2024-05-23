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
package com.metatrope.tables.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A set of related columns representing a row of data.
 *
 * @author Lawrence McAlpin (admin@lmcalpin.com)
 */
public class Row {
    private static final Logger LOGGER = LoggerFactory.getLogger(Row.class);

    private Map<Column, Value> data = new HashMap<>();
    private Format format;

    public Row(Format format) {
        this.format = format;
    }

    public Map<Column, Value> getAllData() {
        return data;
    }

    public List<Column> getColumnsInDefaultOrder() {
        return format.getColumns();
    }

    public Value getData(Column column) {
        Value value = data.get(column);
        return value;
    }

    public List<Value> getData(List<Column> columns) {
        List<Value> values = new ArrayList<>();
        for (Column c : columns) {
            Value o = data.get(c);
            if (o == null) {
                o = new Value(this, c, null);
            }
            values.add(o);
        }
        return values;
    }

    public Value getData(String columnName) {
        Column column = format.findColumn(columnName);
        Value v = getData(column);
        if (v == null) {
            return new Value(this, column, null);
        }
        return v;
    }

    public Object getDataAsObject(String columnName) {
        Value v = getData(columnName);
        return v.getObject();
    }

    public List<Value> getDataInDefaultOrder() {
        return getData(getColumnsInDefaultOrder());
    }

    public Value setValue(Column c, Object v) {
        if (c == null)
            return null;
        Value nv = new Value(this, c, v);
        data.put(c, nv);
        return nv;
    }

    public Value setValue(int colIdx, Object v) {
        Column c = format.findColumn(colIdx);
        return setValue(c, v);
    }

    public Value setValue(String id, Object v) {
        Column c = format.findColumn(id);
        return setValue(c, v);
    }

    public Row setValues(Object... vs) {
        List<Column> columns = getColumnsInDefaultOrder();
        int i = 0;
        for (Object v : vs) {
            setValue(columns.get(i++), v);
        }
        return this;
    }

    @Override
    public String toString() {
        List<Value> data = getDataInDefaultOrder();
        if (data == null)
            return "[no data]";
        return data.toString();
    }
}
