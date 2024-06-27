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
package com.metatrope.tables.sinks;

import com.metatrope.tables.model.Column;
import com.metatrope.tables.model.Format;
import com.metatrope.tables.model.Row;

public class HtmlSink extends StringSink {
    public HtmlSink() {
        super();
    }

    @Override
    public void onCompleted() {
        append("</table>");
    }

    @Override
    public void onNext(Row row) {
        append("<tr>");
        for (Column column : row.getColumnsInDefaultOrder()) {
            append("<td>");
            append(row.getData(column));
            append("</td>");
        }
        append("</tr>");
    }

    @Override
    public void onStart(Format format) {
        append("<table>");
        if (format != null) {
            append("<th>");
            for (Column column : format.getColumns()) {
                append("<td>");
                append(column.getID());
                append("</td>");
            }
            append("</th>");
        }
    }
}
