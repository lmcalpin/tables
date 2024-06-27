/*******************************************************************************
 * Copyright (c) 2024 Lawrence McAlpin
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Lawrence McAlpin - initial implementation
 *******************************************************************************/
package com.metatrope.tables.exporter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.metatrope.tables.Etl;
import com.metatrope.tables.importer.ListImporter;
import com.metatrope.tables.importer.ParquetImporter;
import com.metatrope.tables.model.DataType;
import com.metatrope.tables.model.Format;
import com.metatrope.tables.model.Row;

import java.io.File;
import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;

// to work on Windows, you must set the HADOOP_HOME directory to the right location
public class ParquetExporterTest {
    @Test
    public void testExport() {
        Format format = new Format();
        format.addColumn("tradeID", DataType.STRING);
        format.addColumn("notional", DataType.DECIMAL);
        format.addColumn("ticker", DataType.STRING);

        ListImporter listImporter = new ListImporter(format);
        Row row1 = listImporter.addRow();
        row1.setValue("tradeID", "12345");
        row1.setValue("notional", 100000.00);
        row1.setValue("ticker", "AAPL");
        Row row2 = listImporter.addRow();
        row2.setValue("tradeID", "22345");
        row2.setValue("notional", 200000.00);
        row2.setValue("ticker", "CAT,JD");

        String fileName = UUID.randomUUID().toString() + ".parquet";
        try {
            Etl.source(listImporter).sink(new ParquetExporter()).toFile(fileName);
            ParquetImporter importer = new ParquetImporter(fileName);
            Row irow1 = importer.next();
            assertEquals(row1.get("tradeID"), irow1.get("tradeID"));
            BigDecimal expected = new BigDecimal(row1.get("notional").toString());
            BigDecimal actual = new BigDecimal(irow1.get("notional").toString());
            assertTrue(expected.compareTo(actual) == 0);
        } finally {
            // cleanup
            File file = new File(fileName);
            file.delete();
        }
    }
    
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
    "type" : "int"
  } ]
}""";
        assertEquals(expected, avroSchema);
    }
}
