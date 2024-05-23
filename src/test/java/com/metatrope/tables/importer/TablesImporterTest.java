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

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.metatrope.tables.model.DataType;
import com.metatrope.tables.model.Format;
import com.metatrope.tables.model.Row;

import org.junit.jupiter.api.Test;

public class TablesImporterTest {
    @Test
    public void testImport() throws Exception {
        Format format = new Format();
        format.addColumn("tradeID", DataType.STRING);
        format.addColumn("notional", DataType.DECIMAL);

        String csv = "tradeID,notional\n" + "12345,100000.00\n" + "22345,200000.00\n";

        try (CsvImporter importer = new CsvImporter(csv)) {
            Row row = importer.next();
            assertEquals("12345", row.getData("tradeID").getObject());
            row = importer.next();
            assertEquals("22345", row.getData("tradeID").getObject());
        }
    }
}
