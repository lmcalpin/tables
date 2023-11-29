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

import com.metatrope.tables.DataType;

public enum SqlDataType {
    BIT(-7),
    TINYINT(-6),
    BIGINT(-5),
    LONGVARBINARY(-4),
    VARBINARY(-3),
    BINARY(-2),
    LONGVARCHAR(-1),
    NULL(0),
    CHAR(1),
    NUMERIC(2),
    DECIMAL(3),
    INTEGER(4),
    SMALLINT(5),
    FLOAT(6),
    REAL(7),
    DOUBLE(8),
    VARCHAR(12),
    DATE(91),
    TIME(92),
    TIMESTAMP(93),
    OTHER(1111);

    private int type;

    private SqlDataType(int type) {
        this.type = type;
    }

    public static SqlDataType fromColumnType(int val) {
        for (SqlDataType dataType : values()) {
            if (dataType.type == val)
                return dataType;
        }
        return null;
    }

    public DataType toDataType() {
        switch (this) {
        case BIT:
        case TINYINT:
        case BIGINT:
        case LONGVARBINARY:
        case VARBINARY:
        case BINARY:
        case NUMERIC:
        case INTEGER:
        case SMALLINT:
        case FLOAT:
        case REAL:
        case DOUBLE:
            return DataType.INTEGER;
        case VARCHAR:
        case LONGVARCHAR:
            return DataType.STRING;
        case DECIMAL:
            return DataType.DECIMAL;
        case DATE:
        case TIME:
        case TIMESTAMP:
            return DataType.DATE;
        default:
            break;
        }
        return null;
    }
}
