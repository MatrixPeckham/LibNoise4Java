/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module;

import static com.matrixpeckham.libnoise.module.Perlin.DEFAULT_PERLIN_FREQUENCY;
import static com.matrixpeckham.libnoise.module.Perlin.DEFAULT_PERLIN_SEED;


public class Turbulence extends Module {

    /**
     * default frequency for turbulence
     */
    public static final double DEFAULT_TURBULENCE_FREQUENCY
            = DEFAULT_PERLIN_FREQUENCY;

    /**
     * Default power for turbulence
     */
    public static final double DEFAULT_TURBULENCE_POWER = 1.0;

    /**
     * Default roughness for turbulence
     */
    public static final int DEFAULT_TURBULENCE_ROUGHNESS = 3;

    /**
     * default seed for the turbulence
     */
    public static final int DEFAULT_TURBULENCE_SEED = DEFAULT_PERLIN_SEED;

    /**
     * power of the displacement
     */
    protected double power;

    /**
     * noise for x displacement
     */
    protected Perlin xDistortionModule;

    /**
     * noise for y displacement
     */
    protected Perlin yDistortionModule;

    /**
     * noise for z displacement
     */
    protected Perlin zDistortionModule;

    /**
     * constructor
     */
    public Turbulence() {
        xDistortionModule = new Perlin();
        yDistortionModule = new Perlin();
        zDistortionModule = new Perlin();
        power = DEFAULT_TURBULENCE_POWER;
        setSeed(DEFAULT_TURBULENCE_SEED);
        setFrequency(DEFAULT_TURBULENCE_FREQUENCY);
        setRoughness(DEFAULT_TURBULENCE_ROUGHNESS);
    }

    /**
     * returns frequency
     *
     * @return the frequcny of the turbulence determines how rapidly the
     * displacement amount changes
     */
    public double getFrequency() {
        return xDistortionModule.getFrequency();
    }

    /**
     * returns the power
     *
     * @return scaling factor applied to the displacement amount
     */
    public double getPower() {
        return power;
    }

    /**
     * returns the seed value
     *
     * @return
     */
    public int getSeed() {
        return xDistortionModule.getSeed();
    }

    @Override
    public int getSourceModuleCount() {
        return 1;
    }

    /**
     * returns the roughness of the turbulence
     *
     * @return
     *
     * the roughness of the turbulence determines the roughness of the changes
     * to the displacement amount. low values smoothly change the displacement
     * amount high values roughly change the displacement amount which produces
     * "kinky" changes
     */
    public int getTurbulence() {
        return xDistortionModule.getOctaveCount();
    }

    @Override
    public double getValue(double x, double y, double z) {
        // Get the values from the three noise::module::Perlin noise modules and
        // add each value to each coordinate of the input value.  There are also
        // some offsets added to the coordinates of the input values.  This prevents
        // the distortion modules from returning zero if the (x, y, z) coordinates,
        // when multiplied by the frequency, are near an integer boundary.  This is
        // due to a property of gradient coherent noise, which returns zero at
        // integer boundaries.
        double x0, y0, z0;
        double x1, y1, z1;
        double x2, y2, z2;
        x0 = x + (12414.0 / 65536.0);
        y0 = y + (65124.0 / 65536.0);
        z0 = z + (31337.0 / 65536.0);
        x1 = x + (26519.0 / 65536.0);
        y1 = y + (18128.0 / 65536.0);
        z1 = z + (60493.0 / 65536.0);
        x2 = x + (53820.0 / 65536.0);
        y2 = y + (11213.0 / 65536.0);
        z2 = z + (44845.0 / 65536.0);
        double xDistort = x + (xDistortionModule.getValue(x0, y0, z0)
                * power);
        double yDistort = y + (yDistortionModule.getValue(x1, y1, z1)
                * power);
        double zDistort = z + (zDistortionModule.getValue(x2, y2, z2)
                * power);
        
        // Retrieve the output value at the offsetted input value instead of the
        // original input value.
        return sourceModule[0].getValue(xDistort, yDistort, zDistort);
    }

    /**
     * sets the frequency of the turbulence.
     *
     * @param frequency
     *
     * the frequency of the turbulence determines how rapidly the displacement
     * amount changes.
     */
    public void setFrequency(double frequency) {
        xDistortionModule.setFrequency(frequency);
        yDistortionModule.setFrequency(frequency);
        zDistortionModule.setFrequency(frequency);
    }

    /**
     * sets the power of the turbulence
     *
     * @param power
     *
     * power of the turbulence determines the scaling factor that applied to the
     * displacement amount
     */
    public void setPower(double power) {
        this.power = power;
    }

    /**
     * Sets the roughness of the turbulence.
     *
     * @param roughness The roughness of the turbulence.
     *
     * The roughness of the turbulence determines the roughness of the changes
     * to the displacement amount. Low values smoothly change the displacement
     * amount. High values roughly change the displacement amount, which
     * produces more "kinky" changes.
     *
     * Internally, there are three noise::module::Perlin noise modules that
     * displace the input value; one for the @a x, one for the @a y, and one for
     * the @a z coordinate. The roughness value is equal to the number of
     * octaves used by the noise::module::Perlin noise modules.
     */
    public void setRoughness(int roughness) {
        //sets the octave count for the roughness
        xDistortionModule.setOctaveCount(roughness);
        yDistortionModule.setOctaveCount(roughness);
        zDistortionModule.setOctaveCount(roughness);
    }

    /**
     * Sets the seed value of the internal noise modules that are used to
     * displace the input values.
     *
     * @param seed The seed value.
     *
     * Internally, there are three noise::module::Perlin noise modules that
     * displace the input value; one for the @a x, one for the @a y, and one for
     * the @a z coordinate. This noise module assigns the following seed values
     * to the noise::module::Perlin noise modules:
     *
     * - It assigns the seed value (@a seed + 0) to the @a x noise module.
     *
     * - It assigns the seed value (@a seed + 1) to the @a y noise module.
     *
     * - It assigns the seed value (@a seed + 2) to the @a z noise module.
     */
    public void setSeed(int seed) {
        //set the seed of each nosie value. to prevent
        //artifacts we use a different seed for each noise
        xDistortionModule.setSeed(seed);
        yDistortionModule.setSeed(seed + 1);
        zDistortionModule.setSeed(seed + 2);
    }

}
