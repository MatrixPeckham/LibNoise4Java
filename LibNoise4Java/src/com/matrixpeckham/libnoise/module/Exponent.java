/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module;

import static java.lang.Math.abs;
import static java.lang.Math.pow;


public class Exponent extends Module {

    /**
     * default exponent for the exponent noise module
     */
    public static final double DEFAULT_EXPONENT = 1.0;

    /**
     * exponent to apply to the output value from the source noise.
     */
    protected double exponent;

    /**
     * constructor
     */
    public Exponent() {
        exponent = DEFAULT_EXPONENT;
    }

    /**
     * Returns the exponent value to apply to the output value from the source
     * module.
     *
     * @returns The exponent value.
     *
     * Because most noise modules will output values that range from -1.0 to
     * +1.0, this noise module first normalizes this output value (the range
     * becomes 0.0 to 1.0), maps that value onto an exponential curve, then
     * rescales that value back to the original range.
     */
    public double getExponent() {
        return exponent;
    }

    @Override
    public int getSourceModuleCount() {
        return 1;
    }

    @Override
    public double getValue(double x, double y, double z) {
        double value = sourceModule[0].getValue(x, y, z);
        return pow(abs((value + 1.0) / 2.0), exponent) * 2.0 - 1.0;
    }

    /**
     * Sets the exponent value to apply to the output value from the source
     * module.
     *
     * @param exponent The exponent value.
     *
     * Because most noise modules will output values that range from -1.0 to
     * +1.0, this noise module first normalizes this output value (the range
     * becomes 0.0 to 1.0), maps that value onto an exponential curve, then
     * rescales that value back to the original range.
     */
    public void setExponent(double exponent) {
        this.exponent = exponent;
    }

}
