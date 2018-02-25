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

package com.matrixpeckham.libnoise.module.util;

import com.matrixpeckham.libnoise.module.Module;
import java.util.logging.Logger;

/**
 * ForwardModule is a module implementation that holds another module and
 * forwards all calls to it. The purpose of this module is to facilitate wrapper
 * classes, such as Cache. This module on its own does not add any
 * functionality.
 *
 * @author William Matrix Peckham
 * @param <T> Module type that we are going to use.
 */
public class ForwardModule<T extends Module> implements Module {

    //inner module that we forward to.
    private final T inner;

    /**
     * Constructor that takes the module to forward to.
     *
     * @param inner
     */
    public ForwardModule(T inner) {
	this.inner = inner;
    }

    /**
     * Provides access to the inner module, in case we want to use specific
     * methods of the subclasses instead of the index based methods of the
     * interface.
     *
     * Useful mostly for setting parameters like octaves, or constant values.
     *
     * @return
     */
    public T asT() {
	return inner;
    }

    @Override
    public Module getSourceModule(int index) {
	return inner.getSourceModule(index);
    }

    @Override
    public int getSourceModuleCount() {
	return inner.getSourceModuleCount();
    }

    @Override
    public double getValue(double x, double y, double z) {
	return inner.getValue(x, y, z);
    }

    @Override
    public void setSourceModule(int index, Module source) {
	inner.setSourceModule(index, source);
    }

    @Override
    public void setName(String name) {
	inner.setName(name);
    }

    @Override
    public String getName() {
	return inner.getName();
    }

    private static final Logger LOG
	    = Logger.getLogger(ForwardModule.class.getName());

}
