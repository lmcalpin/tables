package com.metatrope.tables.transformer;

import com.metatrope.tables.model.Row;

/**
 * A RowTransformer is used with the <code>Etl</code> class to modify the data in rows before
 * sending the data to the output sink.  If an implementation returns null, the Row will be 
 * filtered out. 
 */
@FunctionalInterface
public interface RowTransformer {
    public Row transform(Row in);
}
