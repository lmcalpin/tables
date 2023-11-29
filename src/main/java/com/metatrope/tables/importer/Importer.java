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

import com.metatrope.tables.Format;
import com.metatrope.tables.Row;

import java.io.Closeable;

public interface Importer extends Closeable {
    public Format getFormat();

    public Row next();
}
