/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.util;

import com.matrixpeckham.libnoise.model.Plane;
import static com.matrixpeckham.libnoise.util.Globals.linearInterp;
import java.util.logging.Logger;

/**
 * Builds a planar noise map.
 *
 * This class builds a noise map by filling it with coherent-noise values
 * generated from the surface of a plane.
 *
 * This class describes these input values using (x, z) coordinates. Their y
 * coordinates are always 0.0.
 *
 * The application must provide the lower and upper x coordinate bounds of the
 * noise map, in units, and the lower and upper z coordinate bounds of the noise
 * map, in units.
 *
 * To make a tileable noise map with no seams at the edges, call the
 * EnableSeamless() method.
 */
public class NoiseMapBuilderPlane extends NoiseMapBuilder {

    /**
     * A flag specifying whether seamless tiling is enabled.
     */
    private boolean isSeamlessEnabled;

    /**
     * Lower x boundary of the planar noise map, in units.
     */
    private double lowerXBound;

    /**
     * Lower z boundary of the planar noise map, in units.
     */
    private double lowerZBound;

    /**
     * Upper x boundary of the planar noise map, in units.
     */
    private double upperXBound;

    /**
     * Upper z boundary of the planar noise map, in units.
     */
    private double upperZBound;

    /**
     * Constructor.
     */
    public NoiseMapBuilderPlane() {
        lowerXBound = 0;
        lowerZBound = 0;
        upperXBound = 0;
        upperZBound = 0;
        isSeamlessEnabled = true;
    }

    @Override
    public void build() {
        if (upperXBound <= lowerXBound
                || upperZBound <= lowerZBound
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
        Plane planeModel = new Plane();
        planeModel.useXY();
        planeModel.setModule(sourceModule);
        double xExtent = upperXBound - lowerXBound;
        double zExtent = upperZBound - lowerZBound;
        double xDelta = xExtent / destWidth;
        double zDelta = zExtent / destHeight;
        double zCur = lowerZBound;
        // Fill every point in the noise map with the output values from the model.
        for (int z = 0; z < destHeight; z++) {
            double xCur = lowerXBound;
            for (int x = 0; x < destWidth; x++) {
                double finalValue;
                if (!isSeamlessEnabled) {
                    finalValue = planeModel.getValue(xCur, zCur);
                } else {
                    double swValue, seValue, nwValue, neValue;
                    swValue = planeModel.getValue(xCur, zCur);
                    seValue = planeModel.getValue(xCur + xExtent, zCur);
                    nwValue = planeModel.getValue(xCur, zCur + zExtent);
                    neValue = planeModel.
                            getValue(xCur + xExtent, zCur + zExtent);
                    double xBlend = 1.0 - ((xCur - lowerXBound) / xExtent);
                    double zBlend = 1.0 - ((zCur - lowerZBound) / zExtent);
                    double z0 = linearInterp(swValue, seValue, xBlend);
                    double z1 = linearInterp(nwValue, neValue, xBlend);
                    finalValue = linearInterp(z0, z1, zBlend);
                }
                destNoiseMap.setValue(x, z, finalValue);
                xCur += xDelta;
            }
            zCur += zDelta;
            if (callback != null) {
                callback.rowFinished(z);
            }
        }
    }

    /**
     * Enables or disables seamless tiling.
     *
     * @param enable A flag that enables or disables seamless tiling.
     *
     * Enabling seamless tiling builds a noise map with no seams at the edges.
     * This allows the noise map to be tileable.
     */
    public void enableSeamless(boolean enable) {
        isSeamlessEnabled = enable;
    }

    void enableSeamless() {
        enableSeamless(true);
    }

    /**
     * Returns the lower x boundary of the planar noise map.
     *
     * @return The lower x boundary of the planar noise map, in units.
     */
    public double getLowerXBound() {
        return lowerXBound;
    }

    /**
     * Returns the lower z boundary of the planar noise map.
     *
     * @return The lower z boundary of the noise map, in units.
     */
    double getLowerZBound() {
        return lowerZBound;
    }

    /**
     * Returns the upper x boundary of the planar noise map.
     *
     * @return The upper x boundary of the noise map, in units.
     */
    public double getUpperXBound() {
        return upperXBound;
    }

    /**
     * Returns the upper z boundary of the planar noise map.
     *
     * @return The upper z boundary of the noise map, in units.
     */
    public double getUpperZBound() {
        return upperZBound;
    }

    /**
     * Determines if seamless tiling is enabled.
     *
     * @return - @a true if seamless tiling is enabled. - @a false if seamless
     * tiling is disabled.
     *
     * Enabling seamless tiling builds a noise map with no seams at the edges.
     * This allows the noise map to be tileable.
     */
    public boolean isSeamlessEnabled() {
        return isSeamlessEnabled;
    }

    /**
     * Sets the boundaries of the planar noise map.
     *
     * @param lowerXBound The lower x boundary of the noise map, in units.
     * @param upperXBound The upper x boundary of the noise map, in units.
     * @param lowerZBound The lower z boundary of the noise map, in units.
     * @param upperZBound The upper z boundary of the noise map, in units.
     *
     * @noise.pre The lower x boundary is less than the upper x boundary.
     * @noise.pre The lower z boundary is less than the upper z boundary.
     *
     * @throws IllegalArgumentException See the preconditions.
     */
    public void setBounds(double lowerXBound, double upperXBound,
            double lowerZBound, double upperZBound) {
        if (lowerXBound >= upperXBound
                || lowerZBound >= upperZBound) {
            throw new IllegalArgumentException();
        }

        this.lowerXBound = lowerXBound;
        this.upperXBound = upperXBound;
        this.lowerZBound = lowerZBound;
        this.upperZBound = upperZBound;
    }

    private static final Logger LOG
            = Logger.getLogger(NoiseMapBuilderPlane.class.getName());

}
