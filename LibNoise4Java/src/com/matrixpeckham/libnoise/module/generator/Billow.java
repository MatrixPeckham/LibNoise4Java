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
import static com.matrixpeckham.libnoise.util.Globals.gradientCoherentNoise3D;
import static com.matrixpeckham.libnoise.util.Globals.makeIntRange;
import com.matrixpeckham.libnoise.util.NoiseQuality;
import static com.matrixpeckham.libnoise.util.NoiseQuality.STD;
import static java.lang.Math.abs;
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
    public double getValue(double x, double y, double z) {
        double value = 0;
        double curPersistence = 1;
        double nx;
        double ny;
        double nz;
        int localseed;
        x *= frequency;
        y *= frequency;
        z *= frequency;
        for (int curOctave = 0; curOctave < octaveCount;
                curOctave++) {
            //make sure that these floating point values have the same
            //range as an integer so we can pass them to the noise functions.
            nx = makeIntRange(x);
            ny = makeIntRange(y);
            nz = makeIntRange(z);
            //get the coherent noise value from the input value
            //and add it to the final result
            localseed = (seed + curOctave) & 0XFFFF_FFFF;
            double signal
                    = gradientCoherentNoise3D(nx, ny, nz, localseed,
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
        if (octaveCount < 1 || octaveCount > BILLOW_MAX_OCTAVE) {
            throw new IllegalArgumentException("Octave count is too high or low");
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

    private static final Logger LOG = Logger.getLogger(Billow.class.getName());

}
