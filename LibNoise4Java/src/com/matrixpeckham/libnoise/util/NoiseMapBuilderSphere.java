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

import com.matrixpeckham.libnoise.model.Sphere;
import java.util.logging.Logger;

/**
 * Builds a spherical noise map.
 *
 * This class builds a noise map by filling it with coherent-noise values
 * generated from the surface of a sphere.
 *
 * This class describes these input values using a (latitude, longitude)
 * coordinate system. After generating the coherent-noise value from the input
 * value, it then "flattens" these coordinates onto a plane so that it can write
 * the values into a two-dimensional noise map.
 *
 * The sphere model has a radius of 1.0 unit. Its center is at the origin.
 *
 * The x coordinate in the noise map represents the longitude. The y coordinate
 * in the noise map represents the latitude.
 *
 * The application must provide the southern, northern, western, and eastern
 * bounds of the noise map, in degrees.
 */
public class NoiseMapBuilderSphere extends NoiseMapBuilder {

    /**
     * Eastern boundary of the spherical noise map, in degrees.
     */
    private double eastLonBound;

    /**
     * Northern boundary of the spherical noise map, in degrees.
     */
    private double northLatBound;

    /**
     * Southern boundary of the spherical noise map, in degrees.
     */
    private double southLatBound;

    /**
     * Western boundary of the spherical noise map, in degrees.
     */
    private double westLonBound;

    /**
     * Constructor.
     */
    public NoiseMapBuilderSphere() {
        eastLonBound = 0;
        westLonBound = 0;
        northLatBound = 0;
        southLatBound = 0;
    }

    @Override
    public void build() {
        if (eastLonBound <= westLonBound
                || northLatBound <= southLatBound
                || destWidth <= 0
                || destHeight <= 0
                || sourceModule == null
                || destNoiseMap == null) {
            throw new IllegalArgumentException();
        }
        // Resize the destination noise map so that it can store the new output
        // values from the source model.
        destNoiseMap.setSize(destWidth, destHeight);
        // Create the plane model.
        Sphere sphereModel = new Sphere();
        sphereModel.setModule(sourceModule);
        double lonExtent = eastLonBound - westLonBound;
        double latExtent = northLatBound - southLatBound;
        double xDelta = lonExtent / destWidth;
        double yDelta = latExtent / destHeight;
        double curLat = southLatBound;
        // Fill every point in the noise map with the output values from the model.
        for (int y = 0; y < destHeight; y++) {
            double curLon = westLonBound;
            for (int x = 0; x < destWidth; x++) {
                float curValue = (float) sphereModel.getValue(curLat, curLon);
                destNoiseMap.setValue(x, y, curValue);
                curLon += xDelta;
            }
            curLat += yDelta;
            if (callback != null) {
                callback.rowFinished(y);
            }
        }
    }

    /**
     * Returns the eastern boundary of the spherical noise map.
     *
     * @return The eastern boundary of the noise map, in degrees.
     */
    public double getEastLonBound() {
        return eastLonBound;
    }

    /**
     * Returns the northern boundary of the spherical noise map
     *
     * @return The northern boundary of the noise map, in degrees.
     */
    public double getNorthLatBound() {
        return northLatBound;
    }

    /**
     * Returns the southern boundary of the spherical noise map
     *
     * @return The southern boundary of the noise map, in degrees.
     */
    public double getSouthLatBound() {
        return southLatBound;
    }

    /**
     * Returns the western boundary of the spherical noise map
     *
     * @return The western boundary of the noise map, in degrees.
     */
    public double getWestLonBound() {
        return westLonBound;
    }

    /**
     * Sets the coordinate boundaries of the noise map.
     *
     * @param southLatBound The southern boundary of the noise map, in degrees.
     * @param northLatBound The northern boundary of the noise map, in degrees.
     * @param westLonBound The western boundary of the noise map, in degrees.
     * @param eastLonBound The eastern boundary of the noise map, in degrees.
     *
     * @noise.pre The southern boundary is less than the northern boundary.
     * @noise.pre The western boundary is less than the eastern boundary.
     *
     * @throws IllegalArgumentException See the preconditions.
     */
    public void setBounds(double southLatBound, double northLatBound,
            double westLonBound, double eastLonBound) {
        if (southLatBound >= northLatBound
                || westLonBound >= eastLonBound) {
            throw new IllegalArgumentException();
        }

        this.southLatBound = southLatBound;
        this.northLatBound = northLatBound;
        this.westLonBound = westLonBound;
        this.eastLonBound = eastLonBound;
    }

    private static final Logger LOG
            = Logger.getLogger(NoiseMapBuilderSphere.class.getName());

};
