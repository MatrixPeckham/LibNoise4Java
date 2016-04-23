/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.util;

import com.matrixpeckham.libnoise.model.Sphere;


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
        double curLon = westLonBound;
        double curLat = southLatBound;
        // Fill every point in the noise map with the output values from the model.
        for (int y = 0; y < destHeight; y++) {
            curLon = westLonBound;
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
     * @returns The eastern boundary of the noise map, in degrees.
     */
    public double getEastLonBound() {
        return eastLonBound;
    }

    /**
     * Returns the northern boundary of the spherical noise map
     *
     * @returns The northern boundary of the noise map, in degrees.
     */
    public double getNorthLatBound() {
        return northLatBound;
    }

    /**
     * Returns the southern boundary of the spherical noise map
     *
     * @returns The southern boundary of the noise map, in degrees.
     */
    public double getSouthLatBound() {
        return southLatBound;
    }

    /**
     * Returns the western boundary of the spherical noise map
     *
     * @returns The western boundary of the noise map, in degrees.
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
     * @pre The southern boundary is less than the northern boundary.
     * @pre The western boundary is less than the eastern boundary.
     *
     * @throw noise::ExceptionInvalidParam See the preconditions.
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

};
