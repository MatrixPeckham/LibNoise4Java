/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module;

import static java.lang.Math.pow;
import java.util.logging.Logger;

/**
 * Noise module that raises the output value from a first source module to the
 * power of the output value from a second source module.
 *
 * <img src="modulepower.png" alt="MODULE_POWER_IMAGE" />
 *
 * The first source module must have an index value of 0.
 *
 * The second source module must have an index value of 1.
 *
 * This noise module requires two source modules.
 *
 * @author William Matrix Peckham
 */
public class Power extends Module {

    @Override
    public int getSourceModuleCount() {
        return 2;
    }

    @Override
    public double getValue(double x, double y, double z) {
        return pow(sourceModule[0].getValue(x, y, z), sourceModule[1].
                getValue(x, y, z));
    }

    private static final Logger LOG = Logger.getLogger(Power.class.getName());

}
