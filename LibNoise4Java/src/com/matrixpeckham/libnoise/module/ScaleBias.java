/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module;


public class ScaleBias extends Module {

    /**
     * default bias
     */
    public static final double DEFAULT_BIAS = 0.0;

    /**
     * default scale
     */
    public static final double DEFAULT_SCALE = 1.0;

    /**
     * bias
     */
    protected double bias;

    /**
     * scale
     */
    protected double scale;

    public ScaleBias() {
        scale = DEFAULT_SCALE;
        bias = DEFAULT_BIAS;
    }

    /**
     * Returns the bias to apply to the scaled output value from the source
     * module.
     *
     * @returns The bias to apply.
     *
     * The GetValue() method retrieves the output value from the source module,
     * multiplies it with the scaling factor, adds the bias to it, then outputs
     * the value.
     */
    public double getBias() {
        return bias;
    }

    /**
     * Returns the scaling factor to apply to the output value from the source
     * module.
     *
     * @returns The scaling factor to apply.
     *
     * The GetValue() method retrieves the output value from the source module,
     * multiplies it with the scaling factor, adds the bias to it, then outputs
     * the value.
     */
    public double getScale() {
        return scale;
    }

    @Override
    public int getSourceModuleCount() {
        return 1;
    }


    @Override
    public double getValue(double x, double y, double z) {
        return sourceModule[0].getValue(x, y, z) * scale + bias;
    }

    /**
     * Sets the bias to apply to the scaled output value from the source module.
     *
     * @param bias The bias to apply.
     *
     * The GetValue() method retrieves the output value from the source module,
     * multiplies it with the scaling factor, adds the bias to it, then outputs
     * the value.
     */
    public void setBias(double bias) {
        this.bias = bias;
    }

    /**
     * Sets the scaling factor to apply to the output value from the source
     * module.
     *
     * @param scale The scaling factor to apply.
     *
     * The GetValue() method retrieves the output value from the source module,
     * multiplies it with the scaling factor, adds the bias to it, then outputs
     * the value.
     */
    public void setScale(double scale) {
        this.scale = scale;
    }

}
