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

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.metatrope.tables.Etl;
import com.metatrope.tables.importer.ListImporter;
import com.metatrope.tables.model.DataType;
import com.metatrope.tables.model.Format;
import com.metatrope.tables.model.Row;

import org.junit.jupiter.api.Test;

public class CsvExporterTest {
    @Test
    public void testExport() throws Exception {
        Format format = new Format();
        format.addColumn("tradeID", DataType.STRING);
        format.addColumn("notional", DataType.DECIMAL);
        format.addColumn("ticker", DataType.STRING);

        ListImporter listImporter = new ListImporter(format);
        Row row1 = listImporter.addRow();
        row1.setValue("tradeID", "12345");
        row1.setValue("notional", "100000.00");
        row1.setValue("ticker", "AAPL");
        Row row2 = listImporter.addRow();
        row2.setValue("tradeID", "22345");
        row2.setValue("notional", "200000.00");
        row2.setValue("ticker", "CAT,JD");

        String csv = Etl.source(listImporter).sink(new CsvExporter()).asString();
        assertEquals("tradeID,notional,ticker\n" + "12345,100000.00,AAPL\n" + "22345,200000.00,\"CAT,JD\"\n", csv);
    }
}
