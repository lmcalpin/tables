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

import com.metatrope.tables.conversion.RowConverter;
import com.metatrope.tables.exporter.Exporter;
import com.metatrope.tables.importer.Importer;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A helper class to convert data from one tabular format to another. The input is referred to
 * as the source, and the representation we want to transform it to is the sink.
 *
 * @author Lawrence McAlpin (admin@lmcalpin.com)
 */
public class Tables<T> {
    private Importer source;
    private Exporter<T> sink;
    private List<RowConverter> converters = new ArrayList<>();

    public Tables(Importer importer) {
        this.source = importer;
    }

    public static <T> Tables<T> source(Importer importer) {
        Tables<T> tables = new Tables<T>(importer);
        return tables;
    }

    private T convert() {
        sink.onStart(source.getFormat());
        Row currentRow;
        do {
            try {
                currentRow = source.next();
            } catch (NoSuchElementException e) {
                currentRow = null;
            }
            if (currentRow != null) {
                if (!converters.isEmpty()) {
                    for (RowConverter converter : converters) {
                        currentRow = converter.convert(currentRow);
                    }
                }
                sink.onNext(currentRow);
            }
        } while (currentRow != null);
        return sink.onCompleted();
    }

    public T sink(Exporter<T> exporter) {
        this.sink = exporter;
        return convert();
    }

    public Tables<T> transform(RowConverter converter) {
        converters.add(converter);
        return this;
    }
}
