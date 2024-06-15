package com.metatrope.tables.exporter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.metatrope.tables.model.DataType;
import com.metatrope.tables.model.Format;

import org.junit.jupiter.api.Test;

public class ParquetExporterTest {
    @Test
    public void testAvroSchema() {
        Format format = new Format();
        format.addColumn("name", DataType.STRING);
        format.addColumn("val", DataType.INTEGER);
        String avroSchema = ParquetExporter.createSchema(format).toString(true);
        avroSchema = avroSchema.replaceAll("\\r", ""); // normalize to \n as line end terminator
        String expected = """
{
  "type" : "record",
  "name" : "table",
  "namespace" : "com.metatrope.tables",
  "fields" : [ {
    "name" : "name",
    "type" : "string"
  }, {
    "name" : "val",
    "type" : "string"
  } ]
}""";
        assertEquals(expected, avroSchema);
    }
}
