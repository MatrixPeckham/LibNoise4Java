/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module;

/**
 * Noise module that scales the coordinates of the input value before returning
 * the output value from a source module.
 *
 * <img src="modulescalepoint.png" alt="MODULE_SCALEPOINT_IMAGE" />
 *
 *
 * The GetValue() method multiplies the ( @a x, @a y, @a z ) coordinates of the
 * input value with a scaling factor before returning the output value from the
 * source module. To set the scaling factor, call the SetScale() method. To set
 * the scaling factor to apply to the individual @a x, @a y, or @a z
 * coordinates, call the SetXScale(), SetYScale() or SetZScale() methods,
 * respectively.
 *
 * This noise module requires one source module.
 *
 * @author William Matrix Peckham
 */
public class ScalePoint extends Module {

    /**
     * default x scale
     */
    public static final double DEFAULT_SCALE_POINT_X = 1.0;

    /**
     * default Y scale
     */
    public static final double DEFAULT_SCALE_POINT_Y = 1.0;

    /**
     * default Z scale
     */
    public static final double DEFAULT_SCALE_POINT_Z = 1.0;

    protected double xScale;

    protected double yScale;

    protected double zScale;

    public ScalePoint() {
        xScale = DEFAULT_SCALE_POINT_X;
        yScale = DEFAULT_SCALE_POINT_Y;
        zScale = DEFAULT_SCALE_POINT_Z;
    }

    @Override
    public int getSourceModuleCount() {
        return 1;
    }

    @Override
    public double getValue(double x, double y, double z) {
        return sourceModule[0].getValue(x * xScale, y * yScale, z * zScale);
    }

    public void setScale(double scale) {
        xScale = yScale = zScale = scale;
    }

    public void setScale(double x, double y, double z) {
        xScale = x;
        yScale = y;
        zScale = z;
    }

    public double getxScale() {
        return xScale;
    }

    public void setxScale(double xScale) {
        this.xScale = xScale;
    }

    public double getyScale() {
        return yScale;
    }

    public void setyScale(double yScale) {
        this.yScale = yScale;
    }

    public double getzScale() {
        return zScale;
    }

    public void setzScale(double zScale) {
        this.zScale = zScale;
    }

}
