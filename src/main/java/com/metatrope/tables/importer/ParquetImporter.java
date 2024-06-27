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
package com.metatrope.tables.importer;

import com.metatrope.tables.exception.TableImporterException;
import com.metatrope.tables.model.Format;
import com.metatrope.tables.model.Row;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.column.page.PageReadStore;
import org.apache.parquet.example.data.simple.SimpleGroup;
import org.apache.parquet.example.data.simple.convert.GroupRecordConverter;
import org.apache.parquet.hadoop.ParquetFileReader;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.apache.parquet.io.ColumnIOFactory;
import org.apache.parquet.io.MessageColumnIO;
import org.apache.parquet.io.RecordReader;
import org.apache.parquet.schema.GroupType;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.Type;

public class ParquetImporter implements Importer {
    List<Row> rows = new ArrayList<>();
    int idx = 0;
    Format format;

    public ParquetImporter(String filePath) {
        readParquetFile(filePath);
    }

    void readParquetFile(String filePath) {
        ParquetFileReader reader;
        try {
            reader = ParquetFileReader.open(HadoopInputFile.fromPath(new Path(filePath), new Configuration()));
            MessageType schema = reader.getFooter().getFileMetaData().getSchema();
            List<Type> fields = schema.getFields();
            Format format = new Format();
            for (Type type : fields) {
                format.addColumn(type.getName());
            }
            this.format = format;
            PageReadStore pages;
            while ((pages = reader.readNextRowGroup()) != null) {
                long rowCount = pages.getRowCount();
                MessageColumnIO columnIO = new ColumnIOFactory().getColumnIO(schema);
                RecordReader<?> recordReader = columnIO.getRecordReader(pages, new GroupRecordConverter(schema));
                for (int i = 0; i < rowCount; i++) {
                    Row row = new Row(format);
                    SimpleGroup simpleGroup = (SimpleGroup) recordReader.read();
                    GroupType groupType = simpleGroup.getType();
                    for (int col = 0; col < fields.size(); col++) {
                        Type type = groupType.getType(col);
                        Object val;
                        switch (type.asPrimitiveType().getPrimitiveTypeName()) {
                        case DOUBLE:
                            val = simpleGroup.getDouble(col, 0);
                            break;
                        default:
                            val = simpleGroup.getValueToString(col, 0);
                            break;
                        }
                        row.setValue(col, val);
                    }
                    rows.add(row);
                }
            }
            reader.close();
        } catch (IllegalArgumentException | IOException e) {
            throw new TableImporterException(e);
        }
    }

    @Override
    public Format getFormat() {
        return format;
    }

    @Override
    public boolean hasNext() {
        if (idx < rows.size()) {
            return true;
        }
        return false;
    }

    @Override
    public Row next() {
        if (hasNext()) {
            return rows.get(idx++);
        }
        return null;
    }

    @Override
    public void close() throws IOException {

    }

}
