/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module.generator;

import static com.matrixpeckham.libnoise.util.Globals.makeIntRange;
import static java.lang.Math.floor;

import com.matrixpeckham.libnoise.module.AbstractModule;
import com.matrixpeckham.libnoise.module.NoiseSample;
import java.util.logging.Logger;

/**
 * Noise module that outputs a checkerboard pattern.
 * <p>
 * <img src="modulecheckerboard.png" alt="MODULE_CHECKER_IMAGE" />
 * <p>
 * This noise module outputs unit-sized blocks of alternating values. The values
 * of these blocks alternate between -1.0 and +1.0.
 * <p>
 * This noise module is not really useful by itself, but it is often used for
 * debugging purposes.
 * <p>
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
    public NoiseSample getNoise(double x, double y, double z, double w, double u,
            double v) {
        int ix = (int) floor(makeIntRange(x));
        int iy = (int) floor(makeIntRange(y));
        int iz = (int) floor(makeIntRange(z));
        int iw = (int) floor(makeIntRange(w));
        int iu = (int) floor(makeIntRange(u));
        int iv = (int) floor(makeIntRange(v));
        NoiseSample s = new NoiseSample();
//        s.value=(ix & 1 ^ iy & 1 ^ iz & 1) != 0 ? -1 : 1;
        s.value = ((ix + iy + iz + iw + iu + iv) % 2 == 0) ? -1 : 1;
        return s;
    }

    private static final Logger LOG
            = Logger.getLogger(Checkerboard.class.getName());

}
