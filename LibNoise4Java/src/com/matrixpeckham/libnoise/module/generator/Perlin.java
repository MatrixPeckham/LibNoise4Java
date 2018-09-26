/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module.generator;

import static com.matrixpeckham.libnoise.util.Globals.*;
import static com.matrixpeckham.libnoise.util.NoiseQuality.STD;

import com.matrixpeckham.libnoise.module.AbstractModule;
import com.matrixpeckham.libnoise.module.NoiseSample;
import com.matrixpeckham.libnoise.util.NoiseQuality;
import java.util.logging.Logger;

/**
 * Noise module that outputs 3-dimensional Perlin noise.
 *
 * @image html moduleperlin.png
 *
 * Perlin noise is the sum of several coherent-noise functions of
 * ever-increasing frequencies and ever-decreasing amplitudes.
 *
 * An important property of Perlin noise is that a small change in the input
 * value will produce a small change in the output value, while a large change
 * in the input value will produce a random change in the output value.
 *
 * This noise module outputs Perlin-noise values that usually range from -1.0 to
 * +1.0, but there are no guarantees that all output values will exist within
 * that range.
 *
 * For a better description of Perlin noise, see the links in the
 * <i>References and Acknowledgments</i> section.
 *
 * This noise module does not require any source modules.
 *
 * <b>Octaves</b>
 *
 * The number of octaves control the <i>amount of detail</i> of the Perlin
 * noise. Adding more octaves increases the detail of the Perlin noise, but with
 * the drawback of increasing the calculation time.
 *
 * An octave is one of the coherent-noise functions in a series of
 * coherent-noise functions that are added together to form Perlin noise.
 *
 * An application may specify the frequency of the first octave by calling the
 * SetFrequency() method.
 *
 * An application may specify the number of octaves that generate Perlin noise
 * by calling the SetOctaveCount() method.
 *
 * These coherent-noise functions are called octaves because each octave has, by
 * default, double the frequency of the previous octave. Musical tones have this
 * property as well; a musical C tone that is one octave higher than the
 * previous C tone has double its frequency.
 *
 * <b>Frequency</b>
 *
 * An application may specify the frequency of the first octave by calling the
 * SetFrequency() method.
 *
 * <b>Persistence</b>
 *
 * The persistence value controls the <i>roughness</i> of the Perlin noise.
 * Larger values produce rougher noise.
 *
 * The persistence value determines how quickly the amplitudes diminish for
 * successive octaves. The amplitude of the first octave is 1.0. The amplitude
 * of each subsequent octave is equal to the product of the previous octave's
 * amplitude and the persistence value. So a persistence value of 0.5 sets the
 * amplitude of the first octave to 1.0; the second, 0.5; the third, 0.25; etc.
 *
 * An application may specify the persistence value by calling the
 * SetPersistence() method.
 *
 * <b>Lacunarity</b>
 *
 * The lacunarity specifies the frequency multipler between successive octaves.
 *
 * The effect of modifying the lacunarity is subtle; you may need to play with
 * the lacunarity value to determine the effects. For best results, set the
 * lacunarity to a number between 1.5 and 3.5.
 *
 * <b>References &amp; acknowledgments</b>
 *
 * <a href=http://www.noisemachine.com/talk1/>The Noise Machine</a> - From the
 * master, Ken Perlin himself. This page contains a presentation that describes
 * Perlin noise and some of its variants. He won an Oscar for creating the
 * Perlin noise algorithm!
 *
 * <a
 * href=http://freespace.virgin.net/hugo.elias/models/m_perlin.htm>
 * Perlin Noise</a> - Hugo Elias's webpage contains a very good description of
 * Perlin noise and describes its many applications. This page gave me the
 * inspiration to create libnoise in the first place. Now that I know how to
 * generate Perlin noise, I will never again use cheesy subdivision algorithms
 * to create terrain (unless I absolutely need the speed.)
 *
 * <a
 * href=http://www.robo-murito.net/code/perlin-noise-math-faq.html>The Perlin
 * noise math FAQ</a> - A good page that describes Perlin noise in plain English
 * with only a minor amount of math. During development of libnoise, I noticed
 * that my coherent-noise function generated terrain with some "regularity" to
 * the terrain features. This page describes a better coherent-noise function
 * called <i>gradient noise</i>. This version of noise::module::Perlin uses
 * gradient coherent noise to generate Perlin noise.
 */
public class Perlin extends AbstractModule {

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

    /**
     * Default Constructor.
     */
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
    public NoiseSample getNoise(double x, double y, double z, double w, double u,
            double v) {
        double value = 0;
        double curPersistence = 1;
        double nx, ny, nz, nw, nu, nv;
        int localSeed;
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;
        u *= frequency;
        v *= frequency;
        for (int curOctave = 0; curOctave < octaveCount;
                curOctave++) {
            //make sure that these floating point values have the same range as
            //an integer so that we can pass them to the coherent noise function
            nx = makeIntRange(x);
            ny = makeIntRange(y);
            nz = makeIntRange(z);
            nw = makeIntRange(w);
            nu = makeIntRange(u);
            nv = makeIntRange(v);
            //get the coherent noise value from the input value and add it to the
            //final result
            localSeed = (seed + curOctave) & 0XFFFF_FFFF;
            double signal
                    = gradientCoherentNoise6D(nx, ny, nz, nw, nu, nv, localSeed,
                            noiseQuality);
            value += signal * curPersistence;
            //prepare next octave
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
            u *= lacunarity;
            v *= lacunarity;
            curPersistence *= persistence;
        }
        NoiseSample s = new NoiseSample();
        s.value = value;
        return s;
    }

    public double getValue(double x, double y, double z) {
        double value = 0;
        double curPersistence = 1;
        double nx, ny, nz, nw, nu, nv;
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
            double signal
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
        if (octaveCount < 1 || octaveCount > PERLIN_MAX_OCTAVE) {
            throw new IllegalArgumentException("octave count is too high or low");
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

    private static final Logger LOG = Logger.getLogger(Perlin.class.getName());

}
