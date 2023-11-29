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

import com.metatrope.tables.DataType;
import com.metatrope.tables.Row;
import com.metatrope.tables.Value;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultExcelColorizer implements ExcelColorizer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExcelColorizer.class);

    protected HSSFFont boldFont, normalFont, redFont;
    protected HSSFCellStyle headerStyle;
    private HSSFWorkbook wb;
    protected Map<String, HSSFCellStyle> CELL_STYLE = new HashMap<String, HSSFCellStyle>();

    protected Map<String, Short> DATA_FORMAT = new HashMap<String, Short>();
    private int rowNumber = 1;

    private void colorHeader(HSSFWorkbook wb, HSSFSheet sheet) {
        if (headerStyle == null) {
            headerStyle = wb.createCellStyle();
            headerStyle.setFont(boldFont);
            headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        }

        HSSFRow row = sheet.getRow(0);
        Iterator<?> it = row.cellIterator();
        while (it.hasNext()) {
            HSSFCell cell = (HSSFCell) it.next();
            cell.setCellStyle(headerStyle);
        }
    }

    @Override
    public void colorize(HSSFRow hssfrow, Row row) {
        List<Value> values = row.getDataInDefaultOrder();
        for (short i = 0; i < values.size(); i++) {
            HSSFCell cell = hssfrow.getCell(i);
            Value value = values.get(i);
            HSSFCellStyle style = findOrCreateCellStyle(wb, value);
            cell.setCellStyle(style);
            LOGGER.debug("Set cell style [" + rowNumber + "], [" + i + "]: " + style.getIndex());
        }
        rowNumber++;
    }

    @Override
    public void colorize(HSSFWorkbook wb) {
        this.wb = wb;
        boldFont = wb.createFont();
        boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        normalFont = wb.createFont();
        normalFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        redFont = wb.createFont();
        redFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        redFont.setColor(HSSFFont.COLOR_RED);

        HSSFSheet sheet = wb.getSheetAt(0);
        colorHeader(wb, sheet);
    }

    // Excel interprets java date formats differently -- normally we would use
    // m/d/yy (or d/m/yy for UK format)
    private String convertJavaDateFormat(String in) {
        if (in.equalsIgnoreCase("dd/mm/yy"))
            return "d/m/yy";
        if (in.equalsIgnoreCase("dd/mm/yyyy"))
            return "d/m/yy";
        if (in.equalsIgnoreCase("mm/dd/yy"))
            return "m/d/yy";
        if (in.equalsIgnoreCase("mm/dd/yyyy"))
            return "m/d/yy";
        return in;
    }

    protected HSSFCellStyle createCellStyle(HSSFWorkbook wb, Value v, String hint, String metadata) {
        HSSFCellStyle style;
        style = wb.createCellStyle();
        if (v.getColumn().getDataType() == DataType.DATE) {
            if (!Strings.isNullOrEmpty(metadata)) {
                short idx = findOrCreateDataFormat(wb, metadata);
                style.setDataFormat(idx);
            }
        } else if (v.getColumn().getDataType() == DataType.STRING) {
            style.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        }
        style.setFont(getFont(hint));
        return style;
    }

    protected HSSFCellStyle findOrCreateCellStyle(HSSFWorkbook wb, Value v) {
        String dataType = v.getColumn().getDataType().name();
        String metadata = v.getColumn().getMetadata();
        String key = dataType + "." + metadata;
        HSSFCellStyle style = CELL_STYLE.get(key);
        if (style == null) {
            LOGGER.info("Created cell style for " + key);
            style = createCellStyle(wb, v, key, metadata);
            CELL_STYLE.put(key, style);
        }
        return style;
    }

    private short findOrCreateDataFormat(HSSFWorkbook wb, String metadata) {
        Short s = DATA_FORMAT.get(metadata);
        if (s != null) {
            return s.shortValue();
        }
        HSSFDataFormat df = wb.createDataFormat();
        short idx = df.getFormat(convertJavaDateFormat(metadata));
        DATA_FORMAT.put(metadata, idx);
        return idx;
    }

    protected HSSFFont getFont(String hint) {
        if (Strings.isNullOrEmpty(hint))
            return normalFont;
        if (hint.equalsIgnoreCase("Y"))
            return redFont;
        return normalFont;
    }
}
