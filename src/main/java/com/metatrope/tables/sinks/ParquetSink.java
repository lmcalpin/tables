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
package com.metatrope.tables.sinks;

import com.metatrope.tables.exception.TableExporterException;
import com.metatrope.tables.model.Column;
import com.metatrope.tables.model.Format;
import com.metatrope.tables.model.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;

// [WIP] TODO: the Parquet writer writes to a temp file; when the Exporter is completed, 
// it copies the file to the output stream.  This isn't great, but it works for now.
public class ParquetSink implements Sink {
    private Schema schema;
    private ParquetWriter<GenericData.Record> writer;
    private String tempFileName;
    private OutputStream os;

    public ParquetSink() {
    }

    @Override
    public void close() throws Exception {
    }

    @Override
    public void onCompleted() {
        try {
            writer.close();

            FileInputStream fis = new FileInputStream(tempFileName);
            byte[] buf = new byte[8192];
            int length;
            while ((length = fis.read(buf)) != -1) {
                os.write(buf, 0, length);
            }
            os.close();
            fis.close();

            File file = new File(tempFileName);
            boolean deleted = file.delete();
            System.out.println(tempFileName + " is deleted? " + deleted);
        } catch (IOException e) {
            throw new TableExporterException(e);
        }
    }

    @Override
    public void setOutputStream(OutputStream os) {
        this.os = os;
    }

    @Override
    public void onNext(Row row) {
        try {
            GenericData.Record rec = new GenericData.Record(schema);
            for (Column c : row.getColumnsInDefaultOrder()) {
                rec.put(c.getID(), row.getDataAsObject(c.getID()));
            }
            writer.write(rec);
        } catch (IOException e) {
            throw new TableExporterException(e);
        }
    }

    @Override
    public void onStart(Format format) {
        try {
            this.schema = createSchema(format);
            String tmpDir = System.getProperty("java.io.tmpdir");
            this.tempFileName = tmpDir + "/" + UUID.randomUUID().toString() + "_tables.parquet";
            this.writer = AvroParquetWriter.<GenericData.Record> builder(new Path(tempFileName)).withConf(new Configuration()).withSchema(schema).withCompressionCodec(CompressionCodecName.SNAPPY).build();
        } catch (IOException e) {
            throw new TableExporterException(e);
        }
    }

    // create avro schema
    static Schema createSchema(Format format) {
        StringBuilder fieldListBuilder = new StringBuilder();
        for (Column column : format.getColumns()) {
            String avroDataType;
            switch (column.getDataType()) {
            case STRING:
                avroDataType = "string";
                break;
            case DATE:
                avroDataType = "int";
                break;
            case DECIMAL:
                avroDataType = "double";
                break;
            case INTEGER:
                avroDataType = "int";
                break;
            default:
                avroDataType = "string";
                break;
            }
            ;
            fieldListBuilder.append(String.format("""
                    { "name": "%s",
                      "type": "%s" },""", column.getID(), avroDataType));
        }
        fieldListBuilder.deleteCharAt(fieldListBuilder.length() - 1);
        String fieldList = fieldListBuilder.toString();
        String schemaJson = String.format("""
                { "namespace": "com.metatrope.tables",
                  "type": "record",
                  "name": "table",
                  "fields": [
                      %s
                   ]
                }""", fieldList);

        Schema.Parser parser = new Schema.Parser().setValidate(true);
        return parser.parse(schemaJson);
    }
}
