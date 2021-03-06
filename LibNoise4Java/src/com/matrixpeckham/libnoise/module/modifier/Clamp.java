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
 * Noise module that clamps the output value from a source module to a range of
 * values.
 *
 * @image html moduleclamp.png
 *
 * The range of values in which to clamp the output value is called the
 * <i>clamping range</i>.
 *
 * If the output value from the source module is less than the lower bound of
 * the clamping range, this noise module clamps that value to the lower bound.
 * If the output value from the source module is greater than the upper bound of
 * the clamping range, this noise module clamps that value to the upper bound.
 *
 * To specify the upper and lower bounds of the clamping range, call the
 * SetBounds() method.
 *
 * This noise module requires one source module.
 */
public class Clamp extends AbstractModule {

    /**
     * Default lower bound of the clamping range for clamping module.
     */
    public final double DEFAULT_CLAMP_LOWER_BOUND = -1.0;

    /**
     * Default upper bound of the clamping range for clamping module.
     */
    public final double DEFAULT_CLAMP_UPPER_BOUND = 1.0;

    /**
     * Lower bound of the clamping range
     */
    protected double lowerBound;

    /**
     * Upper bound of the clamping range.
     */
    protected double upperBound;

    /**
     * Constructor.
     *
     * The default lower bound of the clamping range is set to
     * noise::module::DEFAULT_CLAMP_LOWER_BOUND.
     *
     * The default upper bound of the clamping range is set to
     * noise::module::DEFAULT_CLAMP_UPPER_BOUND.
     */
    public Clamp() {
	lowerBound = DEFAULT_CLAMP_LOWER_BOUND;
	upperBound = DEFAULT_CLAMP_UPPER_BOUND;
    }

    /**
     * Returns the lower bound of the clamping range.
     *
     * @return The lower bound.
     *
     * If the output value from the source module is less than the lower bound
     * of the clamping range, this noise module clamps that value to the lower
     * bound.
     */
    public double getLowerBound() {
	return lowerBound;
    }

    @Override
    public int getSourceModuleCount() {
	return 1;
    }

    /**
     * Returns the upper bound of the clamping range.
     *
     * @return The upper bound.
     *
     * If the output value from the source module is greater than the upper
     * bound of the clamping range, this noise module clamps that value to the
     * upper bound.
     */
    public double getUpperBound() {
	return upperBound;
    }

    @Override
    public double getValue(double x, double y, double z) {
	double value = sourceModule[0].getValue(x, y, z);
	if (value < lowerBound) {
	    return lowerBound;
	} else if (value > upperBound) {
	    return upperBound;
	} else {
	    return value;
	}
    }

    /**
     * Sets the lower and upper bounds of the clamping range.
     *
     * @param lower The lower bound.
     * @param upper The upper bound.
     *
     * @noise.pre The lower bound must be less than or equal to the upper bound.
     *
     *
     * If the output value from the source module is less than the lower bound
     * of the clamping range, this noise module clamps that value to the lower
     * bound. If the output value from the source module is greater than the
     * upper bound of the clamping range, this noise module clamps that value to
     * the upper bound.
     */
    public void setBounds(double lower, double upper) {
	lowerBound = lower;
	upperBound = upper;
    }

    private static final Logger LOG = Logger.getLogger(Clamp.class.getName());

}
