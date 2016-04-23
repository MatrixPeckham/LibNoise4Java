/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.util;

import com.matrixpeckham.libnoise.model.Cylinder;


public class NoiseMapBuilderCylinder extends NoiseMapBuilder {


    /**
     * Lower angle boundary of the cylindrical noise map, in degrees.
     */
    double lowerAngleBound;

    /**
     * Lower height boundary of the cylindrical noise map, in units.
     */
    double lowerHeightBound;

    /**
     * Upper angle boundary of the cylindrical noise map, in degrees.
     */
    double upperAngleBound;

    /**
     * Upper height boundary of the cylindrical noise map, in units.
     */
    double upperHeightBound;

    /**
     * Constructor.
     */
    public NoiseMapBuilderCylinder() {
        lowerAngleBound = 0;
        lowerHeightBound = 0;
        upperAngleBound = 0;
        upperHeightBound = 0;
    }

    @Override
    public void build() {
        if (upperAngleBound <= lowerAngleBound
                || upperHeightBound <= lowerHeightBound
                || destWidth <= 0
                || destHeight <= 0
                || sourceModule == null
                || destNoiseMap == null) {
            throw new IllegalArgumentException();
        }
        // Resize the destination noise map so that it can store the new output
        // values from the source model.
        destNoiseMap.setSize(destWidth, destHeight);
        // Create the cylinder model.
        Cylinder cylinderModel = new Cylinder();
        cylinderModel.setModule(sourceModule);
        double angleExtent = upperAngleBound - lowerAngleBound;
        double heightExtent = upperHeightBound - lowerHeightBound;
        double xDelta = angleExtent / destWidth;
        double yDelta = heightExtent / destHeight;
        double curAngle = lowerAngleBound;
        double curHeight = lowerHeightBound;
        // Fill every point in the noise map with the output values from the model.
        for (int y = 0; y < destHeight; y++) {
            curAngle = lowerAngleBound;
            for (int x = 0; x < destWidth; x++) {
                float curValue = (float) cylinderModel.getValue(curAngle,
                        curHeight);
                destNoiseMap.setValue(x, y, curValue);
                curAngle += xDelta;
            }
            curHeight += yDelta;
            if (callback != null) {
                callback.rowFinished(y);
            }
        }
    }

    /**
     * Returns the lower angle boundary of the cylindrical noise map.
     *
     * @returns The lower angle boundary of the noise map, in degrees.
     */
    public double getLowerAngleBound() {
        return lowerAngleBound;
    }

    /**
     * Returns the lower height boundary of the cylindrical noise map.
     *
     * @returns The lower height boundary of the noise map, in units.
     *
     * One unit is equal to the radius of the cylinder.
     */
    public double getLowerHeightBound() {
        return lowerHeightBound;
    }

    /**
     * Returns the upper angle boundary of the cylindrical noise map.
     *
     * @returns The upper angle boundary of the noise map, in degrees.
     */
    public double getUpperAngleBound() {
        return upperAngleBound;
    }

    /**
     * Returns the upper height boundary of the cylindrical noise map.
     *
     * @returns The upper height boundary of the noise map, in units.
     *
     * One unit is equal to the radius of the cylinder.
     */
    public double getUpperHeightBound() {
        return upperHeightBound;
    }

    /**
     * Sets the coordinate boundaries of the noise map.
     *
     * @param lowerAngleBound The lower angle boundary of the noise map, in
     * degrees.
     * @param upperAngleBound The upper angle boundary of the noise map, in
     * degrees.
     * @param lowerHeightBound The lower height boundary of the noise map, in
     * units.
     * @param upperHeightBound The upper height boundary of the noise map, in
     * units.
     *
     * @pre The lower angle boundary is less than the upper angle boundary.
     * @pre The lower height boundary is less than the upper height boundary.
     *
     * @throw noise::ExceptionInvalidParam See the preconditions.
     *
     * One unit is equal to the radius of the cylinder.
     */
    public void setBounds(double lowerAngleBound, double upperAngleBound,
            double lowerHeightBound, double upperHeightBound) {
        if (lowerAngleBound >= upperAngleBound
                || lowerHeightBound >= upperHeightBound) {
            throw new IllegalArgumentException();
        }

        this.lowerAngleBound = lowerAngleBound;
        this.upperAngleBound = upperAngleBound;
        this.lowerHeightBound = lowerHeightBound;
        this.upperHeightBound = upperHeightBound;
    }

}
