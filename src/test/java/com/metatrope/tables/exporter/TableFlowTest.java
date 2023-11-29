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

import com.metatrope.tables.Tables;
import com.metatrope.tables.importer.CsvImporter;

import org.junit.Assert;
import org.junit.Test;

public class TableFlowTest {
    @Test
    public void testFlow() {
        String csv = "tradeID,notional\n" + "12345,100000.00\n" + "22345,200000.00\n";
        String html = Tables.<String> source(new CsvImporter(csv)).sink(new HtmlExporter());
        Assert.assertEquals("<table><th><td>tradeID</td><td>notional</td></th><tr><td>12345</td><td>100000.00</td></tr><tr><td>22345</td><td>200000.00</td></tr></table>", html);
    }
}
