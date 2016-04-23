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
import static java.lang.Math.abs;
import static java.lang.Math.pow;


public class RidgedMulti extends Module {

    /**
     * default frequency for the ridgedmulti noise
     */
    public static final double DEFAULT_RIDGED_FREQUENCY = 1.0;

    /**
     * default gain
     */
    public static final double DEFAULT_RIDGED_GAIN = 2.0;

    /**
     * default lacunarity for the noise
     */
    public static final double DEFAULT_RIDGED_LACUNARITY = 2.0;

    /**
     * default number of octaves for the noise
     */
    public static final int DEFAULT_RIDGED_OCTAVE_COUNT = 6;


    /**
     * default offset
     */
    public static final double DEFAULT_RIDGED_OFFSET = 1.0;

    /**
     * default quality for the noise
     */
    public static final NoiseQuality DEFAULT_RIDGED_QUALITY = STD;

    /**
     * default seed for the noise
     */
    public static final int DEFAULT_RIDGED_SEED = 0;

    /**
     * default exponent parameter
     */
    public static final double DEFUALT_RIDGED_EXPONENT = 1.0;

    /**
     * maximum number of octaves for the nosie
     */
    public static final int RIDGED_MAX_OCTAVE = 30;

    /**
     * exponent parameter for generating weights
     */
    protected double exponent;

    /**
     * frequency of the first octave
     */
    protected double frequency;


    /**
     * gain value
     */
    protected double gain;

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
     * offset value
     */
    protected double offset;


    /**
     * seed value used by perlin noise
     */
    int seed;

    /**
     * contains the spectral weights for each octave
     */
    double[] spectralWeights;

    /**
     * constructor
     */
    public RidgedMulti() {
        frequency = DEFAULT_RIDGED_FREQUENCY;
        lacunarity = DEFAULT_RIDGED_LACUNARITY;
        noiseQuality = DEFAULT_RIDGED_QUALITY;
        octaveCount = DEFAULT_RIDGED_OCTAVE_COUNT;
        seed = DEFAULT_RIDGED_SEED;
        exponent = DEFUALT_RIDGED_EXPONENT;
        spectralWeights = new double[RIDGED_MAX_OCTAVE];
        calcSpectralWeights();
    }

    /**
     * Calculates the spectral weights for each octave.
     *
     * This method is called when the lacunarity changes.
     */
    protected void calcSpectralWeights() {
        //exposed parameter to user
        double h = exponent;
        double localFrequency = 1.0;
        for (int i = 0; i < RIDGED_MAX_OCTAVE;
                i++) {
            //compute weight for each frequency
            spectralWeights[i] = pow(localFrequency, -h);
            frequency *= lacunarity;
        }
    }

    /**
     * returns the exponent parameter for calculating the spectral weights
     *
     * @return
     */    public double getExponent() {
         return exponent;
     }

    /**
     * frequency of the first octave
     *
     * @return
     */
    public double getFrequency() {
        return frequency;
    }

    public double getGain() {
        return gain;
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

    public double getOffset() {
        return offset;
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

    //multifractal code originally written by F. Kenton "Doc Mojo" Musgrave. 1998. Modified by jas for use with libnoise

    @Override
    public double getValue(double x, double y, double z) {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        double signal = 0;
        double value = 0;
        double weight = 1;
        //we've exposed offset and gain
        for (int curOctave = 0; curOctave < octaveCount;
                curOctave++) {
            //make sure these floating
            double nx = makeIntRange(x);
            double ny = makeIntRange(y);
            double nz = makeIntRange(z);
            //get the coherent noise value
            int localSeed = (seed + curOctave) & 0X7FFF_FFFF;
            signal
                    = gradientCoherentNoise3D(nx, ny, nz, localSeed,
                            noiseQuality);
            //make the ridges.
            signal = abs(signal);
            signal = offset - signal;
            //square the signal to increase sharpness of ridge
            signal *= signal;
            //the weighting from the previous octave is applied to signal.
            //larger values hav higher weights producing sharp oints along the
            //ridges
            signal *= weight;
            //weight successive contributions by the previous signal
            weight = signal * gain;
            if (weight > 1) {
                weight = 1;
            }
            if (weight < 0) {
                weight = 0;
            }
            //add the signal to the output value
            value += (signal * spectralWeights[curOctave]);
            //next octave
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
        }
        return (value * 1.25) - 1.0;
    }

    /**
     * sets the exponent parameter for calculating the spectral weights
     *
     * @param exponent
     */
    public void setExponent(double exponent) {
        this.exponent = exponent;
    }

    /**
     * sets the frequency of the first octave
     *
     * @param frequency
     */
    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public void setGain(double gain) {
        this.gain = gain;
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
        calcSpectralWeights();
    }

    /**
     * sets the quality of the noise
     *
     * @param noiseQuality
     */
    public void setNoiseQuality(
            NoiseQuality noiseQuality) {
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
        if (octaveCount < 1 || octaveCount > RIDGED_MAX_OCTAVE) {
            throw new IllegalArgumentException("octave count is too high or low");
        }
        this.octaveCount = octaveCount;
    }

    public void setOffset(double offset) {
        this.offset = offset;
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
