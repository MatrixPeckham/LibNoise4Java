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
 * Callback class for use with NoiseMap. Replaces function pointer in original
 * C++.
 */
//@FunctionalInterface//FIXME: java 1.8 only, commented out for 1.7 compatability
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
