/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module.combiner;

import static java.lang.Math.pow;

import com.matrixpeckham.libnoise.module.AbstractModule;
import com.matrixpeckham.libnoise.module.NoiseSample;
import java.util.logging.Logger;

/**
 * Noise module that raises the output value from a first source module to the
 * power of the output value from a second source module.
 * <p>
 * <img src="modulepower.png" alt="MODULE_POWER_IMAGE" />
 * <p>
 * The first source module must have an index value of 0.
 * <p>
 * The second source module must have an index value of 1.
 * <p>
 * This noise module requires two source modules.
 *
 * @author William Matrix Peckham
 */
public class Power extends AbstractModule {

    @Override
    public int getSourceModuleCount() {
        return 2;
    }

    @Override
    public NoiseSample getNoise(double x, double y, double z, double w, double u,
            double v) {
        NoiseSample s = new NoiseSample();
        s.value = pow(sourceModule[0].getNoise(x, y, z, w, u, v).value,
                sourceModule[1].getNoise(x, y, z, w, u, v).value);
        return s;
    }

    private static final Logger LOG = Logger.getLogger(Power.class.getName());

}
