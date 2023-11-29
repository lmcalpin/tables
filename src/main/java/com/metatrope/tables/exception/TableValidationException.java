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

public class TableValidationException extends RuntimeException {
    private static final long serialVersionUID = 4927851855374991213L;

    public TableValidationException(String message) {
        super(message);
    }

    public TableValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TableValidationException(Throwable cause) {
        super(cause);
    }

}
