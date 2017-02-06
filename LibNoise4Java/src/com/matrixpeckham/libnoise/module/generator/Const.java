/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module.generator;

import com.matrixpeckham.libnoise.module.AbstractModule;
import java.util.logging.Logger;

/**
 * Noise module that outputs a constant value.
 *
 * @image html moduleconst.png
 *
 * To specify the constant value, call the SetConstValue() method.
 *
 * This noise module is not useful by itself, but it is often used as a source
 * module for other noise modules.
 *
 * This noise module does not require any source modules.
 */
public class Const extends AbstractModule {

    /**
     * Default constant value for the Const noise module.
     */
    public static final double DEFAULT_CONST_VALUE = 0;

    /**
     * Constant value this module will return.
     */
    protected double constValue;

    /**
     * Default constructor sets the value to the default.
     */
    public Const() {
        //super(0);
        constValue = DEFAULT_CONST_VALUE;
    }

    /**
     * Gets the value that this module is set to return.
     *
     * @return
     */
    public double getConstValue() {
        return constValue;
    }

    /**
     * returns the required number of source modules, which is none.
     *
     * @return
     */
    @Override
    public int getSourceModuleCount() {
        return 0;
    }

    /**
     * implements the noise function, returns constant value.
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    @Override
    public double getValue(double x, double y, double z) {
        return constValue;
    }

    /**
     * Sets the value that the module will return.
     *
     * @param constValue
     */
    public void setConstValue(double constValue) {
        this.constValue = constValue;
    }

    private static final Logger LOG = Logger.getLogger(Const.class.getName());

}
