/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module.generator;

import static com.matrixpeckham.libnoise.util.Globals.getMin;
import static java.lang.Math.floor;
import static java.lang.Math.sqrt;

import com.matrixpeckham.libnoise.module.AbstractModule;
import com.matrixpeckham.libnoise.module.NoiseSample;
import java.util.logging.Logger;

/**
 * Noise module that outputs concentric spheres.
 * <p>
 * <img src="modulespheres.png" alt="MODULE_SPHERE_IMAGE" />
 * <p>
 * This noise module outputs concentric spheres centered on the origin like the
 * concentric rings of an onion.
 * <p>
 * The first sphere has a radius of 1.0. Each subsequent sphere has a radius
 * that is 1.0 unit larger than the previous sphere.
 * <p>
 * The output value from this noise module is determined by the distance between
 * the input value and the the nearest spherical surface. The input values that
 * are located on a spherical surface are given the output value 1.0 and the
 * input values that are equidistant from two spherical surfaces are given the
 * output value -1.0.
 * <p>
 * An application can change the frequency of the concentric spheres. Increasing
 * the frequency reduces the distances between spheres. To specify the
 * frequency, call the SetFrequency() method.
 * <p>
 * This noise module, modified with some low-frequency, low-power turbulence, is
 * useful for generating agate-like textures.
 * <p>
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
    public NoiseSample getNoise(double x, double y, double z, double w, double u,
            double v) {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;
        u *= frequency;
        v *= frequency;

        double distFromCenter = sqrt(x * x + y * y + z * z + w * w + u * u + v
                * v);
        double distFromSmallerSphere = distFromCenter - floor(
                distFromCenter);
        double distFromLargerSphere = 1.0 - distFromSmallerSphere;
        double nearestDist = getMin(distFromSmallerSphere,
                distFromLargerSphere);
        NoiseSample s = new NoiseSample();
        s.value = 1.0 - (nearestDist * 4.0);//re-normalize to -1-1
        return s;
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
