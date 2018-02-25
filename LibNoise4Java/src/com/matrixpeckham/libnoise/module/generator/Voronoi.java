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

package com.matrixpeckham.libnoise.module.generator;

import com.matrixpeckham.libnoise.module.AbstractModule;
import static com.matrixpeckham.libnoise.util.Globals.SQRT_3;
import static com.matrixpeckham.libnoise.util.Globals.valueNoise3D;
import static java.lang.Math.floor;
import java.util.logging.Logger;

/**
 * Noise module that outputs Voronoi cells.
 *
 * @image html modulevoronoi.png
 *
 * In mathematics, a <i>Voronoi cell</i> is a region containing all the points
 * that are closer to a specific <i>seed point</i> than to any other seed point.
 * These cells mesh with one another, producing polygon-like formations.
 *
 * By default, this noise module randomly places a seed point within each unit
 * cube. By modifying the <i>frequency</i> of the seed points, an application
 * can change the distance between seed points. The higher the frequency, the
 * closer together this noise module places the seed points, which reduces the
 * size of the cells. To specify the frequency of the cells, call the
 * SetFrequency() method.
 *
 * This noise module assigns each Voronoi cell with a random constant value from
 * a coherent-noise function. The <i>displacement value</i>
 * controls the range of random values to assign to each cell. The range of
 * random values is +/- the displacement value. Call the SetDisplacement()
 * method to specify the displacement value.
 *
 * To modify the random positions of the seed points, call the SetSeed() method.
 *
 * This noise module can optionally add the distance from the nearest seed to
 * the output value. To enable this feature, call the EnableDistance() method.
 * This causes the points in the Voronoi cells to increase in value the further
 * away that point is from the nearest seed point.
 *
 * Voronoi cells are often used to generate cracked-mud terrain formations or
 * crystal-like textures
 *
 * This noise module requires no source modules.
 */
public class Voronoi extends AbstractModule {

    /**
     * Interface this module uses to calculate the closest distance from points.
     */
    public static interface DistanceFunction {

        public double maxDist();

        public double distance(double dx, double dy, double dz);
    }

    /**
     * Enum of default implementations for distance functions.
     */
    public static enum Distances implements DistanceFunction {

        EUCLID( SQRT_3 ) {
            @Override
            public double distance(double dx, double dy, double dz) {
                return Math.sqrt( dx * dx + dy * dy + dz * dz );
            }
        },
        MANHAT( 3.0 ) {
            @Override
            public double distance(double dx, double dy, double dz) {
                return Math.abs( dx ) + Math.abs( dy ) + Math.abs( dz );
            }
        },
        CHEBYC( 1 ) {
            @Override
            public double distance(double dx, double dy, double dz) {
                return Math.max( Math.abs( dx ), Math.max( Math.abs( dy ),
                        Math.abs( dz ) ) );
            }
        },
        SQUARE( 3 ) {
            @Override
            public double distance(double dx, double dy, double dz) {
                return dx * dx + dy * dy + dz * dz;
            }
        },
        QUADRA( 9.0 ) {
            @Override
            public double distance(double dx, double dy, double dz) {
                return dx * dx + 2 * dx * dy + 2 * dx * dz + 2 * dy * dz
                        + dy * dy + dz * dz;
            }
        };

        Distances(double max) {
            this.max = max;
        }

        @Override
        public double maxDist() {
            return this.max;
        }
        private final double max;
    }
    /**
     * default displacement to apply to each cell
     */
    public static final double DEFAULT_VORONOI_DISPLACEMENT = 1.0;
    /**
     * default distance calculation.
     */
    public static final DistanceFunction DEFAULT_VORONOI_DISTANCE =
            Distances.EUCLID;
    /**
     * default frequency of the seed points
     */
    public static final double DEFAULT_VORONOI_FREQUENCY = 1.0;
    /**
     * default seed of the noise function
     */
    public static final int DEFAULT_VORONOI_SEED = 0;
    /**
     * scale of the random displacement to apply to each cell
     */
    protected double displacement;
    /**
     * distance function to use for this instance;
     */
    protected DistanceFunction distFunc;
    /**
     * determines if the distance from the seed point is applied to the output
     * value
     */
    boolean enableDistance;
    /**
     * frequency of the seed points
     */
    double frequency;
    /**
     * seed value used by the noise function to determine the position of the
     * seed points
     */
    int seed;

    /**
     * constructor
     */
    public Voronoi() {
        displacement = DEFAULT_VORONOI_DISPLACEMENT;
        enableDistance = false;
        distFunc = DEFAULT_VORONOI_DISTANCE;
        frequency = DEFAULT_VORONOI_FREQUENCY;
        seed = DEFAULT_VORONOI_SEED;
    }

    /**
     * getter
     *
     * @return
     */
    public DistanceFunction getDistFunc() {
        return distFunc;
    }

    /**
     * setter
     *
     * @param distFunc
     */
    public void setDistFunc(DistanceFunction distFunc) {
        this.distFunc = distFunc;
    }

    /**
     * getter
     *
     * @return
     */
    public boolean isEnableDistance() {
        return enableDistance;
    }

    /**
     * setter
     *
     * @param enableDistance
     */
    public void setEnableDistance(boolean enableDistance) {
        this.enableDistance = enableDistance;
    }

    /**
     * Enables or disables applying the distance from the nearest seed point to
     * the output value.
     *
     * Applying the distance from the nearest seed point to the output value
     * causes the points in the Voronoi cells to increase in value the further
     * away that point is from the nearest seed point. Setting this value to
     *
     * @a
     * true (and setting the displacement to a near-zero value) causes this
     * noise module to generate cracked mud formations.
     */
    public void enableDistance() {
        enableDistance( true );
    }

    /**
     * Enables or disables applying the distance from the nearest seed point to
     * the output value.
     *
     * @param enable Specifies whether to apply the distance to the output value
     * or not.
     *
     * Applying the distance from the nearest seed point to the output value
     * causes the points in the Voronoi cells to increase in value the further
     * away that point is from the nearest seed point. Setting this value to
     *
     * @a
     * true (and setting the displacement to a near-zero value) causes this
     * noise module to generate cracked mud formations.
     */
    public void enableDistance(boolean enable) {
        enableDistance = enable;
    }

    /**
     * Returns the displacement value of the Voronoi cells.
     *
     * @return The displacement value of the Voronoi cells.
     *
     * This noise module assigns each Voronoi cell with a random constant value
     * from a coherent-noise function. The <i>displacement value</i> controls
     * the range of random values to assign to each cell. The range of random
     * values is +/- the displacement value.
     */
    public double getDisplacement() {
        return displacement;
    }

    /**
     * Returns the frequency of the seed points.
     *
     * @return The frequency of the seed points.
     *
     * The frequency determines the size of the Voronoi cells and the distance
     * between these cells.
     */
    public double getFrequency() {
        return frequency;
    }

    /**
     * Returns the seed value used by the Voronoi cells
     *
     * @return The seed value.
     *
     * The positions of the seed values are calculated by a coherent-noise
     * function. By modifying the seed value, the output of that function
     * changes.
     */
    public int getSeed() {
        return seed;
    }

    @Override
    public int getSourceModuleCount() {
        return 0;
    }

    @Override
    public double getValue(double x, double y, double z) {
        //this method could be more efficient by caching the seed values.
        //fix later
        x *= frequency;
        y *= frequency;
        z *= frequency;
        int xInt = (x > 0.0 ? (int) x : (int) x - 1);
        int yInt = (y > 0.0 ? (int) y : (int) y - 1);
        int zInt = (z > 0.0 ? (int) z : (int) z - 1);
        double minDist = 2147483647.0;
        double xCandidate = 0;
        double yCandidate = 0;
        double zCandidate = 0;
        //inside each unit cube, there is a seed point at a random position. go
        //through each of the nearby cubes until we find a cube with a seed point
        //that is closest to the specified position
        for (int zCur = zInt - 2; zCur <= zInt + 2;
                zCur++) {
            for (int yCur = yInt - 2; yCur <= yInt + 2;
                    yCur++) {
                for (int xCur = xInt - 2; xCur <= xInt + 2;
                        xCur++) {
                    //calculate the position and distance to the seed point inside of
                    //this unit cube
                    double xPos = xCur + valueNoise3D( xCur, yCur, zCur, seed );
                    double yPos = yCur + valueNoise3D( xCur, yCur, zCur, seed
                            + 1 );
                    double zPos = zCur + valueNoise3D( xCur, yCur, zCur, seed
                            + 2 );
                    double xDist = xPos - x;
                    double yDist = yPos - y;
                    double zDist = zPos - z;
                    double dist = distFunc.distance( xDist, yDist, zDist );
                    if (dist < minDist) {
                        //this seed point is closer to any other found so far so
                        //record the point
                        minDist = dist;
                        xCandidate = xPos;
                        yCandidate = yPos;
                        zCandidate = zPos;
                    }
                }
            }
        }
        double value;
        if (enableDistance) {
            //determine the distance to the nearest seed point
            double xDist = xCandidate - x;
            double yDist = yCandidate - y;
            double zDist = zCandidate - z;
            value = (distFunc.distance( xDist, yDist, zDist ) / distFunc.
                    maxDist()
                    * 2) - 1;
        } else {
            value = 0;
        }
        //return the calculated distance with the displacement value applied
        return value + (displacement * valueNoise3D( (int) floor( xCandidate ),
                (int) floor( yCandidate ),
                (int) floor( zCandidate ) ));
    }

    /**
     * Determines if the distance from the nearest seed point is applied to the
     * output value.
     *
     * @return
     *
     * -
     *
     * @a true if the distance is applied to the output value.
     *
     * -
     * @a false if not.
     *
     * Applying the distance from the nearest seed point to the output value
     * causes the points in the Voronoi cells to increase in value the further
     * away that point is from the nearest seed point.
     */
    public boolean isDistanceEnabled() {
        return enableDistance;
    }

    /**
     * Sets the displacement value of the Voronoi cells.
     *
     * @param displacement The displacement value of the Voronoi cells.
     *
     * This noise module assigns each Voronoi cell with a random constant value
     * from a coherent-noise function. The <i>displacement value</i> controls
     * the range of random values to assign to each cell. The range of random
     * values is +/- the displacement value.
     */
    public void setDisplacement(double displacement) {
        this.displacement = displacement;
    }

    /**
     * sets the frequency of seed points
     *
     * @param frequency the frequency determines the size of the voronoi cells
     * and the distance between them
     */
    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    /**
     * sets the seed
     *
     * @param seed the positions of cell values are calculated by a coherent
     * noise function. by modifying the seed value. the output of that function
     * changes
     */
    public void setSeed(int seed) {
        this.seed = seed;
    }
    private static final Logger LOG = Logger.
            getLogger( Voronoi.class.getName() );
}
