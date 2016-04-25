/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.util;

import static com.matrixpeckham.libnoise.util.Globals.DEG_TO_RAD;
import static com.matrixpeckham.libnoise.util.Globals.SQRT_2;
import static com.matrixpeckham.libnoise.util.Globals.getMax;
import static com.matrixpeckham.libnoise.util.Globals.linearInterp;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class RendererImage {

    /**
     * A pointer to the background image.
     */
    private Image backgroundImage;

    /**
     * The cosine of the azimuth of the light source.
     */
    private double cosAzimuth;

    /**
     * The cosine of the elevation of the light source.
     */
    private double cosElev;

    /**
     * A pointer to the destination image.
     */
    Image destImage;

    /**
     * The color gradient used to specify the image colors.
     */
    private final GradientColor gradient;

    /**
     * A flag specifying whether lighting is enabled.
     */
    private boolean isLightEnabled;

    /**
     * A flag specifying whether wrapping is enabled.
     */
    private boolean isWrapEnabled;

    /**
     * The azimuth of the light source, in degrees.
     */
    private double lightAzimuth;

    /**
     * The brightness of the light source.
     */
    private double lightBrightness;

    /**
     * The color of the light source.
     */
    private Color lightColor;

    /**
     * The contrast between areas in light and areas in shadow.
     */
    private double lightContrast;

    /**
     * The elevation of the light source, in degrees.
     */
    private double lightElev;

    /**
     * The intensity of the light source.
     */
    private double lightIntensity;

    /**
     * Used by the CalcLightIntensity() method to recalculate the light values
     * only if the light parameters change.
     *
     * When the light parameters change, this value is set to True. When the
     * CalcLightIntensity() method is called, this value is set to false.
     */
    private boolean recalcLightValues;

    /**
     * The sine of the azimuth of the light source.
     */
    private double sinAzimuth;

    /**
     * The sine of the elevation of the light source.
     */
    private double sinElev;

    /**
     * A pointer to the source noise map.
     */
    private NoiseMap sourceNoiseMap;

    /**
     * Constructor.
     */
    public RendererImage() {
        isLightEnabled = false;
        isWrapEnabled = false;
        lightAzimuth = 45;
        lightBrightness = 1;
        lightColor = new Color((short) 255, (short) 255, (short) 255,
                (short) 255);
        lightContrast = 1;
        lightElev = 45;
        lightIntensity = 1;
        backgroundImage = null;
        destImage = null;
        sourceNoiseMap = null;
        recalcLightValues = true;
        gradient = new GradientColor();
        buildGrayscaleGradient();
    }

    /**
     * Sets the elevation of the light source, in degrees.
     *
     * @param lightElev The elevation of the light source.
     *
     * The elevation is the angle above the horizon: - 0 degrees is on the
     * horizon. - 90 degrees is straight up.
     *
     * Make sure the light source is enabled via a call to the EnableLight()
     * method before calling the Render() method.
     */
    public void setLightElev(double lightElev) {
        this.lightElev = lightElev;
        recalcLightValues = true;
    }

    /**
     * Adds a gradient point to this gradient object.
     *
     * @param gradientPos The position of this gradient point.
     * @param gradientColor The color of this gradient point.
     *
     * @pre No two gradient points have the same position.
     *
     * @throw noise::ExceptionInvalidParam See the preconditions.
     *
     * This object uses a color gradient to calculate the color for each pixel
     * in the destination image according to the value from the corresponding
     * position in the noise map.
     *
     * The gradient requires a minimum of two gradient points.
     *
     * The specified color value passed to this method has an alpha channel.
     * This alpha channel specifies how a pixel in the background image (if
     * specified) is blended with the calculated color. If the alpha value is
     * high, this object weighs the blend towards the calculated color, and if
     * the alpha value is low, this object weighs the blend towards the color
     * from the corresponding pixel in the background image.
     */
    public void addGradientPoint(double gradientPos,
            Color gradientColor) {
        gradient.addGradientPoint(gradientPos, gradientColor);
    }

    /**
     * Builds a grayscale gradient.
     *
     * @post The original gradient is cleared and a grayscale gradient is
     * created.
     *
     * This color gradient contains the following gradient points: - -1.0 maps
     * to black - 1.0 maps to white
     */
    public void buildGrayscaleGradient() {
        clearGradient();
        gradient.addGradientPoint(-1, new Color((short) 0, (short) 0, (short) 0,
                (short) 255));
        gradient.addGradientPoint(1, new Color((short) 255, (short) 255,
                (short) 255, (short) 255));
    }

    /**
     * Builds a color gradient suitable for terrain.
     *
     * @post The original gradient is cleared and a terrain gradient is created.
     *
     * This gradient color at position 0.0 is the "sea level". Above that value,
     * the gradient contains greens, browns, and whites. Below that value, the
     * gradient contains various shades of blue.
     */
    public void buildTerrainGradient() {
        clearGradient();
        gradient.addGradientPoint(-1, new Color((short) 0, (short) 0,
                (short) 128, (short) 255));
        gradient.addGradientPoint(-0.2, new Color((short) 32, (short) 64,
                (short) 128, (short) 255));
        gradient.addGradientPoint(-0.04, new Color((short) 64, (short) 96,
                (short) 192, (short) 255));
        gradient.addGradientPoint(-0.02, new Color((short) 192, (short) 192,
                (short) 128, (short) 255));
        gradient.addGradientPoint(0.0, new Color((short) 0, (short) 192,
                (short) 0, (short) 255));
        gradient.addGradientPoint(0.25, new Color((short) 192, (short) 192,
                (short) 0, (short) 255));
        gradient.addGradientPoint(0.5, new Color((short) 160, (short) 96,
                (short) 64, (short) 255));
        gradient.addGradientPoint(0.75, new Color((short) 128, (short) 255,
                (short) 255, (short) 255));
        gradient.addGradientPoint(1, new Color((short) 255, (short) 255,
                (short) 255, (short) 255));
    }

    /**
     * Calculates the destination color.
     *
     * @param sourceColor The source color generated from the color gradient.
     * @param backgroundColor The color from the background image at the
     * corresponding position.
     * @param lightValue The intensity of the light at that position.
     *
     * @returns The destination color.
     */
    private Color calcDestColor(
            Color sourceColor,
            Color backgroundColor, double lightValue) {
        double sourceRed = sourceColor.red / 255.0;
        double sourceGreen = sourceColor.green / 255.0;
        double sourceBlue = sourceColor.blue / 255.0;
        double sourceAlpha = sourceColor.alpha / 255.0;
        double backgroundRed = backgroundColor.red / 255.0;
        double backgroundGreen = backgroundColor.green / 255.0;
        double backgroundBlue = backgroundColor.blue / 255.0;
        //First blend the source color to the background color using the alpha
        //of the source color
        double red = linearInterp(backgroundRed, sourceRed, sourceAlpha);
        double green = linearInterp(backgroundGreen, sourceGreen, sourceAlpha);
        double blue = linearInterp(backgroundBlue, sourceBlue, sourceAlpha);
        if (isLightEnabled) {
            //calculate light color
            double lightRed = lightValue * (lightColor.red / 255.0);
            double lightGreen = lightValue * (lightColor.green / 255.0);
            double lightBlue = lightValue * (lightColor.blue / 255.0);
            //apply light color
            red *= lightRed;
            green *= lightGreen;
            blue *= lightBlue;
        }
        // Clamp the color channels to the (0..1) range.
        red = (red < 0.0) ? 0.0 : red;
        red = (red > 1.0) ? 1.0 : red;
        green = (green < 0.0) ? 0.0 : green;
        green = (green > 1.0) ? 1.0 : green;
        blue = (blue < 0.0) ? 0.0 : blue;
        blue = (blue > 1.0) ? 1.0 : blue;
        //rescale color channels back to 0-255
        Color nColor
                = new Color((short) (((int) (red * 255.0)) & 0xFF),
                        (short) (((int) (green * 255.0)) & 0xFF),
                        (short) (((int) (blue * 255.0)) & 0xFF),
                        getMax(sourceColor.alpha, backgroundColor.alpha));
        return nColor;
    }

    /**
     * Calculates the intensity of the light given some elevation values.
     *
     * @param center Elevation of the center point.
     * @param left Elevation of the point directly left of the center point.
     * @param right Elevation of the point directly right of the center point.
     * @param down Elevation of the point directly below the center point.
     * @param up Elevation of the point directly above the center point.
     *
     * These values come directly from the noise map.
     */
    private double calcLightIntensity(double center, double left, double right,
            double down, double up) {
        //recalculate the sine and cosine of the various light values if
        //necessary so it doesn't need to be calculated every call
        if (recalcLightValues) {
            cosAzimuth = cos(lightAzimuth * DEG_TO_RAD);
            sinAzimuth = sin(lightAzimuth * DEG_TO_RAD);
            cosElev = cos(lightElev * DEG_TO_RAD);
            sinElev = sin(lightElev * DEG_TO_RAD);
            recalcLightValues = false;
        }
        //light calculations
        /////////////////////
        //I may be wrong but this seems to be calculating the dot product of
        //the light direction and the approximate "normal" of the "height field"
        //represented by the noise. in future I intend to add analytic normals
        //to this library and then that will be able to be used instead of this.
        /////////////////////
        final double I_MAX = 1;
        double io = I_MAX * SQRT_2 * sinElev / 2;
        double ix = (I_MAX - io) * lightContrast * SQRT_2 * cosElev * cosAzimuth;
        double iy = (I_MAX - io) * lightContrast * SQRT_2 * cosElev * sinAzimuth;
        double intensity = (ix * (left - right) + iy * (down - up) + io);
        if (intensity < 0) {
            intensity = 0;
        }
        return intensity;
    }

    /**
     * Clears the color gradient.
     *
     * Before calling the Render() method, the application must specify a new
     * color gradient with at least two gradient points.
     */
    public void clearGradient() {
        gradient.clear();
    }

    /**
     * Enables or disables the light source.
     *
     * @param enable A flag that enables or disables the light source.
     *
     * If the light source is enabled, this object will interpret the noise map
     * as a bump map.
     */
    public void enableLight(boolean enable) {
        isLightEnabled = enable;
    }

    public void enableLight() {
        enableLight(true);
    }

    /**
     * Enables or disables noise-map wrapping.
     *
     * @param enable A flag that enables or disables noise-map wrapping.
     *
     * This object requires five points (the initial point and its four
     * neighbors) to calculate light shading. If wrapping is enabled, and the
     * initial point is on the edge of the noise map, the appropriate neighbors
     * that lie outside of the noise map will "wrap" to the opposite side(s) of
     * the noise map. Otherwise, the appropriate neighbors are cropped to the
     * edge of the noise map.
     *
     * Enabling wrapping is useful when creating spherical renderings and
     * tileable textures.
     */
    public void enableWrap(boolean enable) {
        isWrapEnabled = enable;
    }

    public void enableWrap() {
        enableWrap(true);
    }

    /**
     * Returns the azimuth of the light source, in degrees.
     *
     * @returns The azimuth of the light source.
     *
     * The azimuth is the location of the light source around the horizon: - 0.0
     * degrees is east. - 90.0 degrees is north. - 180.0 degrees is west. -
     * 270.0 degrees is south.
     */
    public double getLightAzimuth() {
        return lightAzimuth;
    }

    /**
     * Returns the brightness of the light source.
     *
     * @returns The brightness of the light source.
     */
    public double getLightBrightness() {
        return lightBrightness;
    }

    /**
     * Returns the color of the light source.
     *
     * @returns The color of the light source.
     */
    public Color getLightColor() {
        return lightColor;
    }

    /**
     * Returns the contrast of the light source.
     *
     * @returns The contrast of the light source.
     *
     * The contrast specifies how sharp the boundary is between the light-facing
     * areas and the shadowed areas.
     *
     * The contrast determines the difference between areas in light and areas
     * in shadow. Determining the correct contrast amount requires some trial
     * and error, but if your application interprets the noise map as a height
     * map that has a spatial resolution of @a h meters and an elevation
     * resolution of 1 meter, a good contrast amount to use is ( 1.0 / @a h ).
     */
    public double getLightContrast() {
        return lightContrast;
    }

    /**
     * Returns the elevation of the light source, in degrees.
     *
     * @returns The elevation of the light source.
     *
     * The elevation is the angle above the horizon: - 0 degrees is on the
     * horizon. - 90 degrees is straight up.
     */
    public double getLightElev() {
        return lightElev;
    }

    /**
     * Returns the intensity of the light source.
     *
     * @returns The intensity of the light source.
     */
    public double getLightIntensity() {
        return lightIntensity;
    }

    /**
     * Determines if the light source is enabled.
     *
     * @returns - @a true if the light source is enabled. - @a false if the
     * light source is disabled.
     */
    boolean isLightEnabled() {
        return isLightEnabled;
    }

    /**
     * Determines if noise-map wrapping is enabled.
     *
     * @returns - @a true if noise-map wrapping is enabled. - @a false if
     * noise-map wrapping is disabled.
     *
     * This object requires five points (the initial point and its four
     * neighbors) to calculate light shading. If wrapping is enabled, and the
     * initial point is on the edge of the noise map, the appropriate neighbors
     * that lie outside of the noise map will "wrap" to the opposite side(s) of
     * the noise map. Otherwise, the appropriate neighbors are cropped to the
     * edge of the noise map.
     *
     * Enabling wrapping is useful when creating spherical renderings and
     * tileable textures
     */
    boolean isWrapEnabled() {
        return isWrapEnabled;
    }

    /**
     * Renders the destination image using the contents of the source noise map
     * and an optional background image.
     *
     * @pre SetSourceNoiseMap() has been previously called.
     * @pre SetDestImage() has been previously called.
     * @pre There are at least two gradient points in the color gradient.
     * @pre No two gradient points have the same position.
     * @pre If a background image was specified, it has the exact same size as
     * the source height map.
     *
     * @post The original contents of the destination image is destroyed.
     *
     * @throw noise::ExceptionInvalidParam See the preconditions.
     *
     * The background image and the destination image can safely refer to the
     * same image, although in this case, the destination image is irretrievably
     * blended into the background image.
     */
    public void render() {
        if (sourceNoiseMap == null
                || destImage == null
                || sourceNoiseMap.getWidth() <= 0
                || sourceNoiseMap.getHeight() <= 0
                || gradient.getGradientPointCount() < 2) {
            throw new IllegalArgumentException();
        }

        int width = sourceNoiseMap.getWidth();
        int height = sourceNoiseMap.getHeight();

        // If a background image was provided, make sure it is the same size the
        // source noise map.
        if (backgroundImage != null) {
            if (backgroundImage.getWidth() != width
                    || backgroundImage.getHeight() != height) {
                throw new IllegalArgumentException();
            }
        }

        // Create the destination image.  It is safe to reuse it if this is also the
        // background image.
        if (destImage != backgroundImage) {
            destImage.setSize(width, height);
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                // Get the color based on the value at the current point in the noise
                // map.
                Color destColor = gradient.getColor(sourceNoiseMap.
                        getValue(x, y));

                // If lighting is enabled, calculate the light intensity based on the
                // rate of change at the current point in the noise map.
                double locLightIntensity;
                if (isLightEnabled) {

                    // Calculate the positions of the current point's four-neighbors.
                    int xLeftOffset, xRightOffset;
                    int yUpOffset, yDownOffset;
                    if (isWrapEnabled) {
                        if (x == 0) {
                            xLeftOffset = width - 1;
                            xRightOffset = 1;
                        } else if (x == width - 1) {
                            xLeftOffset = -1;
                            xRightOffset = -(width - 1);
                        } else {
                            xLeftOffset = -1;
                            xRightOffset = 1;
                        }
                        if (y == 0) {
                            yDownOffset = height - 1;
                            yUpOffset = 1;

                        } else if (y == height - 1) {
                            yDownOffset = -1;
                            yUpOffset = -(height - 1);
                        } else {
                            yDownOffset = -1;
                            yUpOffset = 1;
                        }
                    } else {
                        if (x == 0) {
                            xLeftOffset = 0;
                            xRightOffset = 1;
                        } else if (x == width - 1) {
                            xLeftOffset = -1;
                            xRightOffset = 0;
                        } else {
                            xLeftOffset = -1;
                            xRightOffset = 1;
                        }
                        if (y == 0) {
                            yDownOffset = 0;
                            yUpOffset = 1;

                        } else if (y == height - 1) {
                            yDownOffset = -1;
                            yUpOffset = 0;
                        } else {
                            yDownOffset = -1;
                            yUpOffset = 1;
                        }
                    }

                    // Get the noise value of the current point in the source noise map
                    // and the noise values of its four-neighbors.
                    double nc = sourceNoiseMap.getValue(x, y);
                    double nl = sourceNoiseMap.getValue(x + xLeftOffset, y);
                    double nr = sourceNoiseMap.getValue(x + xRightOffset, y);
                    double nd = sourceNoiseMap.getValue(x, y + yDownOffset);
                    double nu = sourceNoiseMap.getValue(x, y + yUpOffset);

                    // Now we can calculate the lighting intensity.
                    locLightIntensity = calcLightIntensity(nc, nl, nr, nd, nu);
                    locLightIntensity *= lightBrightness;

                } else {

                    // These values will apply no lighting to the destination image.
                    locLightIntensity = 1.0;
                }

                // Get the current background color from the background image.
                Color backgroundColor = new Color((short) 255, (short) 255,
                        (short) 255, (short) 255);
                if (backgroundImage != null) {
                    backgroundColor = backgroundImage.getValue(x, y);
                }

                // Blend the destination color, background color, and the light
                // intensity together, then update the destination image with that
                // color.
                destImage.setValue(x, y, calcDestColor(destColor,
                        backgroundColor,
                        locLightIntensity));

            }
        }
    }

    /**
     * Sets the background image.
     *
     * @param backgroundImage The background image.
     *
     * If a background image has been specified, the Render() method blends the
     * pixels from the background image onto the corresponding pixels in the
     * destination image. The blending weights are determined by the alpha
     * channel in the pixels in the destination image.
     *
     * The destination image must exist throughout the lifetime of this object
     * unless another image replaces that image.
     */
    public void setBackgroundImage(
            Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    /**
     * Sets the destination image.
     *
     * @param destImage The destination image.
     *
     * The destination image will contain the rendered image after a successful
     * call to the Render() method.
     *
     * The destination image must exist throughout the lifetime of this object
     * unless another image replaces that image.
     */
    public void setDestImage(Image destImage) {
        this.destImage = destImage;
    }

    /**
     * Sets the azimuth of the light source, in degrees.
     *
     * @param lightAzimuth The azimuth of the light source.
     *
     * The azimuth is the location of the light source around the horizon: - 0.0
     * degrees is east. - 90.0 degrees is north. - 180.0 degrees is west. -
     * 270.0 degrees is south.
     *
     * Make sure the light source is enabled via a call to the EnableLight()
     * method before calling the Render() method.
     */
    public void setLightAzimuth(double lightAzimuth) {
        this.lightAzimuth = lightAzimuth;
        recalcLightValues = true;
    }

    /**
     * Sets the brightness of the light source.
     *
     * @param lightBrightness The brightness of the light source.
     *
     * Make sure the light source is enabled via a call to the EnableLight()
     * method before calling the Render() method.
     */
    public void setLightBrightness(double lightBrightness) {
        this.lightBrightness = lightBrightness;
        recalcLightValues = true;
    }

    /**
     * Sets the color of the light source.
     *
     * @param lightColor The light color.
     *
     * Make sure the light source is enabled via a call to the EnableLight()
     * method before calling the Render() method.
     */
    public void setLightColor(Color lightColor) {
        this.lightColor = lightColor;
    }

    /**
     * Sets the contrast of the light source.
     *
     * @param lightContrast The contrast of the light source.
     *
     * @pre The specified light contrast is positive.
     *
     * @throw noise::ExceptionInvalidParam See the preconditions.
     *
     * The contrast specifies how sharp the boundary is between the light-facing
     * areas and the shadowed areas.
     *
     * The contrast determines the difference between areas in light and areas
     * in shadow. Determining the correct contrast amount requires some trial
     * and error, but if your application interprets the noise map as a height
     * map that has a spatial resolution of @a h meters and an elevation
     * resolution of 1 meter, a good contrast amount to use is ( 1.0 / @a h ).
     *
     * Make sure the light source is enabled via a call to the EnableLight()
     * method before calling the Render() method.
     */
    public void setLightContrast(double lightContrast) {
        if (lightContrast <= 0.0) {
            throw new IllegalArgumentException();
        }

        this.lightContrast = lightContrast;
        recalcLightValues = true;
    }

    /**
     * Returns the intensity of the light source.
     *
     * @returns The intensity of the light source.
     *
     * A good value for intensity is 2.0.
     *
     * Make sure the light source is enabled via a call to the EnableLight()
     * method before calling the Render() method.
     */
    public void setLightIntensity(double lightIntensity) {
        if (lightIntensity < 0.0) {
            throw new IllegalArgumentException();
        }

        this.lightIntensity = lightIntensity;
        recalcLightValues = true;
    }

    /**
     * Sets the source noise map.
     *
     * @param sourceNoiseMap The source noise map.
     *
     * The destination image must exist throughout the lifetime of this object
     * unless another image replaces that image.
     */
    public void setSourceNoiseMap(
            NoiseMap sourceNoiseMap) {
        this.sourceNoiseMap = sourceNoiseMap;
    }

};
