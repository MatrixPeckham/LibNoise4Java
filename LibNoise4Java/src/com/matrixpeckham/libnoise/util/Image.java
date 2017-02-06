/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.util;

import static com.matrixpeckham.libnoise.util.Globals.RASTER_MAX_HEIGHT;
import static com.matrixpeckham.libnoise.util.Globals.RASTER_MAX_WIDTH;
import java.awt.image.BufferedImage;
import static java.lang.System.arraycopy;
import java.util.logging.Logger;

/**
 * Implements an image, a 2-dimensional array of color values.
 *
 * An image can be used to store a color texture.
 *
 * These color values are of type Color.
 *
 * The size (width and height) of the image can be specified during object
 * construction or at any other time.
 *
 * The GetValue() and SetValue() methods can be used to access individual color
 * values stored in the image.
 *
 * This class manages its own memory. If you copy an image object into another
 * image object, the original contents of the image object will be freed.
 *
 * If you specify a new size for the image and the new size is smaller than the
 * current size, the allocated memory will not be reallocated. Call ReclaimMem()
 * to reclaim the wasted memory.
 *
 * <b>Border Values</b>
 *
 * All of the color values outside of the image are assumed to have a common
 * color value known as the <i>border value</i>.
 *
 * To set the border value, call the SetBorderValue() method.
 *
 * The GetValue() method returns the border value if the specified position lies
 * outside of the image.
 *
 * <b>Internal Image Structure</b>
 *
 * 2D array of color values.
 *
 *
 * @author William Matrix Peckham
 */
public final class Image {

    /**
     * Value used for all positions outside of the noise map.
     */
    private Color borderValue;

    /**
     * The current height of the noise map.
     */
    private int height;

    /**
     * The amount of memory allocated for this noise map.
     *
     * This value is equal to the number of @a Color values allocated for the
     * noise map, not the number of bytes.
     */
    private int memUsed;

    /**
     * A pointer to the noise map buffer.
     */
    private Color[][] noiseImage;

    /**
     * The stride amount of the noise map.
     */
    private int stride;

    /**
     * The current width of the noise map.
     */
    private int width;

    /**
     * Constructor.
     *
     * Creates an empty noise map.
     */
    public Image() {
	initObj();
    }

    /**
     * Constructor.
     *
     * @param width The width of the new noise map.
     * @param height The height of the new noise map.
     *
     * @noise.pre The width and height values are positive.
     * @noise.pre The width and height values do not exceed the maximum possible
     * width and height for the noise map.
     *
     * @throws IllegalArgumentException See the preconditions.
     *
     * Creates a noise map with uninitialized values.
     *
     * It is considered an error if the specified dimensions are not positive.
     */
    public Image(int width, int height) {
	initObj();
	setSize(width, height);
    }

    /**
     * Copy constructor.
     *
     * @param rhs image to copy
     */
    public Image(Image rhs) {
	initObj();
	copyNoiseImage(rhs);
    }

    /**
     * Returns the minimum amount of memory required to store a noise map of the
     * specified size.
     *
     * @param width The width of the noise map.
     * @param height The height of the noise map.
     *
     * @return The minimum amount of memory required to store the noise map.
     *
     * The returned value is measured by the number of @a Color values required
     * to store the noise map, not by the number of bytes.
     */
    private int calcMinMemUsage(int width, int height) {
	return calcStride(width) * height;
    }

    /**
     * Calculates the stride amount for a noise map.
     *
     * @param width The width of the noise map.
     *
     * @return The stride amount.
     *
     * - The <i>stride amount</i> is the offset between the starting points of
     * any two adjacent slabs in a noise map. - The stride amount is measured by
     * the number of @a Color values between these two points, not by the number
     * of bytes.
     */
    private int calcStride(int width) {
	return width;
    }

    /**
     * Clears the noise map to a specified value.
     *
     * @param value The value that all positions within the noise map are
     * cleared to.
     */
    public void clear(Color value) {
	if (noiseImage != null) {
	    for (int y = 0; y < height; y++) {
		for (int x = 0; x < width; x++) {
		    noiseImage[y][x] = value;
		}
	    }
	}
    }

    /**
     * Copies the contents of the buffer in the source noise map into this noise
     * map.
     *
     * @param source The source noise map.
     *
     * @throws noise::ExceptionOutOfMemory Out of memory.
     *
     * This method reallocates the buffer in this noise map object if necessary.
     *
     * @warning This method calls the standard library function
     * @a memcpy, which probably violates the DMCA because it can be used //. to
     * make a bitwise copy of anything, like, say, a DVD. Don't call this method
     * if you live in the USA.
     */
    private void copyNoiseImage(Image source) {
	//resize the noise map buffer then copy the source map
	//buffer to this map buffer
	setSize(source.width, source.height);
	for (int y = 0; y < source.getHeight();
		y++) {
	    arraycopy(source.noiseImage[y], 0, noiseImage[y], 0,
		    noiseImage[y].length);
	}
	borderValue = source.borderValue;
    }

    /**
     * Resets the noise map object.
     *
     * This method is similar to the InitObj() method, except this method
     * deletes the buffer in this noise map.
     */
    private void deleteNoiseImageAndReset() {
	noiseImage = null;
	initObj();
    }

    /**
     * Returns the value used for all positions outside of the noise map.
     *
     * @return The value used for all positions outside of the noise map.
     *
     * All positions outside of the noise map are assumed to have a common value
     * known as the <i>border value</i>.
     */
    Color getBorderValue() {
	return borderValue;
    }

    /**
     * Returns the height of the noise map.
     *
     * @return The height of the noise map.
     */
    public int getHeight() {
	return height;
    }

    /**
     * Returns the amount of memory allocated for this noise map.
     *
     * @return The amount of memory allocated for this noise map.
     *
     * This method returns the number of @a Color values allocated.
     */
    public int getMemUsed() {
	return memUsed;
    }

    /**
     * Returns a value from the specified position in the noise map.
     *
     * @param x The x coordinate of the position.
     * @param y The y coordinate of the position.
     *
     * @return The value at that position.
     *
     * This method returns the border value if the coordinates exist outside of
     * the noise map.
     */
    public Color getValue(int x, int y) {
	if (noiseImage != null) {
	    if (x >= 0 && x < width && y >= 0 && y < height) {
		return noiseImage[y][x];
	    }
	}
	//the coordinates specified are outside the map, border
	return borderValue;
    }

    /**
     * Returns the width of the noise map.
     *
     * @return The width of the noise map.
     */
    public int getWidth() {
	return width;
    }

    /**
     * Initializes the noise map object.
     *
     * @noise.pre Must be called during object construction.
     * @noise.pre The noise map buffer must not exist.
     */
    private void initObj() {
	noiseImage = null;
	height = 0;
	width = 0;
	stride = 0;
	memUsed = 0;
	borderValue = new Color();
    }

    /**
     * Reallocates the noise map to recover wasted memory.
     *
     * The contents of the noise map is unaffected.
     */
    public void reclaimMem() {
	int newMemUsage = calcMinMemUsage(width, height);
	if (memUsed > newMemUsage) {
	    Color[][] newNoiseImage = new Color[height][width];
	    for (int y = 0; y < height;
		    y++) {
		arraycopy(noiseImage[y], 0, newNoiseImage[y], 0, width);
	    }
	    noiseImage = newNoiseImage;
	    memUsed = newMemUsage;
	}
    }

    /**
     * Sets the value to use for all positions outside of the noise map.
     *
     * @param borderValue The value to use for all positions outside of the
     * noise map.
     *
     * All positions outside of the noise map are assumed to have a common value
     * known as the <i>border value</i>.
     */
    public void setBorderValue(Color borderValue) {
	this.borderValue = borderValue;
    }

    /**
     * Sets the new size for the noise map.
     *
     * @param width The new width for the noise map.
     * @param height The new height for the noise map.
     *
     * @noise.pre The width and height values are positive.
     * @noise.pre The width and height values do not exceed the maximum possible
     * width and height for the noise map.
     *
     * @throws IllegalArgumentException See the preconditions.
     *
     * On exit, the contents of the noise map are undefined.
     *
     *
     */
    public void setSize(int width, int height) {
	if (width < 0 || height < 0 || width > RASTER_MAX_WIDTH || height
		> RASTER_MAX_HEIGHT) {
	    throw new IllegalArgumentException("Bad width or height for map");
	} else if (width == 0 || height == 0) {
	    //an empty noise map was specified. delete all and reset
	    deleteNoiseImageAndReset();
	} else {
	    //new noise map size was specified. Allocates a new noise buffer
	    //unless the current buffer is large enough for the new noise map
	    //that way we don't reallocate when we don't need to.
	    int newMemUsage = calcMinMemUsage(width, height);
	    if (memUsed < newMemUsage) {
		deleteNoiseImageAndReset();
		noiseImage = new Color[height][width];
		memUsed = newMemUsage;
	    }
	    stride = calcStride(width);
	    this.width = width;
	    this.height = height;
	}
    }

    /**
     * Sets a value at a specified position in the noise map.
     *
     * @param x The x coordinate of the position.
     * @param y The y coordinate of the position.
     * @param value The value to set at the given position.
     *
     * This method does nothing if the noise map object is empty or the position
     * is outside the bounds of the noise map.
     */
    public void setValue(int x, int y, Color value) {
	if (noiseImage != null) {
	    if (x >= 0 && x < width && y >= 0 && y < height) {
		noiseImage[y][x] = value;
	    }
	}
    }

    /**
     * Takes ownership of the buffer within the source noise map.
     *
     * @param source The source noise map.
     *
     * On exit, the source noise map object becomes empty.
     *
     * This method only moves the buffer pointer so this method is very quick.
     */
    public void takeOwnership(Image source) {
	//copy the noise and have it reset
	memUsed = source.memUsed;
	height = source.height;
	width = source.width;
	stride = source.stride;
	noiseImage = source.noiseImage;
	source.initObj();
    }

    /**
     * Generates a java.awt.image.BufferedImage from the current image. Should
     * be called after values have been generated.
     *
     * @return
     */
    public BufferedImage asBufferedImage() {
	BufferedImage image = new BufferedImage(getWidth(), getHeight(),
		BufferedImage.TYPE_INT_ARGB);
	for (int y = 0; y < getHeight(); y++) {
	    for (int x = 0; x < getWidth(); x++) {
		Color c = getValue(x, y);
		image.setRGB(x, y, c.getAWTColor().getRGB());
	    }
	}
	return image;
    }

    private static final Logger LOG = Logger.getLogger(Image.class.getName());

}
