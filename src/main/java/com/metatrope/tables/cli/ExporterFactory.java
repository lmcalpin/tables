package com.metatrope.tables.cli;

import com.metatrope.tables.exception.TableImporterException;
import com.metatrope.tables.exporter.CsvExporter;
import com.metatrope.tables.exporter.ExcelExporter;
import com.metatrope.tables.exporter.Exporter;
import com.metatrope.tables.exporter.HtmlExporter;
import com.metatrope.tables.exporter.XmlExporter;
import com.metatrope.util.FileUtils;

import java.io.File;

public class ExporterFactory {
    public static Exporter fromFileExtension(File file) {
        String extension = FileUtils.getExtension(file);
        switch (extension) {
        case "csv":
            return new CsvExporter();
        case "html":
            return new HtmlExporter();
        case "xml":
            return new XmlExporter();
        case "xsl":
            return new ExcelExporter("table");
        default:
            throw new TableImporterException("Unsupported extension: " + extension);
        }
    }

}
