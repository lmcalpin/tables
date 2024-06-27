package com.metatrope.tables;

import com.metatrope.tables.exception.TableImporterException;
import com.metatrope.tables.sinks.CsvSink;
import com.metatrope.tables.sinks.ExcelSink;
import com.metatrope.tables.sinks.HtmlSink;
import com.metatrope.tables.sinks.ParquetSink;
import com.metatrope.tables.sinks.Sink;
import com.metatrope.tables.sinks.XmlSink;
import com.metatrope.util.FileUtils;

import java.io.File;

public class Sinks {
    public static CsvSink asCsv() {
        return new CsvSink();
    }

    public static HtmlSink asHtml() {
        return new HtmlSink();
    }

    public static Sink fromFileExtension(File file) {
        String extension = FileUtils.getExtension(file);
        switch (extension) {
        case "csv":
            return new CsvSink();
        case "html":
            return new HtmlSink();
        case "xml":
            return new XmlSink();
        case "parquet":
            return new ParquetSink();
        case "xsl":
            return new ExcelSink("table");
        default:
            throw new TableImporterException("Unsupported extension: " + extension);
        }
    }
}