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
 * Holds an x, y, z triple.
 *
 * @author William Matrix Peckham
 */
public class Vec3 {

    /**
     *
     */
    public double x = 0;

    /**
     *
     */
    public double y = 0;

    /**
     *
     */
    public double z = 0;

    /**
     *
     */
    public Vec3() {
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     */
    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    private static final Logger LOG = Logger.getLogger(Vec3.class.getName());
}
