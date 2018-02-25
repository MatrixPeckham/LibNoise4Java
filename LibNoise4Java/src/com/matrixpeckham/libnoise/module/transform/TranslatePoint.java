/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module.transform;

import com.matrixpeckham.libnoise.module.AbstractModule;
import java.util.logging.Logger;

/**
 * Noise module that moves the coordinates of the input value before returning
 * the output value from a source module.
 *
 * @image html moduletranslatepoint.png
 *
 * The GetValue() method moves the ( @a x, @a y, @a z ) coordinates of the input
 * value by a translation amount before returning the output value from the
 * source module. To set the translation amount, call the SetTranslation()
 * method. To set the translation amount to apply to the individual @a x, @a y,
 * or @a z coordinates, call the SetXTranslation(), SetYTranslation() or
 * SetZTranslation() methods, respectively.
 *
 * This noise module requires one source module.
 */
public class TranslatePoint extends AbstractModule {

    /**
     * default x translation
     */
    public static final double DEFAULT_TRANSLATE_POINT_X = 0.0;

    /**
     * default y translation
     */
    public static final double DEFAULT_TRANSLATE_POINT_Y = 0.0;

    /**
     * default z translation
     */
    public static final double DEFAULT_TRANSLATE_POINT_Z = 0.0;

    /**
     * x
     */
    protected double xTranslation;

    /**
     * z
     */
    protected double yTranslation;

    /**
     * z
     */
    protected double zTranslation;

    /**
     * Default Constructor.
     */
    public TranslatePoint() {
	xTranslation = DEFAULT_TRANSLATE_POINT_X;
	yTranslation = DEFAULT_TRANSLATE_POINT_Y;
	zTranslation = DEFAULT_TRANSLATE_POINT_Z;
    }

    @Override
    public int getSourceModuleCount() {
	return 1;
    }

    @Override
    public double getValue(double x, double y, double z) {
	return sourceModule[0].getValue(x + xTranslation, y + yTranslation, z
		+ zTranslation);
    }

    /**
     * gets x translation
     *
     * @return
     */
    public double getxTranslation() {
	return xTranslation;
    }

    /**
     * get y translation
     *
     * @return
     */
    public double getyTranslation() {
	return yTranslation;
    }

    /**
     * get z translation
     *
     * @return
     */
    public double getzTranslation() {
	return zTranslation;
    }

    /**
     * Sets the translation amount to apply to the input value.
     *
     * @param translation The translation amount to apply.
     *
     * The GetValue() method moves the ( @a x, @a y, @a z ) coordinates of the
     * input value by a translation amount before returning the output value
     * from the source module
     */
    public void setTranslation(double translation) {
	xTranslation = translation;
	yTranslation = translation;
	zTranslation = translation;
    }

    /**
     * Sets the translation amounts to apply to the ( @a x, @a y, @a z )
     * coordinates of the input value.
     *
     * @param x The translation amount to apply to the @a x coordinate.
     * @param y The translation amount to apply to the @a y coordinate.
     * @param z The translation amount to apply to the @a z coordinate.
     *
     * The GetValue() method moves the ( @a x, @a y, @a z ) coordinates of the
     * input value by a translation amount before returning the output value
     * from the source module
     */
    public void setTranslation(double x, double y, double z) {
	xTranslation = x;
	yTranslation = y;
	zTranslation = z;
    }

    /**
     * sets x translation
     *
     * @param xTranslation
     */
    public void setxTranslation(double xTranslation) {
	this.xTranslation = xTranslation;
    }

    /**
     * sets y translation
     *
     * @param yTranslation
     */
    public void setyTranslation(double yTranslation) {
	this.yTranslation = yTranslation;
    }

    /**
     * sets z translation
     *
     * @param zTranslation
     */
    public void setzTranslation(double zTranslation) {
	this.zTranslation = zTranslation;
    }

    private static final Logger LOG
	    = Logger.getLogger(TranslatePoint.class.getName());

}
