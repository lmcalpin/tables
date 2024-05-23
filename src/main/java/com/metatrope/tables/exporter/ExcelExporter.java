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
package com.metatrope.tables.exporter;

import com.metatrope.tables.exception.TableExporterException;
import com.metatrope.tables.model.Column;
import com.metatrope.tables.model.Format;
import com.metatrope.tables.model.Row;
import com.metatrope.tables.model.Value;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelExporter implements Exporter {
    private HSSFWorkbook wb;
    private ExcelColorizer colorizer;
    private String sectionName;
    private Format format;
    private int rowNumber = 1;
    private HSSFSheet sheet;
    private OutputStream os;

    public ExcelExporter(String sectionName) {
        this.sectionName = sectionName;
    }

    public ExcelExporter(String sectionName, ExcelColorizer colorizer) {
        this.sectionName = sectionName;
        this.colorizer = colorizer;
    }

    private void addHeader(HSSFSheet sheet, List<Column> list) {
        HSSFRow row = sheet.createRow(0);
        for (short i = 0; i < list.size(); i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(list.get(i).getID());
        }
    }

    @Override
    public void close() throws Exception {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                throw new TableExporterException(e);
            }
        }
    }

    private void createWorksheet() {
        wb = new HSSFWorkbook();
        sheet = wb.createSheet(sectionName);
        addHeader(sheet, format.getColumns());
    }

    public ExcelColorizer getColorizer() {
        return colorizer;
    }

    private byte[] getExcelReport(HSSFWorkbook wb) {
        try {
            ByteArrayOutputStream bost = new ByteArrayOutputStream();
            wb.write(bost);
            byte[] wbBytes = bost.toByteArray();
            bost.close();
            return wbBytes;
        } catch (Exception e) {
            throw new TableExporterException("Could not export table", e);
        }
    }

    private HSSFWorkbook getWorksheet() {
        if (wb == null) {
            createWorksheet();
        }

        return wb;
    }

    @Override
    public void onCompleted() {
        byte[] bytes = getExcelReport(wb);
        if (os != null) {
            try {
                os.write(bytes);
            } catch (IOException e) {
                throw new TableExporterException(e);
            }
        }
    }

    @Override
    public void onNext(Row row) {
        HSSFRow hssfrow = sheet.createRow(rowNumber);
        List<Value> values = row.getDataInDefaultOrder();
        for (short i = 0; i < values.size(); i++) {
            Value value = values.get(i);
            HSSFCell cell = hssfrow.createCell(i);
            setCellValue(cell, value);
        }
        rowNumber++;

        if (colorizer != null) {
            colorizer.colorize(hssfrow, row);
        }
    }

    @Override
    public void onStart(Format format) {
        this.format = format;
        createWorksheet();
        if (colorizer != null) {
            colorizer.colorize(wb);
        }
    }

    private void setCellValue(HSSFCell cell, Value value) {
        if (value == null)
            return;
        Object obj = value.getObject();
        if (obj == null)
            return;
        if (obj instanceof String) {
            String s = (String) obj;
            cell.setCellValue(s);
            return;
        }
        if (obj instanceof Date) {
            Date s = (Date) obj;
            cell.setCellValue(s);
            return;
        }
        if (obj instanceof Double) {
            Double s = (Double) obj;
            cell.setCellValue(s);
            return;
        }
        if (obj instanceof BigDecimal) {
            BigDecimal s = (BigDecimal) obj;
            cell.setCellValue(s.doubleValue());
            return;
        }
        if (obj instanceof Integer) {
            Integer s = (Integer) obj;
            cell.setCellValue(s);
            return;
        }
        throw new TableExporterException("Could not export cell with type: " + obj.getClass().getSimpleName());
    }

    public void setColorizer(ExcelColorizer colorizer) {
        this.colorizer = colorizer;
    }

    @Override
    public void setOutputStream(OutputStream os) {
        this.os = os;
    }
}
