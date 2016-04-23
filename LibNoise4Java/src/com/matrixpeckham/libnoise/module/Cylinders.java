/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module;

import static com.matrixpeckham.libnoise.util.Globals.getMin;
import static java.lang.Math.floor;
import static java.lang.Math.sqrt;


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
     * @param frequency The frequency of the concentric cylinders.
     *
     * Increasing the frequency increases the density of the concentric
     * cylinders, reducing the distances between them.
     */
    public void setFrequency(double freq) {
        frequency = freq;
    }

}
