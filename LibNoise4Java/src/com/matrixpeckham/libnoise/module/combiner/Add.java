/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module.combiner;

import com.matrixpeckham.libnoise.module.AbstractModule;
import java.util.logging.Logger;

/**
 * Noise module that outputs the sum of the two output values from two source
 * modules.
 *
 * <img src="moduleadd.png" alt="MODULE_ADD_IMG" />
 *
 * This module requires two source modules.
 *
 * @author William Matrix Peckham
 */
public class Add extends AbstractModule {

    @Override
    public int getSourceModuleCount() {
        return 2;
    }

    @Override
    public double getValue(double x, double y, double z) {
        return sourceModule[0].getValue(x, y, z) + sourceModule[1].
                getValue(x, y, z);
    }

    private static final Logger LOG = Logger.getLogger(Add.class.getName());

}
