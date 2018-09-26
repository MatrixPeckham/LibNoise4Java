/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module.generator;

import static com.matrixpeckham.libnoise.util.Globals.gradientCoherentNoise6D;
import static com.matrixpeckham.libnoise.util.Globals.makeIntRange;
import static com.matrixpeckham.libnoise.util.NoiseQuality.STD;
import static java.lang.Math.abs;

import com.matrixpeckham.libnoise.module.AbstractModule;
import com.matrixpeckham.libnoise.module.NoiseSample;
import com.matrixpeckham.libnoise.util.NoiseQuality;
import java.util.logging.Logger;

/**
 * Noise module that outputs three-dimensional "billowy" noise.
 *
 * @image html modulebillow.png
 *
 * This noise module generates "billowy" noise suitable for clouds and rocks.
 *
 * This noise module is nearly identical to noise::module::Perlin except this
 * noise module modifies each octave with an absolute-value function. See the
 * documentation of noise::module::Perlin for more information.
 */
public class Billow extends AbstractModule {

    /**
     * Maximum number of octaves for the billow noise.
     */
    public static final int BILLOW_MAX_OCTAVE = 30;

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

    /**
     * Default Constructor
     */
    public Billow() {
        frequency = DEFAULT_BILLOW_FREQUENCY;
        lacunarity = DEFAULT_BILLOW_LACUNARITY;
        noiseQuality = DEFAULT_BILLOW_QUALITY;
        octaveCount = DEFAULT_BILLOW_OCTAV_COUNT;
        persistence = DEFAULT_BILLOW_PERSISTENCE;
        seed = DEFAULT_BILLOW_SEED;
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
    public NoiseSample getNoise(double x, double y, double z, double w, double u,
            double v) {
        double value = 0;
        double curPersistence = 1;
        double nx;
        double ny;
        double nz;
        double nw;
        double nu;
        double nv;
        int localseed;
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;
        u *= frequency;
        v *= frequency;
        for (int curOctave = 0; curOctave < octaveCount;
                curOctave++) {
            //make sure that these floating point values have the same
            //range as an integer so we can pass them to the noise functions.
            nx = makeIntRange(x);
            ny = makeIntRange(y);
            nz = makeIntRange(z);
            nw = makeIntRange(w);
            nu = makeIntRange(u);
            nv = makeIntRange(v);
            //get the coherent noise value from the input value
            //and add it to the final result
            localseed = (seed + curOctave) & 0XFFFF_FFFF;
            double signal
                    = gradientCoherentNoise6D(nx, ny, nz, nw, nu, nv, localseed,
                            noiseQuality);
            signal = 2.0 * abs(signal) - 1.0;
            value += signal * curPersistence;
            //prepare the next octave
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            curPersistence *= persistence;
        }
        value += 0.5;
        NoiseSample s = new NoiseSample();
        s.value = value;
        return s;
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
     * <p>
     * The lacunarity is the frequency multiplier between successive octaves
     * <p>
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
     * <p>
     * The parameter should be between 1 and BILLOW_MAX_OCTAVE
     * <p>
     * The number of octaves controls the amount of detail in the billow noise
     * <p>
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
     * Sets the persistence value of the noise.
     * <p>
     * the persistence value controls the roughness of the noise
     * <p>
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

    private static final Logger LOG = Logger.getLogger(Billow.class.getName());

}
