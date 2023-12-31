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
import com.metatrope.tables.Format;
import com.metatrope.tables.Row;
import com.metatrope.tables.Tables;
import com.metatrope.tables.importer.ListImporter;

import org.junit.Assert;
import org.junit.Test;

public class TablesExporterTest {
    @Test
    public void testExport() throws Exception {
        Format format = new Format();
        format.addColumn("tradeID", DataType.STRING);
        format.addColumn("notional", DataType.DECIMAL);

        ListImporter listImporter = new ListImporter(format);
        Row row1 = listImporter.addRow();
        row1.setValue("tradeID", "12345");
        row1.setValue("notional", "100000.00");
        Row row2 = listImporter.addRow();
        row2.setValue("tradeID", "22345");
        row2.setValue("notional", "200000.00");

        String csv = Tables.<String> source(listImporter).sink(new CsvExporter());
        Assert.assertEquals("tradeID,notional\n" + "12345,100000.00\n" + "22345,200000.00\n", csv);
    }
}
