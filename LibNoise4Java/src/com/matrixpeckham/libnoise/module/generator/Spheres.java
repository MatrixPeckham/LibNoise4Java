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
 * Noise module that outputs concentric spheres.
 *
 * <img src="modulespheres.png" alt="MODULE_SPHERE_IMAGE" />
 *
 * This noise module outputs concentric spheres centered on the origin like the
 * concentric rings of an onion.
 *
 * The first sphere has a radius of 1.0. Each subsequent sphere has a radius
 * that is 1.0 unit larger than the previous sphere.
 *
 * The output value from this noise module is determined by the distance between
 * the input value and the the nearest spherical surface. The input values that
 * are located on a spherical surface are given the output value 1.0 and the
 * input values that are equidistant from two spherical surfaces are given the
 * output value -1.0.
 *
 * An application can change the frequency of the concentric spheres. Increasing
 * the frequency reduces the distances between spheres. To specify the
 * frequency, call the SetFrequency() method.
 *
 * This noise module, modified with some low-frequency, low-power turbulence, is
 * useful for generating agate-like textures.
 *
 * This noise module does not require any source modules.
 *
 * @author William Matrix Peckham
 */
public class Spheres extends AbstractModule {

    /**
     * default frequency for spheres
     */
    public static final double DEFAULT_SPHERES_FREQUENCY = 1;

    /**
     * frequency of the spheres
     */
    protected double frequency;

    /**
     *
     */
    public Spheres() {
        frequency = DEFAULT_SPHERES_FREQUENCY;
    }

    /**
     * returns the frequency
     *
     * @return
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
        y *= frequency;
        z *= frequency;

        double distFromCenter = sqrt(x * x + y * y + z * z);
        double distFromSmallerSphere = distFromCenter - floor(
                distFromCenter);
        double distFromLargerSphere = 1.0 - distFromSmallerSphere;
        double nearestDist = getMin(distFromSmallerSphere,
                distFromLargerSphere);
        return 1.0 - (nearestDist * 4.0);//re-normalize to -1-1
    }

    /**
     * Sets the frequenct of the concentric spheres.
     *
     * @param frequency The frequency of the concentric spheres.
     *
     * Increasing the frequency increases the density of the concentric spheres,
     * reducing the distances between them.
     */
    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    private static final Logger LOG = Logger.getLogger(Spheres.class.getName());

}
