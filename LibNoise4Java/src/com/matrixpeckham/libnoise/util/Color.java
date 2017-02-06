/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.util;

import java.util.logging.Logger;

/**
 * Defines a color.
 *
 * <p>
 * A color object contains four 16-bit components this class is used instead of
 * Java's built in color class because we want mutable classes here. The values
 * are 16-bit shorts so that we can store 0-255 in them and they should be kept
 * between those values.</p>
 *
 * @author William Matrix Peckham
 */
public class Color {

    /**
     * Value of the alpha(transparency) channel.
     */
    public short alpha;

    /**
     * Value of the blue channel.
     */
    public short blue;

    /**
     * Value of the green channel.
     */
    public short green;

    /**
     * Value of the red channel.
     */
    public short red;

    public Color() {
    }

    public Color(Color c) {
        red = c.red;
        green = c.green;
        blue = c.blue;
        alpha = c.alpha;
    }

    /**
     * Constructor.
     *
     * @param r Value of the red channel.
     * @param g Value of the green channel.
     * @param b Value of the blue channel.
     * @param a Value of the alpha (transparency) channel.
     *
     */
    public Color(short r, short g, short b, short a) {
        if (r < 0 || r > 255) {
            throw new IllegalArgumentException();
        }
        if (g < 0 || g > 255) {
            throw new IllegalArgumentException();
        }
        if (b < 0 || b > 255) {
            throw new IllegalArgumentException();
        }
        if (a < 0 || a > 255) {
            throw new IllegalArgumentException();
        }
        red = r;
        green = g;
        blue = b;
        alpha = a;
    }

    /**
     * Constructor.
     *
     * @param r Value of the red channel.
     * @param g Value of the green channel.
     * @param b Value of the blue channel.
     * @param a Value of the alpha (transparency) channel.
     *
     */
    public Color(int r, int g, int b, int a) {
        if (r < 0 || r > 255) {
            throw new IllegalArgumentException();
        }
        if (g < 0 || g > 255) {
            throw new IllegalArgumentException();
        }
        if (b < 0 || b > 255) {
            throw new IllegalArgumentException();
        }
        if (a < 0 || a > 255) {
            throw new IllegalArgumentException();
        }
        this.red = (short) r;
        this.green = (short) g;
        this.blue = (short) b;
        this.alpha = (short) a;
    }

    /**
     * Gets the AWT Color that this color represents.
     *
     * @return
     */
    public java.awt.Color getAWTColor() {
        return new java.awt.Color(red, green, blue, alpha);
    }

    /**
     * Copies parameter into this object.
     *
     * @param color
     */
    void setTo(Color color) {
        red = color.red;
        green = color.green;
        blue = color.blue;
        alpha = color.alpha;
    }

    private static final Logger LOG = Logger.getLogger(Color.class.getName());

}
