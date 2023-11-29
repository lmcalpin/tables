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
package com.metatrope.tables.exception;

public class TableImporterException extends RuntimeException {
    private static final long serialVersionUID = 4927851855374991213L;

    public TableImporterException(String message) {
        super(message);
    }

    public TableImporterException(String message, Throwable cause) {
        super(message, cause);
    }

    public TableImporterException(Throwable cause) {
        super(cause);
    }

}
