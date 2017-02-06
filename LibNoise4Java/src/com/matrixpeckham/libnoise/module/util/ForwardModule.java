/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
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

    private static final Logger LOG
	    = Logger.getLogger(ForwardModule.class.getName());

}
