/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2017-2018 William Peckham
 */
package com.matrixpeckham.libnoise.module.util;

import com.matrixpeckham.libnoise.module.Module;
import java.util.logging.Logger;

/**
 * Noise module that caches the last output value generated by a source module.
 *
 * If an application passes an input value to the GetValue() method that differs
 * from the previously passed-in input value, this noise module instructs the
 * source module to calculate the output value. This value, as well as the ( x,
 * y, z ) coordinates of the input value, are stored (cached) in this noise
 * module.
 *
 * If the application passes an input value to the GetValue() method that is
 * equal to the previously passed-in input value, this noise module returns the
 * cached output value without having the source module recalculate the output
 * value.
 *
 * If an application passes a new source module to the SetSourceModule() method,
 * the cache is invalidated.
 *
 * Caching a noise module is useful if it is used as a source module for
 * multiple noise modules. If a source module is not cached, the source module
 * will redundantly calculate the same output value once for each noise module
 * in which it is included.
 *
 * This noise module requires one source module.
 *
 * @param <T>
 */
public class Cache<T extends Module> extends ForwardModule<T> {

    /**
     * The cached output value at the cached input value.
     */
    protected double cachedValue;

    /**
     * determine if the output value is stored in this module
     */
    protected boolean isCached;

    /**
     * x coordinate of the input value
     */
    protected double xCache;

    /**
     * y coordinate of the input value
     */
    protected double yCache;

    /**
     * z coordinate of the input value
     */
    protected double zCache;

    /**
     * Default Constructor
     *
     * @param m Module to wrap
     */
    public Cache(T m) {
	super(m);
	isCached = false;
    }

    //simple implementation based on forward module
    @Override
    public double getValue(double x, double y, double z) {
	//extreamly simple caching scheme
	if (!(isCached && xCache == x && yCache == y && zCache == z)) {
	    cachedValue = super.getValue(x, y, z);
	    xCache = x;
	    yCache = y;
	    zCache = z;
	}
	isCached = true;
	return cachedValue;
    }

    @Override
    public void setSourceModule(int index, Module source) {
	super.setSourceModule(index, source);
	isCached = false;
    }

    @Override
    public String getName() {
	return "Cached (" + super.getName() + " )";
    }

    @Override
    public String toString() {
	return getName();
    }

    private static final Logger LOG = Logger.getLogger(Cache.class.getName());

}
