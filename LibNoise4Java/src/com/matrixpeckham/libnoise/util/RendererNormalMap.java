/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.util;

import static java.lang.Math.floor;
import static java.lang.Math.sqrt;


public class RendererNormalMap {

    /**
     * The bump height for the normal map.
     */
    private double bumpHeight;

    /**
     * A pointer to the destination image.
     */
    private Image destImage;

    /**
     * A flag specifying whether wrapping is enabled.
     */
    private boolean isWrapEnabled;

    /**
     * A pointer to the source noise map.
     */
    private NoiseMap sourceNoiseMap;

    /**
     * Constructor.
     */
    public RendererNormalMap() {
        bumpHeight = 1;
        isWrapEnabled = false;
        destImage = null;
        sourceNoiseMap = null;
    }

    /**
     * Calculates the normal vector at a given point on the noise map.
     *
     * @param nc The height of the given point in the noise map.
     * @param nr The height of the left neighbor.
     * @param nu The height of the up neighbor.
     * @param bumpHeight The bump height.
     *
     * @returns The normal vector represented as a color.
     *
     * This method encodes the (x, y, z) components of the normal vector into
     * the (red, green, blue) channels of the returned color. In order to
     * represent the vector as a color, each coordinate of the normal is mapped
     * from the -1.0 to 1.0 range to the 0 to 255 range.
     *
     * The bump height specifies the ratio of spatial resolution to elevation
     * resolution. For example, if your noise map has a spatial resolution of 30
     * meters and an elevation resolution of one meter, set the bump height to
     * 1.0 / 30.0.
     *
     * The spatial resolution and elevation resolution are determined by the
     * application.
     */
    private Color calcNormalColor(double nc, double nr, double nu, double bumpHeight) {
        // Calculate the surface normal.
        nc *= bumpHeight;
        nr *= bumpHeight;
        nu *= bumpHeight;
        double ncr = (nc - nr);
        double ncu = (nc - nu);
        double d = sqrt((ncu * ncu) + (ncr * ncr) + 1);
        double vxc = (nc - nr) / d;
        double vyc = (nc - nu) / d;
        double vzc = 1.0 / d;
        short xc, yc, zc;
        xc = (short) ((int) (floor((vxc + 1.0) * 127.5)) & 0xff);
        yc = (short) ((int) (floor((vyc + 1.0) * 127.5)) & 0xff);
        zc = (short) ((int) (floor((vzc + 1.0) * 127.5)) & 0xff);
        return new Color(xc, yc, zc, (short) 0);
    }

    /**
     * Enables or disables noise-map wrapping.
     *
     * @param enable A flag that enables or disables noise-map wrapping.
     *
     * This object requires three points (the initial point and the right and up
     * neighbors) to calculate the normal vector at that point. If wrapping is/
     * enabled, and the initial point is on the edge of the noise map, the
     * appropriate neighbors that lie outside of the noise map will "wrap" to
     * the opposite side(s) of the noise map. Otherwise, the appropriate
     * neighbors are cropped to the edge of the noise map.
     *
     * Enabling wrapping is useful when creating spherical and tileable normal
     * maps.
     */
    public void enableWrap(boolean enable) {
        isWrapEnabled = enable;
    }

    public void enableWrap() {
        enableWrap(true);
    }

    /**
     * Returns the bump height.
     *
     * @returns The bump height.
     *
     * The bump height specifies the ratio of spatial resolution to elevation
     * resolution. For example, if your noise map has a spatial resolution of 30
     * meters and an elevation resolution of one meter, set the bump height to
     * 1.0 / 30.0.
     *
     * The spatial resolution and elevation resolution are determined by the
     * application.
     */
    public double getBumpHeight() {
        return bumpHeight;
    }

    /**
     * Determines if noise-map wrapping is enabled.
     *
     * @returns - @a true if noise-map wrapping is enabled. - @a false if
     * noise-map wrapping is disabled.
     *
     * This object requires three points (the initial point and the right and up
     * neighbors) to calculate the normal vector at that point. If wrapping is/
     * enabled, and the initial point is on the edge of the noise map, the
     * appropriate neighbors that lie outside of the noise map will "wrap" to
     * the opposite side(s) of the noise map. Otherwise, the appropriate
     * neighbors are cropped to the edge of the noise map.
     *
     * Enabling wrapping is useful when creating spherical and tileable normal
     * maps.
     */
    boolean isWrapEnabled() {
        return isWrapEnabled;
    }

    /**
     * Renders the noise map to the destination image.
     *
     * @pre SetSourceNoiseMap() has been previously called.
     * @pre SetDestImage() has been previously called.
     *
     * @post The original contents of the destination image is destroyed.
     *
     * @throw noise::ExceptionInvalidParam See the preconditions.
     */
    public void render() {
        if (sourceNoiseMap == null
                || destImage == null
                || sourceNoiseMap.getWidth() <= 0
                || sourceNoiseMap.getHeight() <= 0) {
            throw new IllegalArgumentException();
        }

        int width = sourceNoiseMap.getWidth();
        int height = sourceNoiseMap.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                // Calculate the positions of the current point's right and up
                // neighbors.
                int xRightOffset, yUpOffset;
                if (isWrapEnabled) {
                    if (x == width - 1) {
                        xRightOffset = -(width - 1);
                    } else {
                        xRightOffset = 1;
                    }
                    if (y == height - 1) {
                        yUpOffset = -(height - 1);
                    } else {
                        yUpOffset = 1;
                    }
                } else {
                    if (x == width - 1) {
                        xRightOffset = 0;
                    } else {
                        xRightOffset = 1;
                    }
                    if (y == height - 1) {
                        yUpOffset = 0;
                    } else {
                        yUpOffset = 1;
                    }
                }

                // Get the noise value of the current point in the source noise map
                // and the noise values of its right and up neighbors.
                double nc = sourceNoiseMap.getValue(x, y);
                double nr = sourceNoiseMap.getValue(x + xRightOffset, y);
                double nu = sourceNoiseMap.getValue(x, y + yUpOffset);

                // Calculate the normal product.
                destImage.
                        setValue(x, y, calcNormalColor(nc, nr, nu, bumpHeight));

            }
        }
    }

    /**
     * Sets the bump height.
     *
     * @param bumpHeight The bump height.
     *
     * The bump height specifies the ratio of spatial resolution to elevation
     * resolution. For example, if your noise map has a spatial resolution of 30
     * meters and an elevation resolution of one meter, set the bump height to
     * 1.0 / 30.0.
     *
     * The spatial resolution and elevation resolution are determined by the
     * application.
     */
    public void setBumpHeight(double bumpHeight) {
        this.bumpHeight = bumpHeight;
    }

    /**
     * Sets the destination image.
     *
     * @param destImage The destination image.
     *
     * The destination image will contain the normal map after a successful call
     * to the Render() method.
     *
     * The destination image must exist throughout the lifetime of this object
     * unless another image replaces that image.
     */
    public void setDestImage(Image destImage) {
        this.destImage = destImage;
    }

    /**
     * Sets the source noise map.
     *
     * @param sourceNoiseMap The source noise map.
     *
     * The destination image must exist throughout the lifetime of this object
     * unless another image replaces that image.
     */
    public void setSourceNoiseMap(NoiseMap sourceNoiseMap) {
        this.sourceNoiseMap = sourceNoiseMap;
    }


}
