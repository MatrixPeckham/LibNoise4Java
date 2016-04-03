/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.util.exceptions;

/**
 * No module exception
 *
 * Could not retrieve a source module from a noise module.
 *
 * @note If one or more required source modules were not connected to a specific
 * noise module, and its GetValue() method was called, that method will raise a
 * debug assertion instead of this exception. This is done for performance
 * reasons.
 * @author William Matrix Peckham
 */
public class ExceptionNoModule extends NoiseException {

    public ExceptionNoModule() {
    }

    public ExceptionNoModule(String message) {
        super(message);
    }

    public ExceptionNoModule(String message, Throwable cause) {
        super(message, cause);
    }

    public ExceptionNoModule(Throwable cause) {
        super(cause);
    }

    public ExceptionNoModule(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
