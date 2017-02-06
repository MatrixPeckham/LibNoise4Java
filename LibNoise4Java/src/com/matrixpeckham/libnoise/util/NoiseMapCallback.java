/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.util;

/**
 * Callback class for use with NoiseMap. Replaces function pointer in original
 * C++.
 */
//@FunctionalInterface//java 1.8 only, commented out for 1.7 compatability
public interface NoiseMapCallback {

    /**
     * The NoiseMapBuilder::Build() method calls this callback function each
     * time it fills a row of the noise map with coherent-noise values.
     *
     * This callback function has a single integer parameter that contains a
     * count of the rows that have been completed. It returns void. Pass a
     * function with this signature to the NoiseMapBuilder::SetCallback()
     * method.
     *
     * @param row
     */
    public void rowFinished(int row);
}
