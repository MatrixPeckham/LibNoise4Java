/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.util;

import java.util.logging.Logger;

/**
 * Temporarily non-public.
 *
 * @author William Matrix Peckham
 */
class NoiseSample {

    public double value = 0;

    public Vec3 derivative = new Vec3();

    private static final Logger LOG
            = Logger.getLogger(NoiseSample.class.getName());

}
