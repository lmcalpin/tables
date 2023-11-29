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
import com.metatrope.tables.importer.CsvImporter;

import org.junit.Assert;
import org.junit.Test;

public class TablesImporterTest {
    @Test
    public void testImport() throws Exception {
        Format format = new Format();
        format.addColumn("tradeID", DataType.STRING);
        format.addColumn("notional", DataType.DECIMAL);

        String csv = "tradeID,notional\n" + "12345,100000.00\n" + "22345,200000.00\n";

        try (CsvImporter importer = new CsvImporter(csv)) {
            Row row = importer.next();
            Assert.assertEquals("12345", row.getData("tradeID").getObject());
            row = importer.next();
            Assert.assertEquals("22345", row.getData("tradeID").getObject());
        }
    }
}
