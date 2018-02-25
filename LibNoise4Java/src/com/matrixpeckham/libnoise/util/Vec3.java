/*
 * Copyright © 2017-2018 William Peckham
 *
 * This file is part of LibNoise4Java.
 * 
 * LibNoise4Java is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * LibNoise4Java is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with LibNoise4Java.  If not, see <http://www.gnu.org/licenses/>. 
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

    //logger so that we don't get "no logger" warning
    private static final Logger LOG = Logger.getLogger(Vec3.class.getName());

}
