/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module;


public class Cache extends Module {

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

    public Cache() {
        isCached = false;
    }

    @Override
    public int getSourceModuleCount() {
        return 1;
    }

    @Override
    public double getValue(double x, double y, double z) {
        if (!(isCached && xCache == x && yCache == y && zCache == z)) {
            cachedValue = sourceModule[0].getValue(x, y, z);
            xCache = x;
            yCache = y;
            zCache = z;
        }
        isCached = true;
        return cachedValue;
    }

    @Override
    public void setSourceModule(int index, Module source) {
        super.setSourceModule(index, source); //To change body of generated methods, choose Tools | Templates.
        isCached = false;
    }

}
