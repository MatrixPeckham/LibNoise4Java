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
