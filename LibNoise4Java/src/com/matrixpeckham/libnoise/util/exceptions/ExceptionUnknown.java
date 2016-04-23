/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
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
