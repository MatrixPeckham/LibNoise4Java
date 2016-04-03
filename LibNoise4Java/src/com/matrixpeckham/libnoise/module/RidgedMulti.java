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

/**
 * Noise module that outputs 3-dimensional ridged-multifractal noise.
 *
 * <img src="moduleridgedmulti.png" alt="MODULE_RIDGEDMULTI_IMAGE" />
 *
 * This noise module, heavily based on the Perlin-noise module, generates
 * ridged-multifractal noise. Ridged-multifractal noise is generated in much of
 * the same way as Perlin noise, except the output of each octave is modified by
 * an absolute-value function. Modifying the octave values in this way produces
 * ridge-like formations.
 *
 * Ridged-multifractal noise does not use a persistence value. This is because
 * the persistence values of the octaves are based on the values generated from
 * from previous octaves, creating a feedback loop (or that's what it looks like
 * after reading the code.)
 *
 * This noise module outputs ridged-multifractal-noise values that usually range
 * from -1.0 to +1.0, but there are no guarantees that all output values will
 * exist within that range.
 *
 * @note For ridged-multifractal noise generated with only one octave, the
 * output value ranges from -1.0 to 0.0.
 *
 * Ridged-multifractal noise is often used to generate craggy mountainous
 * terrain or marble-like textures.
 *
 * This noise module does not require any source modules.
 *
 * <b>Octaves</b>
 *
 * The number of octaves control the <i>amount of detail</i> of the
 * ridged-multifractal noise. Adding more octaves increases the detail of the
 * ridged-multifractal noise, but with the drawback of increasing the
 * calculation time.
 *
 * An application may specify the number of octaves that generate
 * ridged-multifractal noise by calling the SetOctaveCount() method.
 *
 * <b>Frequency</b>
 *
 * An application may specify the frequency of the first octave by calling the
 * SetFrequency() method.
 *
 * <b>Lacunarity</b>
 *
 * The lacunarity specifies the frequency multipler between successive octaves.
 *
 * The effect of modifying the lacunarity is subtle; you may need to play with
 * the lacunarity value to determine the effects. For best results, set the
 * lacunarity to a number between 1.5 and 3.5.
 *
 * <b>References &amp; Acknowledgments</b>
 *
 * <a href=http://www.texturingandmodeling.com/Musgrave.html>F. Kenton "Doc
 * Mojo" Musgrave's texturing page</a> - This page contains links to source code
 * that generates ridged-multfractal noise, among other types of noise. The
 * source file <a
 * href=http://www.texturingandmodeling.com/CODE/MUSGRAVE/CLOUD/fractal.c>
 * fractal.c</a> contains the code I used in my ridged-multifractal class (see
 * the @a RidgedMultifractal() function.) This code was written by F. Kenton
 * Musgrave, the person who created
 * <a href=http://www.pandromeda.com/>MojoWorld</a>. He is also one of the
 * authors in <i>Texturing and Modeling: A Procedural Approach</i>
 * (Morgan Kaufmann, 2002. ISBN 1-55860-848-6.)
 * @author William Matrix Peckham
 */
public class RidgedMulti extends Module {

    /**
     * default frequency for the ridgedmulti noise
     */
    public static final double DEFAULT_RIDGED_FREQUENCY = 1.0;

    /**
     * default lacunarity for the noise
     */
    public static final double DEFAULT_RIDGED_LACUNARITY = 2.0;

    /**
     * default number of octaves for the noise
     */
    public static final int DEFAULT_RIDGED_OCTAVE_COUNT = 6;

    /**
     * default quality for the noise
     */
    public static final NoiseQuality DEFAULT_RIDGED_QUALITY = NoiseQuality.STD;

    /**
     * default exponent parameter
     */
    public static final double DEFUALT_RIDGED_EXPONENT = 1.0;

    /**
     * default gain
     */
    public static final double DEFAULT_RIDGED_GAIN = 2.0;

    /**
     * default offset
     */
    public static final double DEFAULT_RIDGED_OFFSET = 1.0;

    /**
     * default seed for the noise
     */
    public static final int DEFAULT_RIDGED_SEED = 0;

    /**
     * maximum number of octaves for the nosie
     */
    public static final int RIDGED_MAX_OCTAVE = 30;

    /**
     * frequency of the first octave
     */
    protected double frequency;

    /**
     * frequency multiplier between successive octaves
     */
    protected double lacunarity;

    /**
     * offset value
     */
    protected double offset;

    /**
     * gain value
     */
    protected double gain;

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public double getGain() {
        return gain;
    }

    public void setGain(double gain) {
        this.gain = gain;
    }

    /**
     * quality of the noise
     */
    protected NoiseQuality noiseQuality;

    /**
     * Total number of octaves that generate perlin noise
     */
    protected int octaveCount;

    /**
     * exponent parameter for generating weights
     */
    protected double exponent;

    /**
     * contains the spectral weights for each octave
     */
    double[] spectralWeights;

    /**
     * seed value used by perlin noise
     */
    int seed;

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
        for (int curOctave = 0; curOctave < octaveCount; curOctave++) {

            //make sure these floating
            double nx = Globals.makeIntRange(x);
            double ny = Globals.makeIntRange(y);
            double nz = Globals.makeIntRange(z);

            //get the coherent noise value
            int localSeed = (seed + curOctave) & 0X7FFFFFFF;
            signal = Globals.gradientCoherentNoise3D(nx, ny, nz, localSeed,
                    noiseQuality);

            //make the ridges.
            signal = Math.abs(signal);
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
        calcSpectralWeights();
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
        if (octaveCount < 1 || octaveCount > RIDGED_MAX_OCTAVE) {
            throw new IllegalArgumentException("octave count is too high or low");
        }
        this.octaveCount = octaveCount;
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

    /**
     * Calculates the spectral weights for each octave.
     *
     * This method is called when the lacunarity changes.
     */
    protected void calcSpectralWeights() {
        //exposed parameter to user
        double h = exponent;

        double localFrequency = 1.0;
        for (int i = 0; i < RIDGED_MAX_OCTAVE; i++) {
            //compute weight for each frequency
            spectralWeights[i] = Math.pow(localFrequency, -h);
            frequency *= lacunarity;
        }

    }

    /**
     * returns the exponent parameter for calculating the spectral weights
     *
     * @return
     */
    public double getExponent() {
        return exponent;
    }

    /**
     * sets the exponent parameter for calculating the spectral weights
     *
     * @param exponent
     */
    public void setExponent(double exponent) {
        this.exponent = exponent;
    }

}
