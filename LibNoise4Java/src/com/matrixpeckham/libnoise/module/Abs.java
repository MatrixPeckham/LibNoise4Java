/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module;

/**
 * Noise module that outputs the absolute value of the output value of a source
 * module.
 *
 * <img src="moduleabs.png" alt="ABS_MODULE_IMAGE" />
 *
 * This module requires one source module.
 *
 * @author William Matrix Peckham
 */
public class Abs extends Module {

    @Override
    public int getSourceModuleCount() {
        return 1;
    }

    @Override
    public double getValue(double x, double y, double z) {
        return Math.abs(sourceModule[0].getValue(x, y, z));
    }

}
