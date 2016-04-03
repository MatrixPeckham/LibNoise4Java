/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.util.exceptions;

/**
 * Base class for exceptions generated by LibNoise4Java
 *
 * @author William Matrix Peckham
 */
public class NoiseException extends RuntimeException {

    public NoiseException() {
    }

    public NoiseException(String message) {
        super(message);
    }

    public NoiseException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoiseException(Throwable cause) {
        super(cause);
    }

    public NoiseException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
