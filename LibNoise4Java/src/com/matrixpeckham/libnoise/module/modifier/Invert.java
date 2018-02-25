/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module.modifier;

import com.matrixpeckham.libnoise.module.AbstractModule;
import java.util.logging.Logger;

/**
 * Noise module that inverts the output value from a source module.
 *
 * <img src="moduleinvert.png" alt="MODULE_INVERT_IMAGE" />
 *
 * This noise module requires one source module.
 *
 */
public class Invert extends AbstractModule {

    @Override
    public int getSourceModuleCount() {
	return 1;
    }

    @Override
    public double getValue(double x, double y, double z) {
	return -sourceModule[0].getValue(x, y, z);
    }

    private static final Logger LOG = Logger.getLogger(Invert.class.getName());

}
