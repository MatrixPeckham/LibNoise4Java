/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module.transform;

import com.matrixpeckham.libnoise.module.AbstractModule;
import static com.matrixpeckham.libnoise.util.Globals.DEG_TO_RAD;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import java.util.logging.Logger;

/**
 * Noise module that rotates the input value around the origin before returning
 * the output value from a source module.
 *
 * <img src="modulerotatepoint.png" alt="MODULE_ROTATEPOINT_IMAGE" />
 *
 * The GetValue() method rotates the coordinates of the input value around the
 * origin before returning the output value from the source module. To set the
 * rotation angles, call the SetAngles() method. To set the rotation angle
 * around the individual @a x, @a y, or @a z axes, call the SetXAngle(),
 * SetYAngle() or SetZAngle() methods, respectively.
 *
 * The coordinate system of the input value is assumed to be "left-handed" (@a x
 * increases to the right, @a y increases upward, and @a z increases inward.)
 *
 * This noise module requires one source module.
 *
 * @author William Matrix Peckham
 */
//TODO: Should this be a forwarding extender?
public class RotatePoint extends AbstractModule {

    /**
     * Default x rotation angle
     */
    public static final double DEFAULT_ROTATE_X = 0.0;

    /**
     * Default y rotation angle
     */
    public static final double DEFAULT_ROTATE_Y = 0.0;

    /**
     * Default z rotation angle
     */
    public static final double DEFAULT_ROTATE_Z = 0.0;

    /**
     * entries in matrix
     */
    protected double x1Matrix;

    /**
     *
     */
    protected double x2Matrix;

    /**
     *
     */
    protected double x3Matrix;

    /**
     * x rotation angle in degrees
     */
    protected double xAngle;

    /**
     * entry in matrix
     */
    protected double y1Matrix;

    /**
     *
     */
    protected double y2Matrix;

    /**
     *
     */
    protected double y3Matrix;

    /**
     * y angle in degrees
     */
    protected double yAngle;

    /**
     * entry in matrix
     */
    protected double z1Matrix;

    /**
     *
     */
    protected double z2Matrix;

    /**
     *
     */
    protected double z3Matrix;

    /**
     * z angle in degrees
     */
    protected double zAngle;

    /**
     *
     */
    public RotatePoint() {
	setAngles(DEFAULT_ROTATE_X, DEFAULT_ROTATE_Y, DEFAULT_ROTATE_Z);
    }

    @Override
    public int getSourceModuleCount() {
	return 1;
    }

    @Override
    public double getValue(double x, double y, double z) {
	double nx = (x1Matrix * x) + (y1Matrix * y) + (z1Matrix * z);
	double ny = (x2Matrix * x) + (y2Matrix * y) + (z2Matrix * z);
	double nz = (x3Matrix * x) + (y3Matrix * y) + (z3Matrix * z);
	return sourceModule[0].getValue(nx, ny, nz);
    }

    /**
     * Returns the rotation angle around the @a x axis to apply to the input
     * value.
     *
     * @return The rotation angle around the @a x axis, in degrees.
     */
    public double getXAngle() {
	return xAngle;
    }

    /**
     * Returns the rotation angle around the @a y axis to apply to the input
     * value.
     *
     * @return The rotation angle around the @a y axis, in degrees.
     */
    public double getYAngle() {
	return yAngle;
    }

    /**
     * Returns the rotation angle around the @a z axis to apply to the input
     * value.
     *
     * @return The rotation angle around the @a z axis, in degrees.
     */
    public double getZAngle() {
	return zAngle;
    }

    /**
     * Sets the rotation angles around all three axes to apply to the input
     * value.
     *
     * @param xAngle The rotation angle around the @a x axis, in degrees.
     * @param yAngle The rotation angle around the @a y axis, in degrees.
     * @param zAngle The rotation angle around the @a z axis, in degrees.
     *
     * The GetValue() method rotates the coordinates of the input value around
     * the origin before returning the output value from the source module.
     */
    public final void setAngles(double xAngle, double yAngle, double zAngle) {
	double xCos, yCos, zCos, xSin, ySin, zSin;
	xCos = cos(xAngle * DEG_TO_RAD);
	yCos = cos(yAngle * DEG_TO_RAD);
	zCos = cos(zAngle * DEG_TO_RAD);
	xSin = sin(xAngle * DEG_TO_RAD);
	ySin = sin(yAngle * DEG_TO_RAD);
	zSin = sin(zAngle * DEG_TO_RAD);

	x1Matrix = ySin * xSin * zSin + yCos * zCos;
	y1Matrix = xCos * zSin;
	z1Matrix = ySin * zCos - yCos * xSin * zSin;
	x2Matrix = ySin * xSin * zCos - yCos * zSin;
	y2Matrix = xCos * zCos;
	z2Matrix = -yCos * xSin * zCos - ySin * zSin;
	x3Matrix = -ySin * xCos;
	y3Matrix = xSin;
	z3Matrix = yCos * xCos;

	this.xAngle = xAngle;
	this.yAngle = yAngle;
	this.zAngle = zAngle;

    }

    /**
     * Sets the rotation angle around the @a x axis to apply to the input value.
     *
     * @param xAngle The rotation angle around the @a x axis, in degrees.
     *
     * The GetValue() method rotates the coordinates of the input value around
     * the origin before returning the output value from the source module.
     */
    public void setXAngle(double xAngle) {
	setAngles(xAngle, yAngle, zAngle);
    }

    /**
     * Sets the rotation angle around the @a y axis to apply to the input value.
     *
     * @param yAngle The rotation angle around the @a y axis, in degrees.
     *
     * The GetValue() method rotates the coordinates of the input value around
     * the origin before returning the output value from the source module.
     */
    public void setYAngle(double yAngle) {
	setAngles(xAngle, yAngle, zAngle);
    }

    /**
     * Sets the rotation angle around the @a z axis to apply to the input value.
     *
     * @param zAngle The rotation angle around the @a z axis, in degrees.
     *
     * The GetValue() method rotates the coordinates of the input value around
     * the origin before returning the output value from the source module.
     */
    public void setZAngle(double zAngle) {
	setAngles(xAngle, yAngle, zAngle);
    }

    private static final Logger LOG
	    = Logger.getLogger(RotatePoint.class.getName());

}
