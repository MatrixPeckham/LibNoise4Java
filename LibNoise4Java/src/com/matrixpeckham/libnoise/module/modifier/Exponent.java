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
import static java.lang.Math.abs;
import static java.lang.Math.pow;
import java.util.logging.Logger;

/**
 * Noise module that maps the output value from a source module onto an
 * exponential curve.
 *
 * @image html moduleexponent.png
 *
 * Because most noise modules will output values that range from -1.0 to +1.0,
 * this noise module first normalizes this output value (the range becomes 0.0
 * to 1.0), maps that value onto an exponential curve, then rescales that value
 * back to the original range.
 *
 * This noise module requires one source module.
 */
public class Exponent extends AbstractModule {

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
     * @return The exponent value.
     *
     * Because most noise modules will output values that range from -1.0 to
     * +1.0, this noise module first normalizes this output value (the range
     * becomes 0.0 to 1.0), maps that value onto an exponential curve, then
     * re-scales that value back to the original range.
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
     * re-scales that value back to the original range.
     */
    public void setExponent(double exponent) {
	this.exponent = exponent;
    }

    private static final Logger LOG = Logger.getLogger(Exponent.class.getName());

}
