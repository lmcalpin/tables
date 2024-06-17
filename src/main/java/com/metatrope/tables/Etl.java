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

import com.metatrope.tables.exception.TableExporterException;
import com.metatrope.tables.exporter.Exporter;
import com.metatrope.tables.importer.Importer;
import com.metatrope.tables.model.Row;
import com.metatrope.tables.transformer.RowTransformer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A helper class to convert data from one tabular format to another. The input is referred to
 * as the source, and the representation we want to transform it to is the sink.
 *
 * @author Lawrence McAlpin (admin@lmcalpin.com)
 */
public class Etl {
    private Importer source;
    private Exporter sink;
    private List<RowTransformer> converters = new ArrayList<>();

    public Etl(Importer importer) {
        this.source = importer;
    }

    public static Etl source(Importer importer) {
        Etl tables = new Etl(importer);
        return tables;
    }

    public void convert(OutputStream os) {
        sink.setOutputStream(os);
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
                    for (RowTransformer converter : converters) {
                        currentRow = converter.transform(currentRow);
                    }
                }
                if (currentRow != null) {
                    sink.onNext(currentRow);
                }
            }
        } while (currentRow != null);
        sink.onCompleted();
    }

    public String convertToString() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        convert(baos);
        return baos.toString();
    }

    public byte[] convertToByteArray() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        convert(baos);
        return baos.toByteArray();
    }
    
    public void toFile(Path path) {
        toFile(path.toString());
    }
    
    public void toFile(File file) {
        toFile(file.getPath());
    }
    
    public void toFile(String path) {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            convert(fos);
        } catch (FileNotFoundException e) {
            throw new TableExporterException(e);
        } catch (IOException e) {
            throw new TableExporterException(e);
        }
    }

    public Etl sink(Exporter exporter) {
        this.sink = exporter;
        return this;
    }

    public Etl transform(RowTransformer converter) {
        converters.add(converter);
        return this;
    }
}
