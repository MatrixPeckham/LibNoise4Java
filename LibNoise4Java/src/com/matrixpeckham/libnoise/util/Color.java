/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.util;


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
        red = r;
        green = g;
        blue = b;
    }

    public java.awt.Color getAWTColor() {
        return new java.awt.Color(red, green, blue, alpha);
    }

    void setTo(Color color) {
        red = color.red;
        green = color.green;
        blue = color.blue;
        alpha = color.alpha;
    }

}
