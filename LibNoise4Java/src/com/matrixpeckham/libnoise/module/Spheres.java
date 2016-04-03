/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module;

import com.matrixpeckham.libnoise.util.Globals;

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
public class Spheres extends Module {

    /**
     * default frequency for spheres
     */
    public static final double DEFAULT_SPHERES_FREQUENCY = 1;

    /**
     * frequency of the spheres
     */
    protected double frequency;

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

        double distFromCenter = Math.sqrt(x * x + y * y + z * z);
        double distFromSmallerSphere = distFromCenter - Math.floor(
                distFromCenter);
        double distFromLargerSphere = 1.0 - distFromSmallerSphere;
        double nearestDist = Globals.getMin(distFromSmallerSphere,
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

}
