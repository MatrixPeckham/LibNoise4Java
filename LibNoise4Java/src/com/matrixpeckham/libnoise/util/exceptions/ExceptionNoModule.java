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
 * No module exception
 *
 * Could not retrieve a source module from a noise module.
 *
 */
public class ExceptionNoModule extends NoiseException {

    private static final Logger LOG = getLogger(ExceptionNoModule.class.
	    getName());

    private static final long serialVersionUID = 1L;

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
