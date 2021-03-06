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

import static com.matrixpeckham.libnoise.util.Globals.clampValue;
import static com.matrixpeckham.libnoise.util.Globals.linearInterpColor;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Defines a color gradient.
 *
 * A color gradient is a list of gradually-changing colors. A color gradient is
 * defined by a list of <i>gradient points</i>. Each gradient point has a
 * position and a color. In a color gradient, the colors between two adjacent
 * gradient points are linearly interpolated.
 *
 * To add a gradient point to the color gradient, pass its position and color to
 * the AddGradientPoint() method.
 *
 * To retrieve a color from a specific position in the color gradient, pass that
 * position to the GetColor() method.
 *
 * This class is a useful tool for coloring height maps based on elevation.
 *
 * <b>Gradient example</b>
 *
 * Suppose a gradient object contains the following gradient points: - -1.0 maps
 * to black. - 0.0 maps to white. - 1.0 maps to red.
 *
 * If an application passes -0.5 to the GetColor() method, this method will
 * return a gray color that is halfway between black and white.
 *
 * If an application passes 0.25 to the GetColor() method, this method will
 * return a very light pink color that is one quarter of the way between white
 * and red.
 */
public class GradientColor {

    /**
     * Array that stores the gradient points.
     */
    List<GradientPoint> gradientPoints;

    /**
     * A color object that is used by a gradient object to store a temporary
     * value.
     */
    Color workingColor = new Color();

    /**
     * Default constructor no colors
     */
    public GradientColor() {
	gradientPoints = new ArrayList<>();
    }

    /**
     * Adds a gradient point to this gradient object.
     *
     * @param gradientPos The position of this gradient point.
     * @param gradientColor The color of this gradient point.
     *
     * @noise.pre No two gradient points have the same position.
     *
     * @throws IllegalArgumentException See the precondition.
     *
     * It does not matter which order these gradient points are added.
     */
    public void addGradientPoint(double gradientPos,
	    Color gradientColor) {
	//find the insertion point for the new point and insert
	int insertionPos = findInsertionPos(gradientPos);
	insertAtPos(insertionPos, gradientPos, gradientColor);
    }

    /**
     * Deletes all the gradient points from this gradient object.
     *
     * @noise.post All gradient points from this gradient object are deleted.
     */
    public void clear() {
	gradientPoints.clear();
    }

    /**
     * Determines the array index in which to insert the gradient point into the
     * internal gradient-point array.
     *
     * @param gradientPos The position of this gradient point.
     *
     * @return The array index in which to insert the gradient point.
     *
     * @noise.pre No two gradient points have the same input value.
     *
     * @throws noise::ExceptionInvalidParam See the precondition.
     *
     * By inserting the gradient point at the returned array index, this object
     * ensures that the gradient-point array is sorted by input value. The code
     * that maps a value to a color requires a sorted gradient-point array.
     */
    private int findInsertionPos(double gradientPos) {
	int insertionPos;
	for (insertionPos = 0; insertionPos < gradientPoints.size();
		insertionPos++) {
	    if (gradientPos < gradientPoints.get(insertionPos).pos) {
		// We found the array index in which to insert the new gradient point.
		// Exit now.
		break;
	    } else if (gradientPos == gradientPoints.get(insertionPos).pos) {
		// Each gradient point is required to contain a unique gradient
		// position, so throw an exception.
		throw new IllegalArgumentException(
			"Gradient values must be unique");
	    }
	}
	return insertionPos;
    }

    /**
     * Returns the color at the specified position in the color gradient.
     *
     * @param gradientPos The specified position.
     *
     * @return The color at that position.
     */
    public Color getColor(double gradientPos) {
	// Find the first element in the gradient point array that has a gradient
	// position larger than the gradient position passed to this method.
	int indexPos;
	for (indexPos = 0; indexPos < gradientPoints.size(); indexPos++) {
	    if (gradientPos < gradientPoints.get(indexPos).pos) {
		break;
	    }
	}
	// Find the two nearest gradient points so that we can perform linear
	// interpolation on the color.
	int index0
		= clampValue(indexPos - 1, 0, gradientPoints.size()
			- 1);
	int index1 = clampValue(indexPos, 0, gradientPoints.size() - 1);
	// If some gradient points are missing (which occurs if the gradient
	// position passed to this method is greater than the largest gradient
	// position or less than the smallest gradient position in the array), get
	// the corresponding gradient color of the nearest gradient point and exit
	// now.
	if (index0 == index1) {
	    workingColor.setTo(gradientPoints.get(index1).color);
	    return new Color(workingColor);
	}
	// Compute the alpha value used for linear interpolation.
	double input0 = gradientPoints.get(index0).pos;
	double input1 = gradientPoints.get(index1).pos;
	double alpha = (gradientPos - input0) / (input1 - input0);
	// Now perform the linear interpolation given the alpha value.
	Color color0 = gradientPoints.get(index0).color;
	Color color1 = gradientPoints.get(index1).color;
	linearInterpColor(color0, color1, alpha, workingColor);
	return new Color(workingColor);
    }

    /**
     * Returns a pointer to the array of gradient points in this object.
     *
     * @return A pointer to the array of gradient points.
     *
     * Before calling this method, call GetGradientPointCount() to determine the
     * number of gradient points in this array.
     *
     * It is recommended that an application does not store this pointer for
     * later use since the pointer to the array may change if the application
     * calls another method of this object.
     */
    public List<GradientPoint> getGradientPointArray() {
	return gradientPoints;
    }

    /**
     * Returns the number of gradient points stored in this object.
     *
     * @return The number of gradient points stored in this object.
     */
    public int getGradientPointCount() {
	return gradientPoints.size();
    }

    /**
     * Inserts the gradient point at the specified position in the internal
     * gradient-point array.
     *
     * @param insertionPos The zero-based array position in which to insert the
     * gradient point.
     * @param gradientPos The position of this gradient point.
     * @param gradientColor The color of this gradient point.
     *
     * To make room for this new gradient point, this method reallocates the
     * gradient-point array and shifts all gradient points occurring after the
     * insertion position up by one.
     *
     * Because this object requires that all gradient points in the array must
     * be sorted by the position, the new gradient point should be inserted at
     * the position in which the order is still preserved.
     */
    private void insertAtPos(int insertionPos, double gradientPos,
	    Color gradientColor) {
	GradientPoint gp = new GradientPoint();
	gp.color = gradientColor;
	gp.pos = gradientPos;
	gradientPoints.add(insertionPos, gp);
    }

    /**
     * Defines a point used to build a color gradient. A color gradient is a
     * list of gradually-changing colors. A color gradient is defined by a list
     * of <i>gradient points</i>. Each gradient point has a position and a
     * color. In a color gradient, the colors between two adjacent gradient
     * points are linearly interpolated. The ColorGradient class defines a color
     * gradient by a list of these objects.
     */
    public static class GradientPoint {

	/**
	 * The position of this gradient point.
	 */
	public double pos;

	/**
	 * The color of this gradient point.
	 */
	public Color color;

    }

    private static final Logger LOG
	    = Logger.getLogger(GradientColor.class.getName());

}
