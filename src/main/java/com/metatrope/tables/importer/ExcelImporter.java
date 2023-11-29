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
package com.metatrope.tables.importer;

import com.metatrope.tables.Column;
import com.metatrope.tables.DataType;
import com.metatrope.tables.Format;
import com.metatrope.tables.Row;
import com.metatrope.tables.exception.TableImporterException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import com.google.common.base.Strings;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

// use HSSF to load an Excel spreadsheet into our Tables data structure
public class ExcelImporter implements Importer {
    private HSSFWorkbook wb;
    private HSSFSheet sheet;
    private int rowNumber = 0;
    private Format format;
    private String[] colNames;

    public ExcelImporter(byte[] data) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            wb = new HSSFWorkbook(bis);
            sheet = wb.getSheetAt(0);
            format = extractHeader();
        } catch (IOException e) {
            throw new TableImporterException(e);
        }
    }

    @Override
    public void close() throws IOException {

    }

    private Format extractHeader() {
        try {
            Format format = new Format();
            rowNumber = sheet.getFirstRowNum();
            colNames = new String[sheet.getLastRowNum() + 1];
            for (; rowNumber <= sheet.getLastRowNum(); rowNumber++) {
                HSSFRow row = sheet.getRow(rowNumber);
                boolean colFnd = false;
                for (short j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                    HSSFCell cell = row.getCell(j);
                    String header = cell.getStringCellValue();
                    if (!Strings.isNullOrEmpty(header)) {
                        colFnd = true;
                        colNames[j] = header;
                        format.addColumn(header, DataType.UNKNOWN);
                    }
                }
                if (colFnd) {
                    rowNumber++;
                    break;
                }
            }
            return format;
        } catch (Throwable t) {
            throw new TableImporterException(t);
        }
    }

    private String getDoubleAsString(Double val) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("####.########");
        String strDoubleValue = df.format(val);
        return strDoubleValue;
    }

    @Override
    public Format getFormat() {
        return format;
    }

    @Override
    public Row next() {
        HSSFRow row = sheet.getRow(rowNumber++);
        if (row == null)
            return null;
        Row dataRow = new Row(format);
        for (short j = 0; j < colNames.length; j++) {
            if (colNames[j] != null) {
                HSSFCell cell = row.getCell(j);
                String colName = colNames[j];
                Column col = format.findColumn(colName);
                if (col == null) {
                    throw new TableImporterException("Column not in file format: " + colName);
                }
                if (cell == null) {
                    continue;
                }
                if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                    String val = cell.getStringCellValue();
                    dataRow.setValue(col, val);
                } else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        Date val = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
                        dataRow.setValue(col, val);
                    } else {
                        Double val = cell.getNumericCellValue();
                        // if the column is numeric but our data format insists the data should be a string,
                        // convert it to a string this often happens to numeric trade IDs -- we
                        // want to treat trade IDs as strings since they are identifiers and you don't
                        // manipulate them mathematically, but they can often be numbers.
                        if (col.getDataType() == DataType.STRING) {
                            String strDoubleValue = getDoubleAsString(val);
                            dataRow.setValue(col, strDoubleValue);
                        } else {
                            String strDoubleValue = getDoubleAsString(val);
                            dataRow.setValue(col, new BigDecimal(strDoubleValue));
                        }
                    }
                }
            }
        }
        return dataRow;
    }
}
