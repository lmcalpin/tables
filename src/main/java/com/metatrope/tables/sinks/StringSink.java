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

import com.metatrope.tables.exception.TableExporterException;
import com.metatrope.tables.model.Value;
import com.metatrope.util.IOUtil;

import java.io.OutputStream;
import java.io.PrintStream;

public abstract class StringSink implements Sink {
    private PrintStream stream;
    private OutputStream os;

    public StringSink() {
    }

    protected void append(String o) {
        try {
            stream.append(o);
        } catch (Throwable t) {
            throw new TableExporterException("Error appending to output stream", t);
        }
    }

    protected void append(Value v) {
        try {
            stream.append(v.getObject().toString());
        } catch (Throwable t) {
            throw new TableExporterException("Error appending to output stream", t);
        }
    }

    @Override
    public void close() {
        if (os != null) {
            IOUtil.close(stream);
            IOUtil.close(os);
        }
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void setOutputStream(OutputStream os) {
        this.os = os;
        this.stream = new PrintStream(os);
    }
}
