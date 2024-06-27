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
package com.metatrope.tables.sources;

import com.metatrope.tables.exception.TableImporterException;
import com.metatrope.tables.model.Format;
import com.metatrope.tables.model.Row;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcSource implements Source {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcSource.class);

    private Format format;
    private PreparedStatement ps;
    private ResultSet rs;
    private boolean hasNext;

    public JdbcSource(Connection conn, String tableName) {
        format = getFormat(conn, tableName);
        try {
            ps = conn.prepareStatement("SELECT * FROM " + tableName);
            rs = ps.executeQuery();
            hasNext = rs.next();
        } catch (SQLException e) {
            throw new TableImporterException(e);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            if (!rs.isClosed())
                rs.close();
            if (!ps.isClosed())
                ps.close();
        } catch (SQLException e) {
            throw new TableImporterException(e);
        }
    }

    @Override
    public Format getFormat() {
        return format;
    }

    private Format getFormat(Connection conn, String tableName) {
        try {
            Format format = new Format();
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();
            for (int i = 1; i <= colCount; i++) {
                String colName = rsmd.getColumnName(i);
                int colType = rsmd.getColumnType(i);
                SqlDataType sdt = SqlDataType.fromColumnType(colType);
                LOGGER.trace(colName + "=>" + colType + " (" + sdt.name() + ")");
                format.addColumn(colName, sdt.toDataType());
            }
            return format;
        } catch (SQLException e) {
            throw new TableImporterException(e);
        }
    }

    @Override
    public Row next() {
        try {
            if (!hasNext)
                return null;
            int colCount = format.getNumberOfColumns();
            Row datarow = new Row(format);
            for (int i = 1; i <= colCount; i++) {
                String colName = format.findColumn(i - 1).getID();
                Object obj = rs.getObject(i);
                datarow.setValue(colName, obj);
            }
            hasNext = rs.next();
            return datarow;
        } catch (SQLException e) {
            throw new TableImporterException(e);
        }
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }
}
