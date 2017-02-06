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
     * X coordinate.
     */
    public double x = 0;

    /**
     * Y coordinate.
     */
    public double y = 0;

    /**
     * Z coordinate.
     */
    public double z = 0;

    /**
     * Default constructor.
     */
    public Vec3() {
    }

    /**
     * Constructor.
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

    /**
     * Locally modifies this vector by adding the other.
     *
     * @param o
     */
    public void addLocal(Vec3 o) {
        x += o.x;
        y += o.y;
        z += o.z;
    }

    /**
     * Returns a new vector that is the addition of this and the other vector.
     *
     * @param o
     * @return
     */
    public Vec3 add(Vec3 o) {
        Vec3 ret = new Vec3();
        ret.x = x + o.x;
        ret.y = y + o.y;
        ret.z = z + o.z;
        return ret;
    }

    /**
     * subtracts the other vector in place from this vector.
     *
     * @param o
     */
    public void subLocal(Vec3 o) {
        x -= o.x;
        y -= o.y;
        z -= o.z;
    }

    /**
     * returns a new Vector that is the subtraction of the other vector from
     * this one.
     *
     * @param o
     * @return
     */
    public Vec3 sub(Vec3 o) {
        Vec3 ret = new Vec3();
        ret.x = x - o.x;
        ret.y = y - o.y;
        ret.z = z - o.z;
        return ret;
    }

    /**
     * local multiplication scales this vector by d.
     *
     * @param d
     */
    public void mulLocal(double d) {
        x *= d;
        y *= d;
        z *= d;
    }

    /**
     * returns the scaling of this vector by d as a new vector.
     *
     * @param d
     * @return
     */
    public Vec3 mul(double d) {
        Vec3 ret = new Vec3();
        ret.x = x * d;
        ret.y = y * d;
        ret.z = z * d;
        return ret;
    }

    private static final Logger LOG = Logger.getLogger(Vec3.class.getName());

}
