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
