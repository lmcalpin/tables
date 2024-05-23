package com.metatrope.tables.importer;

import com.metatrope.tables.model.Format;
import com.metatrope.tables.model.Row;

/**
 * An importer that can infer the file format from a header row.
 *
 * @author Lawrence McAlpin (admin@lmcalpin.com)
 */
public abstract class PeekableImporter implements Importer {
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
