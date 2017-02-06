/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module.generator;

import com.matrixpeckham.libnoise.module.AbstractModule;
import static com.matrixpeckham.libnoise.util.Globals.makeIntRange;
import static java.lang.Math.floor;
import java.util.logging.Logger;

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
public class Checkerboard extends AbstractModule {

    @Override
    public int getSourceModuleCount() {
        return 0;
    }

    @Override
    public double getValue(double x, double y, double z) {
        int ix = (int) floor(makeIntRange(x));
        int iy = (int) floor(makeIntRange(y));
        int iz = (int) floor(makeIntRange(z));
        return (ix & 1 ^ iy & 1 ^ iz & 1) != 0 ? -1 : 1;
    }

    private static final Logger LOG
            = Logger.getLogger(Checkerboard.class.getName());

}
