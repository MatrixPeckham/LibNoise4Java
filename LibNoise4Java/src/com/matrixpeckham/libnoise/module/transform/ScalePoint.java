/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module.transform;

import com.matrixpeckham.libnoise.module.AbstractModule;
import java.util.logging.Logger;

/**
 * Noise module that scales the coordinates of the input value before returning
 * the output value from a source module.
 *
 * @image html modulescalepoint.png
 *
 * The GetValue() method multiplies the ( @a x, @a y, @a z ) coordinates of the
 * input value with a scaling factor before returning the output value from the
 * source module. To set the scaling factor, call the SetScale() method. To set
 * the scaling factor to apply to the individual @a x, @a y, or @a z
 * coordinates, call the SetXScale(), SetYScale() or SetZScale() methods,
 * respectively.
 *
 * This noise module requires one source module.
 */
public class ScalePoint extends AbstractModule {

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

    /**
     * Default Constructor.
     */
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

    /**
     * Getter.
     *
     * @return
     */
    public double getxScale() {
	return xScale;
    }

    /**
     * Getter.
     *
     * @return
     */
    public double getyScale() {
	return yScale;
    }

    /**
     * Getter.
     *
     * @return
     */
    public double getzScale() {
	return zScale;
    }

    /**
     * Uniform Scale setter.
     *
     * @param scale
     */
    public void setScale(double scale) {
	xScale = yScale = zScale = scale;
    }

    /**
     * Multiple Setter
     *
     * @param x
     * @param y
     * @param z
     */
    public void setScale(double x, double y, double z) {
	xScale = x;
	yScale = y;
	zScale = z;
    }

    /**
     * Setter.
     *
     * @param xScale
     */
    public void setxScale(double xScale) {
	this.xScale = xScale;
    }

    /**
     * Setter.
     *
     * @param yScale
     */
    public void setyScale(double yScale) {
	this.yScale = yScale;
    }

    /**
     * Setter.
     *
     * @param zScale
     */
    public void setzScale(double zScale) {
	this.zScale = zScale;
    }

    private static final Logger LOG
	    = Logger.getLogger(ScalePoint.class.getName());

}
