/*******************************************************************************
 * Copyright (c) 2024 Lawrence McAlpin
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Lawrence McAlpin - initial API and implementation
 *******************************************************************************/
package com.metatrope.tables.sources;

import com.metatrope.tables.model.Format;
import com.metatrope.tables.model.Row;

/**
 * An importer that can infer the file format from a header row.
 */
public abstract class PeekableSource implements Source {
    private Row lastRow;
    private boolean mayPeekAhead = true;
    protected Format format;

    @Override
    public Format getFormat() {
        if (format == null && mayPeekAhead) {
            mayPeekAhead = false;
            nextRow(true);
        }
        return format;
    }

    @Override
    public Row next() {
        return nextRow(false);
    }

    protected Row nextRow(boolean peek) {
        if (peek && lastRow == null) {
            lastRow = readRow();
            return lastRow;
        } else if (lastRow != null) {
            Row returnMe = lastRow;
            lastRow = null;
            return returnMe;
        }
        return readRow();
    }

    protected abstract Row readRow();
}
