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
 * Noise module that outputs concentric cylinders.
 *
 * <img src="modulecylinders.png" alt="MODULE_CYLINDER_IMAGE" />
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
 *
 * @author William Matrix Peckham
 */
public class Cylinders extends Module {

    /**
     * Default frequency value for cylinders module
     */
    public static final double DEFAULT_CYLINDERS_FREQUENCY = 1.0;

    /**
     * frequency
     *
     * @return
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
     * @returns The frequency of the concentric cylinders.
     *
     * Increasing the frequency increases the density of the concentric
     * cylinders, reducing the distances between them.
     */
    public double getFrequency() {
        return frequency;
    }

    /**
     * Sets the frequenct of the concentric cylinders.
     *
     * @param frequency The frequency of the concentric cylinders.
     *
     * Increasing the frequency increases the density of the concentric
     * cylinders, reducing the distances between them.
     */
    public void setFrequency(double freq) {
        frequency = freq;
    }

    @Override
    public int getSourceModuleCount() {
        return 0;
    }

    @Override
    public double getValue(double x, double y, double z) {
        x *= frequency;
        z *= frequency;

        double distFromCenter = Math.sqrt(x * x + z * z);
        double distFromSmallSphere = distFromCenter - Math.floor(distFromCenter);
        double distFromLargerSphere = 1.0 - distFromSmallSphere;
        double nearestDist = Globals.getMin(distFromSmallSphere,
                distFromLargerSphere);
        return 1.0 - (nearestDist * 4.0);//map to -1-1.
    }

}
