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

import com.matrixpeckham.libnoise.module.Module;

/**
 * Abstract base class for a noise-map builder.
 *
 * A builder class builds a noise map by filling it with coherent-noise values
 * generated from the surface of a three-dimensional mathematical object. Each
 * builder class defines a specific three-dimensional surface, such as a
 * cylinder, sphere, or plane.
 *
 * A builder class describes these input values using a coordinate system
 * applicable for the mathematical object (e.g., a latitude/longitude coordinate
 * system for the spherical noise-map builder.) It then "flattens" these
 * coordinates onto a plane so that it can write the coherent-noise values into
 * a two-dimensional noise map.
 *
 * <b>Building the Noise Map</b>
 *
 * To build the noise map, perform the following steps: - Pass the bounding
 * coordinates to the SetBounds() method. - Pass the noise map size, in points,
 * to the SetDestSize() method. - Pass a NoiseMap object to the
 * SetDestNoiseMap() method. - Pass a noise module (derived from
 * noise::module::AbstractModule) to the SetSourceModule() method. - Call the
 * Build() method.
 *
 * You may also pass a callback function to the SetCallback() method. The
 * Build() method calls this callback function each time it fills a row of the
 * noise map with coherent-noise values. This callback function has a single
 * integer parameter that contains a count of the rows that have been completed.
 * It returns void.
 *
 * Note that SetBounds() is not defined in the abstract base class; it is only
 * defined in the derived classes. This is because each model uses a different
 * coordinate system.
 */
public abstract class NoiseMapBuilder {

    /**
     * The callback function that Build() calls each time it fills a row of the
     * noise map with coherent-noise values.
     *
     * This callback function has a single integer parameter that contains a
     * count of the rows that have been completed. It returns void. Pass a
     * function with this signature to the SetCallback() method.
     */
    protected NoiseMapCallback callback;

    /**
     * Height of the destination noise map, in points.
     */
    protected int destHeight;

    /**
     * Destination noise map that will contain the coherent-noise values.
     */
    protected NoiseMap destNoiseMap;

    /**
     * Width of the destination noise map, in points.
     */
    protected int destWidth;

    /**
     * Source noise module that will generate the coherent-noise values.
     */
    protected Module sourceModule;

    /**
     * Constructor.
     */
    public NoiseMapBuilder() {
        callback = null;
        destHeight = 0;
        destWidth = 0;
        destNoiseMap = null;
        sourceModule = null;
    }

    /**
     * Builds the noise map.
     *
     * @noise.pre SetBounds() was previously called.
     * @noise.pre SetDestNoiseMap() was previously called.
     * @noise.pre SetSourceModule() was previously called.
     * @noise.pre The width and height values specified by SetDestSize() are
     * positive.
     * @noise.pre The width and height values specified by SetDestSize() do not
     * exceed the maximum possible width and height for the noise map.
     *
     * @noise.post The original contents of the destination noise map is
     * destroyed.
     *
     * @throws IllegalStateException See the preconditions.
     *
     * If this method is successful, the destination noise map contains the
     * coherent-noise values from the noise module specified by
     * SetSourceModule().
     */
    public abstract void build();

    /**
     * Returns the height of the destination noise map.
     *
     * @return The height of the destination noise map, in points.
     *
     * This object does not change the height in the destination noise map
     * object until the Build() method is called.
     */
    public double getDestHeight() {
        return destHeight;
    }

    /**
     * Returns the width of the destination noise map.
     *
     * @return The width of the destination noise map, in points.
     *
     * This object does not change the height in the destination noise map
     * object until the Build() method is called.
     */
    public double getDestWidth() {
        return destWidth;
    }

    /**
     * Sets the callback function that Build() calls each time it fills a row of
     * the noise map with coherent-noise values.
     *
     * @param pCallback The callback function.
     *
     * This callback function has a single integer parameter that contains a
     * count of the rows that have been completed. It returns void. Pass a
     * function with this signature to the SetCallback() method.
     */
    public void setCallback(
            NoiseMapCallback pCallback) {
        this.callback = pCallback;
    }

    /**
     * Sets the destination noise map.
     *
     * @param destNoiseMap The destination noise map.
     *
     * The destination noise map will contain the coherent-noise values from
     * this noise map after a successful call to the Build() method.
     *
     * The destination noise map must exist throughout the lifetime of this
     * object unless another noise map replaces that noise map.
     */
    public void setDestNoiseMap(
            NoiseMap destNoiseMap) {
        this.destNoiseMap = destNoiseMap;
    }

    /**
     * Sets the size of the destination noise map.
     *
     * @param destWidth The width of the destination noise map, in points.
     * @param destHeight The height of the destination noise map, in points.
     *
     * This method does not change the size of the destination noise map until
     * the Build() method is called.
     */
    public void setDestSize(int destWidth, int destHeight) {
        this.destWidth = destWidth;
        this.destHeight = destHeight;
    }

    /**
     * Sets the source module.
     *
     * @param sourceModule The source module.
     *
     * This object fills in a noise map with the coherent-noise values from this
     * source module.
     *
     * The source module must exist throughout the lifetime of this object
     * unless another noise module replaces that noise module.
     */
    public void setSourceModule(
            Module sourceModule) {
        this.sourceModule = sourceModule;
    }

}
