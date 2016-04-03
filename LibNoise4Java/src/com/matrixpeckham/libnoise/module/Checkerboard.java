/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module;

import com.matrixpeckham.libnoise.util.Globals;

/**
 * Noise module that outputs a checkerboard pattern.
 *
 * <img src="modulecheckerboard.png" alt="MODULE_CHECKER_IMAGE" />
 *
 * This noise module outputs unit-sized blocks of alternating values. The values
 * of these blocks alternate between -1.0 and +1.0.
 *
 * This noise module is not really useful by itself, but it is often used for
 * debugging purposes.
 *
 * This noise module does not require any source modules.
 *
 * @author William Matrix Peckham
 */
public class Checkerboard extends Module {

    @Override
    public int getSourceModuleCount() {
        return 0;
    }

    @Override
    public double getValue(double x, double y, double z) {
        int ix = (int) Math.floor(Globals.makeIntRange(x));
        int iy = (int) Math.floor(Globals.makeIntRange(y));
        int iz = (int) Math.floor(Globals.makeIntRange(z));
        return (ix & 1 ^ iy & 1 ^ iz & 1) != 0 ? -1 : 1;
    }

}
