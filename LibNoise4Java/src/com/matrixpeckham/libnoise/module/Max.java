/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module;

import static com.matrixpeckham.libnoise.util.Globals.getMax;
import java.util.logging.Logger;

/**
 * Noise module that outputs the larger of the two output values from two source
 * modules.
 *
 * <img src="modulemax.png" alt="MODULE_MAX_IMAGE" />
 *
 * This noise module requires two source modules.
 *
 * @author William Matrix Peckham
 */
public class Max extends Module {

    @Override
    public int getSourceModuleCount() {
        return 2;
    }

    @Override
    public double getValue(double x, double y, double z) {
        double v0 = sourceModule[0].getValue(x, y, z);
        double v1 = sourceModule[1].getValue(x, y, z);
        return getMax(v0, v1);
    }

    private static final Logger LOG = Logger.getLogger(Max.class.getName());

}
