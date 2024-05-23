package com.metatrope.tables.importer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.metatrope.tables.model.Row;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ParquetImporterTest {
    @Test
    public void testParquetImporter() throws IllegalArgumentException, IOException {
        URL url = ParquetImporterTest.class.getResource("/test.parquet");
        ParquetImporter importer = new ParquetImporter(url.toString());
        int count = 0;
        List<Row> rows = new ArrayList<>();
        Row row;
        while ((row = importer.next()) != null) {
            rows.add(row);
            System.out.println(row);
            count++;
        }
        assertEquals(500, count);
    }
}
