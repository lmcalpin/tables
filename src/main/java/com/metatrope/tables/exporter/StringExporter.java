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

import com.metatrope.tables.Value;
import com.metatrope.tables.exception.TableExporterException;
import com.metatrope.util.IOUtil;

import java.io.StringWriter;

public abstract class StringExporter implements Exporter<String> {
    private StringWriter writer;

    public StringExporter() {
        this.writer = new StringWriter();
    }

    public StringExporter(StringWriter writer) {
        this.writer = writer;
    }

    protected void append(String o) {
        try {
            writer.append(o);
        } catch (Throwable t) {
            throw new TableExporterException("Error appending to output stream", t);
        }
    }

    protected void append(Value v) {
        try {
            writer.append(v.getObject().toString());
        } catch (Throwable t) {
            throw new TableExporterException("Error appending to output stream", t);
        }
    }

    @Override
    public void close() {
        IOUtil.close(writer);
    }

    @Override
    public String onCompleted() {
        return writer.toString();
    }
}
