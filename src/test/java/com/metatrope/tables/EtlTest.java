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
package com.metatrope.tables;

import static com.metatrope.tables.Sinks.asCsv;
import static com.metatrope.tables.Sources.fromCsv;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.metatrope.tables.sinks.HtmlSink;
import com.metatrope.tables.sources.CsvSource;

import java.io.ByteArrayOutputStream;

import org.junit.jupiter.api.Test;

public class EtlTest {
    @Test
    public void testCsvToHtml() {
        String csv = "tradeID,notional\n" + "12345,100000.00\n" + "22345,200000.00\n";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Etl.source(new CsvSource(csv)).sink(new HtmlSink()).convert(baos);
        String html = baos.toString();
        String expected = "<table><th><td>tradeID</td><td>notional</td></th><tr><td>12345</td><td>100000.00</td></tr><tr><td>22345</td><td>200000.00</td></tr></table>";
        System.out.println(expected);
        System.out.println(html);
        assertEquals(expected, html);
    }

    @Test
    public void testRoundTripCsv() {
        String sourceCsv = "tradeID,notional\n" + "12345,100000.00\n" + "22345,200000.00\n";
        String outputCsv = Etl.source(fromCsv(sourceCsv)).sink(asCsv()).asString();
        assertEquals(sourceCsv, outputCsv);
    }
}
