/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.util;

/**
 * Enumerates the noise qualities.
 *
 * @author William Matrix Peckham
 */
public enum NoiseQuality {

    /**
     * Generates coherent noise quickly. When a coherent-noise function with
     * this quality setting is used to generate a bump-map image, there are
     * noticeable "creasing" artifacts in the resulting image. This is because
     * the derivative of that function is discontinuous at integer boundaries.
     */
    FAST,
    /**
     * Generates standard-quality coherent noise. When a coherent-noise function
     * with this quality setting is used to generate a bump-map image, there are
     * some minor "creasing" artifacts in the resulting image. This is because
     * the second derivative of that function is discontinuous at integer
     * boundaries.
     */
    STD,
    /**
     * Generates the best-quality coherent noise. When a coherent-noise function
     * with this quality setting is used to generate a bump-map image, there are
     * no "creasing" artifacts in the resulting image. This is because the first
     * and second derivatives of that function are continuous at integer
     * boundaries.
     */
    BEST

}
