/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module.modifier;

import com.matrixpeckham.libnoise.module.AbstractModule;
import static com.matrixpeckham.libnoise.util.Globals.clampValue;
import static com.matrixpeckham.libnoise.util.Globals.linearInterp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Noise module that maps the output value from a source module onto a
 * terrace-forming curve.
 *
 * @image html moduleterrace.png
 *
 * This noise module maps the output value from the source module onto a
 * terrace-forming curve. The start of this curve has a slope of zero; its slope
 * then smoothly increases. This curve also contains
 * <i>control points</i> which resets the slope to zero at that point, producing
 * a "terracing" effect. Refer to the following illustration:
 *
 * @image html terrace.png
 *
 * To add a control point to this noise module, call the AddControlPoint()
 * method.
 *
 * An application must add a minimum of two control points to the curve. If this
 * is not done, the GetValue() method fails. The control points can have any
 * value, although no two control points can have the same value. There is no
 * limit to the number of control points that can be added to the curve.
 *
 * This noise module clamps the output value from the source module if that
 * value is less than the value of the lowest control point or greater than the
 * value of the highest control point.
 *
 * This noise module is often used to generate terrain features such as your
 * stereotypical desert canyon.
 *
 * This noise module requires one source module. Noise module that maps the
 * output value from a source module onto a terrace-forming curve.
 *
 * @image html moduleterrace.png
 *
 * This noise module maps the output value from the source module onto a
 * terrace-forming curve. The start of this curve has a slope of zero; its slope
 * then smoothly increases. This curve also contains
 * <i>control points</i> which resets the slope to zero at that point, producing
 * a "terracing" effect. Refer to the following illustration:
 *
 * @image html terrace.png
 *
 * To add a control point to this noise module, call the AddControlPoint()
 * method.
 *
 * An application must add a minimum of two control points to the curve. If this
 * is not done, the GetValue() method fails. The control points can have any
 * value, although no two control points can have the same value. There is no
 * limit to the number of control points that can be added to the curve.
 *
 * This noise module clamps the output value from the source module if that
 * value is less than the value of the lowest control point or greater than the
 * value of the highest control point.
 *
 * This noise module is often used to generate terrain features such as your
 * stereotypical desert canyon.
 *
 * This noise module requires one source module.
 */
public class Terrace extends AbstractModule {

    /**
     * control points we use ArrayList to avoid all bookkeeping
     */
    List<Double> controlPoints;

    /**
     * Determines if the terrace-forming curve between all control points is
     * inverted.
     */
    boolean invertTerraces;

    /**
     * constructor
     */
    public Terrace() {
	controlPoints = new ArrayList<>();
	invertTerraces = false;
    }

    /**
     * Adds a control point to the terrace-forming curve.
     *
     * @param value The value of the control point to add.
     *
     * @noise.pre No two control points have the same value.
     *
     * @throws IllegalArgumentException An invalid parameter was specified; see
     * the preconditions for more information.
     *
     * Two or more control points define the terrace-forming curve. The start of
     * this curve has a slope of zero; its slope then smoothly increases. At the
     * control points, its slope resets to zero.
     *
     * It does not matter which order these points are added.
     */
    public void addControlPoint(double value) {
	//find the insertion point for the new control point and insert the new
	//point at that position. the control point array will remain sorted by
	//value.
	int insertionPos = findInsertionPos(value);
	insertAtPos(insertionPos, value);
    }

    /**
     * Deletes all the control points on the terrace-forming curve.
     *
     * @noise.post All control points on the terrace-forming curve are deleted.
     */
    public void clearAllControlPoints() {
	controlPoints.clear();
    }

    /**
     * Determines the array index in which to insert the control point into the
     * internal control point array.
     *
     * @param value The value of the control point.
     *
     * @return The array index in which to insert the control point.
     *
     * @noise.pre No two control points have the same value.
     *
     * @throws IllegalArgumentException An invalid parameter was specified; see
     * the preconditions for more information.
     *
     * By inserting the control point at the returned array index, this class
     * ensures that the control point array is sorted by value. The code that
     * maps a value onto the curve requires a sorted control point array.
     */
    protected int findInsertionPos(double value) {
	int insertionPos;

	for (insertionPos = 0; insertionPos < controlPoints.size();
		insertionPos++) {
	    if (value < controlPoints.get(insertionPos)) {
		//we found the array index in which to insert the new control point
		break;
	    } else if (value == controlPoints.get(insertionPos)) {
		//each control point is required to contain a unique value, so
		//throw an exception
		throw new IllegalArgumentException(
			"Control points must be unique");
	    }
	}

	return insertionPos;
    }

    /**
     * Returns a pointer to the array of control points on the terrace-forming
     * curve.
     *
     * @return A pointer to the array of control points in this noise module.
     *
     * Two or more control points define the terrace-forming curve. The start of
     * this curve has a slope of zero; its slope then smoothly increases. At the
     * control points, its slope resets to zero.
     *
     * Before calling this method, call GetControlPointCount() to determine the
     * number of control points in this array.
     *
     * It is recommended that an application does not store this pointer for
     * later use since the pointer to the array may change if the application
     * calls another method of this object.
     */
    public List<Double> getControlPointArray() {
	return controlPoints;
    }

    /**
     * Returns the number of control points on the terrace-forming curve.
     *
     * @return The number of control points on the terrace-forming curve.
     */
    public int getControlPointCount() {
	return controlPoints.size();
    }

    @Override
    public int getSourceModuleCount() {
	return 1;
    }

    @Override
    public double getValue(double x, double y, double z) {
	//get the output value from the source module
	double sourceModuleValue = sourceModule[0].getValue(x, y, z);
	//find the first element in the control point array that has a value
	//larger than the output value fromt he source module
	int indexPos;
	for (indexPos = 0; indexPos < controlPoints.size(); indexPos++) {
	    if (sourceModuleValue < controlPoints.get(indexPos)) {
		break;
	    }
	}
	//find the two nearest control points so that we can map their values
	//onto a quadratic cure
	int index0
		= clampValue(indexPos - 1, 0, controlPoints.size()
			- 1);
	int index1 = clampValue(indexPos, 0, controlPoints.size() - 1);
	//if some control points are missin (which occurs if the output value of
	//the source module is greater than the larger value or less than the
	//smallest value of the control point array, get the value of the
	//nearest control point and exit
	if (index0 == index1) {
	    return controlPoints.get(index1);
	}
	//compute the alpha value used for linear interpolation
	double value0 = controlPoints.get(index0);
	double value1 = controlPoints.get(index1);
	double alpha = (sourceModuleValue - value0) / (value1 - value0);
	if (invertTerraces) {
	    alpha = 1.0 - alpha;
	    double t = value0;
	    value0 = value1;
	    value1 = t;
	}
	//squareing the alpha produces the terrace effect.
	alpha *= alpha;
	//now linear interpolate
	return linearInterp(value0, value1, alpha);
    }

    /**
     * Inserts the control point at the specified position in the internal
     * control point array.
     *
     * @param insertionPos The zero-based array position in which to insert the
     * control point.
     * @param value The value of the control point.
     *
     * To make room for this new control point, this method reallocates the
     * control point array and shifts all control points occurring after the
     * insertion position up by one.
     *
     * Because the curve mapping algorithm in this noise module requires that
     * all control points in the array be sorted by value, the new control point
     * should be inserted at the position in which the order is still preserved.
     */
    protected void insertAtPos(int insertionPos, double value) {
	//we don't need bookkeeping
	controlPoints.add(insertionPos, value);
    }

    /**
     * Enables or disables the inversion of the terrace-forming curve between
     * the control points.
     */
    public void invertTerraces() {
	invertTerraces(true);
    }

    /**
     * Enables or disables the inversion of the terrace-forming curve between
     * the control points.
     *
     * @param invert Specifies whether to invert the curve between the control
     * points.
     */
    public void invertTerraces(boolean invert) {
	invertTerraces = invert;
    }

    /**
     * Determines if the terrace-forming curve between the control points is
     * inverted.
     *
     * @return - @a true if the curve between the control points is inverted. -
     * @a false if the curve between the control points is not inverted.
     */
    public boolean isTerracesInverted() {
	return invertTerraces;
    }

    /**
     * Creates a number of equally-spaced control points that range from -1 to
     * +1.
     *
     * @param controlPointCount The number of control points to generate.
     *
     * @noise.pre The number of control points must be greater than or equal to
     * 2.
     *
     * @noise.post The previous control points on the terrace-forming curve are
     * deleted.
     *
     * @throws IllegalArgumentException An invalid parameter was specified; see
     * the preconditions for more information.
     *
     * Two or more control points define the terrace-forming curve. The start of
     * this curve has a slope of zero; its slope then smoothly increases. At the
     * control points, its slope resets to zero.
     */
    public void makeControlPoints(int controlPointCount) {
	if (controlPointCount < 2) {
	    throw new IllegalArgumentException("Need two or more control points");
	}
	clearAllControlPoints();
	double terraceStep = 2.0 / (controlPointCount - 1.0);
	double curValue = -1.0;
	for (int i = 0; i < controlPointCount; i++) {
	    addControlPoint(curValue);
	    curValue += terraceStep;
	}
    }

    private static final Logger LOG = Logger.getLogger(Terrace.class.getName());

}
