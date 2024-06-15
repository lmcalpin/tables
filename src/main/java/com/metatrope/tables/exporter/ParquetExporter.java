package com.metatrope.tables.exporter;

import com.metatrope.tables.exception.TableExporterException;
import com.metatrope.tables.model.Column;
import com.metatrope.tables.model.Format;
import com.metatrope.tables.model.Row;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.parquet.hadoop.ParquetWriter;

public class ParquetExporter implements Exporter {
    private Schema schema;
    private ParquetWriter<GenericData.Record> writer;

    public ParquetExporter() {
    }

    @Override
    public void close() throws Exception {
        writer.close();
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void setOutputStream(OutputStream os) {
        try {
            File.createTempFile(UUID.randomUUID().toString(), "tables");
        } catch (IOException e) {
            throw new TableExporterException(e);
        }
    }

    @Override
    public void onNext(Row row) {
        try {
            GenericData.Record rec = new GenericData.Record(schema);
            writer.write(rec);
        } catch (IOException e) {
            throw new TableExporterException(e);
        }
    }

    @Override
    public void onStart(Format format) {
        this.schema = createSchema(format);
    }

    // create avro schema
    static Schema createSchema(Format format) {
        StringBuilder fieldListBuilder = new StringBuilder();
        for (Column column : format.getColumns()) {
            fieldListBuilder.append(String.format("""
                        { "name": "%s",
                          "type": "%s" },""", column.getID(), "string"));
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
