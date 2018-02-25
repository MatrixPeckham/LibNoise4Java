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

package com.matrixpeckham.libnoise.module.modifier;

import com.matrixpeckham.libnoise.module.AbstractModule;
import java.util.logging.Logger;

/**
 * Noise module that applies a scaling factor and a bias to the output value
 * from a source module.
 *
 * @image html modulescalebias.png
 *
 * The GetValue() method retrieves the output value from the source module,
 * multiplies it with a scaling factor, adds a bias to it, then outputs the
 * value.
 *
 * This noise module requires one source module.
 */
public class ScaleBias extends AbstractModule {

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
     * @return The bias to apply.
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
     * @return The scaling factor to apply.
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

    private static final Logger LOG
	    = Logger.getLogger(ScaleBias.class.getName());

}
