/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module;

import static com.matrixpeckham.libnoise.util.Globals.gradientCoherentNoise3D;
import static com.matrixpeckham.libnoise.util.Globals.makeIntRange;
import com.matrixpeckham.libnoise.util.NoiseQuality;
import static com.matrixpeckham.libnoise.util.NoiseQuality.STD;


public class Perlin extends Module {

    /**
     * default frequency for perlin noise
     */
    public static final double DEFAULT_PERLIN_FREQUENCY = 1.0;


    /**
     * default number of octaves for perlin noise
     */
    public static final int DEFAULT_PERLIN_OCTAVE_COUNT = 6;

    /**
     * default persistence value for perlin noise
     */
    public static final double DEFAULT_PERLIN_PERSISTENCE = 0.5;

    /**
     * default perlin noise quality
     */
    public static final NoiseQuality DEFAULT_PERLIN_QUALITY = STD;

    /**
     * default noise seed for perlin noise
     */
    public static final int DEFAULT_PERLIN_SEED = 0;

    /**
     * default lacunrity for perlin noise
     */
    public static final double DEFFAULT_PERLIN_LACUNARITY = 2.0;

    /**
     * maximum number of octaves for perlin
     */
    public static final int PERLIN_MAX_OCTAVE = 30;

    /**
     * frequency of the first octave
     */
    protected double frequency;

    /**
     * frequency multiplier between successive octaves
     */
    protected double lacunarity;

    /**
     * quality of the noise
     */
    protected NoiseQuality noiseQuality;

    /**
     * Total number of octaves that generate perlin noise
     */
    protected int octaveCount;

    /**
     * persistence of the noise
     */
    protected double persistence;

    /**
     * seed value used by perlin noise
     */
    int seed;

    public Perlin() {
        frequency = DEFAULT_PERLIN_FREQUENCY;
        lacunarity = DEFFAULT_PERLIN_LACUNARITY;
        noiseQuality = DEFAULT_PERLIN_QUALITY;
        octaveCount = DEFAULT_PERLIN_OCTAVE_COUNT;
        persistence = DEFAULT_PERLIN_PERSISTENCE;
        seed = DEFAULT_PERLIN_SEED;
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
     * returns the lacunarity of the noise
     *
     * @return
     */
    public double getLacunarity() {
        return lacunarity;
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
     * Gets the number of octaves that generate the noise
     *
     * @return
     */
    public int getOctaveCount() {
        return octaveCount;
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
     * gets the seed for the noise
     *
     * @return
     */
    public int getSeed() {
        return seed;
    }

    @Override
    public int getSourceModuleCount() {
        return 0;
    }

    @Override
    public double getValue(double x, double y, double z) {
        double value = 0;
        double signal = 0;
        double curPersistence = 1;
        double nx, ny, nz;
        int localSeed;
        x *= frequency;
        y *= frequency;
        z *= frequency;
        for (int curOctave = 0; curOctave < octaveCount;
                curOctave++) {
            //make sure that these floating point values have the same range as
            //an integer so that we can pass them to the coherent noise function
            nx = makeIntRange(x);
            ny = makeIntRange(y);
            nz = makeIntRange(z);
            //get the coherent noise value from the input value and add it to the
            //final result
            localSeed = (seed + curOctave) & 0XFFFF_FFFF;
            signal
                    = gradientCoherentNoise3D(nx, ny, nz, localSeed,
                            noiseQuality);
            value += signal * curPersistence;
            //prepare next octave
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            curPersistence *= persistence;
        }
        return value;
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
     * sets the quality of the noise
     *
     * @param noiseQuality
     */
    public void setNoiseQuality(NoiseQuality noiseQuality) {
        this.noiseQuality = noiseQuality;
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
        if (octaveCount < 1 || octaveCount > PERLIN_MAX_OCTAVE) {
            throw new IllegalArgumentException("octave count is too high or low");
        }
        this.octaveCount = octaveCount;
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
     * sets the seed for the noise.
     *
     * @param seed
     */
    public void setSeed(int seed) {
        this.seed = seed;
    }
}
