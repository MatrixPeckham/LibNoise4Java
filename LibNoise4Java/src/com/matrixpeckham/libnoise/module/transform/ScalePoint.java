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
