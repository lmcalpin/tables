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

import com.metatrope.tables.model.Column;
import com.metatrope.tables.model.DataType;
import com.metatrope.tables.model.Format;
import com.metatrope.tables.model.Row;
import com.metatrope.tables.model.Value;
import com.metatrope.util.FormatUtils;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.common.base.Strings;

public class XmlExporter extends StringExporter {
    private String rootName = "report";

    public XmlExporter() {
        super();
    }

    private String format(Object o, Column c) {
        if (o == null)
            return "";
        if (o instanceof String) {
            String s = (String) o;
            return s;
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
            return FormatUtils.formatDecimal((Double) o);
        }
        if (o instanceof BigDecimal) {
            return FormatUtils.formatDecimal(((BigDecimal) o).doubleValue());
        }
        return o.toString();
    }

    @Override
    public void onCompleted() {
        append("</" + rootName + ">\n");
    }

    @Override
    public void onNext(Row row) {
        processRow(row);
    }

    @Override
    public void onStart(Format format) {
        startReport(format);
    }

    public void processRow(Row r) {
        append(" <row>\n");
        for (Value v : r.getDataInDefaultOrder()) {
            String columnName = v.getColumn().getID();
            String value = format(v.getObject(), v.getColumn());
            append("   <" + columnName + ">" + value + "</" + columnName + ">\n");
        }
        append(" </row>\n");
    }

    public XmlExporter rootNamed(String name) {
        this.rootName = name;
        return this;
    }

    public void startReport(Format format) {
        append("<" + rootName + ">\n");
    }

}
