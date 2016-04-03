/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module;

/**
 * Noise module that outputs the product of the two output values from two
 * source modules.
 *
 * <img src="modulemultiply.png" alt="MODULE_MULTIPLY_IMAGE" />
 *
 * This noise module requires two source modules.
 *
 * @author William Matrix Peckham
 */
public class Multiply extends Module {

    @Override
    public int getSourceModuleCount() {
        return 2;
    }

    @Override
    public double getValue(double x, double y, double z) {
        return sourceModule[0].getValue(x, y, z) * sourceModule[1].
                getValue(x, y, z);
    }

}
