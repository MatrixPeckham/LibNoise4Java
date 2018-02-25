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

package com.matrixpeckham.libnoise.module.generator;

import com.matrixpeckham.libnoise.module.AbstractModule;
import static com.matrixpeckham.libnoise.util.Globals.getMin;
import static java.lang.Math.floor;
import static java.lang.Math.sqrt;
import java.util.logging.Logger;

/**
 * Noise module that outputs concentric cylinders.
 *
 * @image html modulecylinders.png
 *
 * This noise module outputs concentric cylinders centered on the origin. These
 * cylinders are oriented along the @a y axis similar to the concentric rings of
 * a tree. Each cylinder extends infinitely along the @a y axis.
 *
 * The first cylinder has a radius of 1.0. Each subsequent cylinder has a radius
 * that is 1.0 unit larger than the previous cylinder.
 *
 * The output value from this noise module is determined by the distance between
 * the input value and the the nearest cylinder surface. The input values that
 * are located on a cylinder surface are given the output value 1.0 and the
 * input values that are equidistant from two cylinder surfaces are given the
 * output value -1.0.
 *
 * An application can change the frequency of the concentric cylinders.
 * Increasing the frequency reduces the distances between cylinders. To specify
 * the frequency, call the SetFrequency() method.
 *
 * This noise module, modified with some low-frequency, low-power turbulence, is
 * useful for generating wood-like textures.
 *
 * This noise module does not require any source modules.
 */
public class Cylinders extends AbstractModule {

    /**
     * Default frequency value for cylinders module
     */
    public static final double DEFAULT_CYLINDERS_FREQUENCY = 1.0;

    /**
     * frequency
     *
     */
    protected double frequency;

    /**
     * Constructor
     */
    public Cylinders() {
        frequency = DEFAULT_CYLINDERS_FREQUENCY;
    }

    /**
     * Returns the frequency of the concentric cylinders.
     *
     * @return The frequency of the concentric cylinders.
     *
     * Increasing the frequency increases the density of the concentric
     * cylinders, reducing the distances between them.
     */
    public double getFrequency() {
        return frequency;
    }

    @Override
    public int getSourceModuleCount() {
        return 0;
    }

    @Override
    public double getValue(double x, double y, double z) {
        x *= frequency;
        z *= frequency;
        double distFromCenter = sqrt(x * x + z * z);
        double distFromSmallSphere = distFromCenter - floor(distFromCenter);
        double distFromLargerSphere = 1.0 - distFromSmallSphere;
        double nearestDist = getMin(distFromSmallSphere, distFromLargerSphere);
        return 1.0 - (nearestDist * 4.0);//map to -1-1.

    }

    /**
     * Sets the frequenct of the concentric cylinders.
     *
     * @param freq The frequency of the concentric cylinders.
     *
     * Increasing the frequency increases the density of the concentric
     * cylinders, reducing the distances between them.
     */
    public void setFrequency(double freq) {
        frequency = freq;
    }

    private static final Logger LOG
            = Logger.getLogger(Cylinders.class.getName());

}
