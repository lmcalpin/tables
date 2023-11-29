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
package com.metatrope.tables.exporter;

import com.metatrope.tables.Column;
import com.metatrope.tables.DataType;
import com.metatrope.tables.Format;
import com.metatrope.tables.Row;
import com.metatrope.tables.Value;
import com.metatrope.util.FormatUtils;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.common.base.Strings;

public class CsvExporter extends StringExporter {
    public CsvExporter() {
        super();
    }

    public CsvExporter(StringWriter writer) {
        super(writer);
    }

    private String checkForComma(String s) {
        if (s.indexOf(',') >= 0)
            return "\"" + s + "\"";
        return s;
    }

    private String format(Object o, Column c) {
        if (o == null)
            return "";
        if (o instanceof String) {
            String s = (String) o;
            return checkForComma(s);
        }
        if (o instanceof Date) {
            if (c.getDataType() == DataType.DATE) {
                String dateFormat = c.getMetadata();
                if (!Strings.isNullOrEmpty(dateFormat)) {
                    // return DateUtil.formatDate((Date)o, dateFormat);
                    SimpleDateFormat ratef = new SimpleDateFormat(dateFormat);
                    String formattedRate = ratef.format((Date) o);
                    return formattedRate;
                }
            }
            return FormatUtils.formatDate((Date) o);
        }
        if (o instanceof Double) {
            return FormatUtils.formatDecimalNoCommas((Double) o);
        }
        if (o instanceof BigDecimal) {
            return FormatUtils.formatDecimalNoCommas(((BigDecimal) o).doubleValue());
        }
        return o.toString();
    }

    @Override
    public void onNext(Row row) {
        processRow(row);
    }

    @Override
    public void onStart(Format format) {
        processHeader(format);
    }

    private void processHeader(Format format) {
        if (format == null)
            return;
        List<Column> columns = format.getColumns();
        int i = 0;
        for (Column c : columns) {
            append(checkForComma(c.getID()));
            if (++i < columns.size())
                append(",");
        }
        append("\n");
    }

    private void processRow(Row r) {
        int i = 0;
        List<Column> columns = r.getColumnsInDefaultOrder();
        for (Value v : r.getDataInDefaultOrder()) {
            String value = format(v.getObject(), v.getColumn());
            append(value);
            if (++i < columns.size())
                append(",");
        }
        append("\n");
    }
}
