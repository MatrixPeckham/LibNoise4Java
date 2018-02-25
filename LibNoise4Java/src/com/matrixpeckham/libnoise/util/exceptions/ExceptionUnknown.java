/*
 * Copyright © 2017-2018 William Peckham
 *
 * This file is part of LibNoise4Java.
 * 
 * LibNoise4Java is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * LibNoise4Java is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with LibNoise4Java.  If not, see <http://www.gnu.org/licenses/>. 
 */

package com.matrixpeckham.libnoise.util.exceptions;

import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

/**
 * Unknown exception was raised
 *
 * @author William Matrix Peckham
 */
public class ExceptionUnknown extends NoiseException {

    private static final Logger LOG
            = getLogger(ExceptionUnknown.class.getName());

    private static final long serialVersionUID = 1L;

    public ExceptionUnknown() {
    }

    public ExceptionUnknown(String message) {
        super(message);
    }

    public ExceptionUnknown(String message, Throwable cause) {
        super(message, cause);
    }

    public ExceptionUnknown(Throwable cause) {
        super(cause);
    }

    public ExceptionUnknown(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
