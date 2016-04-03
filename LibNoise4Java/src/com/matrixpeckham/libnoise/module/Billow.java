/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module;

import com.matrixpeckham.libnoise.util.Globals;
import com.matrixpeckham.libnoise.util.NoiseQuality;
import static com.matrixpeckham.libnoise.util.NoiseQuality.STD;

/**
 * Noise module that outputs three-dimensional "billowy" noise.
 *
 * <img src="modulebillow.png" alt="MODULE_BILLOW_IMAGE" />
 *
 * This noise module generates "billowy" noise suitable for clouds and rocks.
 *
 * this noise module is nearly identical to Perlin except this module modifies
 * each octave with an absolute-value function. See the documentation of Perlin
 * for more information.
 *
 * @author William Matrix Peckham
 */
public class Billow extends Module {

    /**
     * Default frequency for the billow noise
     */
    public static final double DEFAULT_BILLOW_FREQUENCY = 1;

    /**
     * Default lacunarity for the billow noise.
     */
    public static final double DEFAULT_BILLOW_LACUNARITY = 2;

    /**
     * Default number of octaves for the billow noise
     */
    public static final int DEFAULT_BILLOW_OCTAV_COUNT = 6;

    /**
     * Default persistence value for the billow noise.
     */
    public static final double DEFAULT_BILLOW_PERSISTENCE = 0.5;

    /**
     * Default noise quality for billow noise
     */
    public static final NoiseQuality DEFAULT_BILLOW_QUALITY = STD;

    /**
     * Default noise seed for the billow noise.
     */
    public static final int DEFAULT_BILLOW_SEED = 0;

    /**
     * Maximum number of octaves for the billow noise.
     */
    public static final int BILLOW_MAX_OCTAVE = 30;

    /**
     * Frequency of the first octave
     */
    protected double frequency;

    /**
     * Frequency multiplier between successive octaves
     */
    protected double lacunarity;

    /**
     * Quality of the noise
     */
    protected NoiseQuality noiseQuality;

    /**
     * Total number of octaves that generate the noise
     */
    protected int octaveCount;

    /**
     * Persistence value of the noise.
     */
    protected double persistence;

    /**
     * seed value used by the noise function
     */
    protected int seed;

    public Billow() {
        frequency = DEFAULT_BILLOW_FREQUENCY;
        lacunarity = DEFAULT_BILLOW_LACUNARITY;
        noiseQuality = DEFAULT_BILLOW_QUALITY;
        octaveCount = DEFAULT_BILLOW_OCTAV_COUNT;
        persistence = DEFAULT_BILLOW_PERSISTENCE;
        seed = DEFAULT_BILLOW_SEED;
    }

    @Override
    public double getValue(double x, double y, double z) {
        double value = 0;
        double signal = 0;
        double curPersistence = 1;
        double nx;
        double ny;
        double nz;
        int localseed;

        x *= frequency;
        y *= frequency;
        z *= frequency;

        for (int curOctave = 0; curOctave < octaveCount; curOctave++) {
            //make sure that these floating point values have the same
            //range as an integer so we can pass them to the noise functions.
            nx = Globals.makeIntRange(x);
            ny = Globals.makeIntRange(y);
            nz = Globals.makeIntRange(z);

            //get the coherent noise value from the input value
            //and add it to the final result
            localseed = (seed + curOctave) & 0XFFFFFFFF;
            signal = Globals.gradientCoherentNoise3D(nx, ny, nz, localseed,
                    noiseQuality);
            signal = 2.0 * Math.abs(signal) - 1.0;
            value += signal * curPersistence;

            //prepare the next octave
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            curPersistence *= persistence;
        }
        value += 0.5;

        return value;
    }

    @Override
    public int getSourceModuleCount() {
        return 0;
    }

    /**
     * frequency of the first octave
     *
     * @return
     */
    public double getFrequency() {
        return frequency;
    }

    /**
     * sets the frequency of the first octave
     *
     * @param frequency
     */
    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    /**
     * returns the lacunarity of the noise
     *
     * @return
     */
    public double getLacunarity() {
        return lacunarity;
    }

    /**
     * sets the lacunarity of the noise
     *
     * The lacunarity is the frequency multiplier between successive octaves
     *
     * for best results use a number between 1.5-3.5
     *
     * @param lacunarity
     */
    public void setLacunarity(double lacunarity) {
        this.lacunarity = lacunarity;
    }

    /**
     * returns the quality of the noise.
     *
     * @return
     */
    public NoiseQuality getNoiseQuality() {
        return noiseQuality;
    }

    /**
     * sets the quality of the noise
     *
     * @param noiseQuality
     */
    public void setNoiseQuality(NoiseQuality noiseQuality) {
        this.noiseQuality = noiseQuality;
    }

    /**
     * Gets the number of octaves that generate the noise
     *
     * @return
     */
    public int getOctaveCount() {
        return octaveCount;
    }

    /**
     * Sets the number of octaves that generate the noise
     *
     * The parameter should be between 1 and BILLOW_MAX_OCTAVE
     *
     * The number of octaves controls the amount of detail in the billow noise
     *
     * the larger the number the more time required to calculate the noise value
     *
     * @param octaveCount
     */
    public void setOctaveCount(int octaveCount) {
        if (octaveCount < 1 || octaveCount > BILLOW_MAX_OCTAVE) {
            throw new IllegalArgumentException("Octave count is too high or low");
        }
        this.octaveCount = octaveCount;
    }

    /**
     * Gets the persistence value of the noise
     *
     * @return
     */
    public double getPersistence() {
        return persistence;
    }

    /**
     * Sets the persistence value of the noise.
     *
     * the persistence value controls the roughness of the noise
     *
     * for best results the persistence value should be between 0-1.
     *
     * @param persistence
     */
    public void setPersistence(double persistence) {
        this.persistence = persistence;
    }

    /**
     * gets the seed for the noise
     *
     * @return
     */
    public int getSeed() {
        return seed;
    }

    /**
     * sets the seed for the noise.
     *
     * @param seed
     */
    public void setSeed(int seed) {
        this.seed = seed;
    }

}
