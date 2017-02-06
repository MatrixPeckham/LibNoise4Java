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
import static com.matrixpeckham.libnoise.util.Globals.cubicInterp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Noise module that maps the output value from a source module onto an
 * arbitrary function curve.
 *
 * @image html modulecurve.png
 *
 * This noise module maps the output value from the source module onto an
 * application-defined curve. This curve is defined by a number of
 * <i>control points</i>; each control point has an <i>input value</i>
 * that maps to an <i>output value</i>. Refer to the following illustration:
 *
 * @image html curve.png
 *
 * To add the control points to this curve, call the AddControlPoint() method.
 *
 * Since this curve is a cubic spline, an application must add a minimum of four
 * control points to the curve. If this is not done, the GetValue() method
 * fails. Each control point can have any input and output value, although no
 * two control points can have the same input value. There is no limit to the
 * number of control points that can be added to the curve.
 *
 * This noise module requires one source module.
 */
//TODO: Should this be a forwarding extender?
public class Curve extends AbstractModule {

    /**
     * array that stores the control points, using ArrayList instead of straight
     * array.
     */
    protected List<ControlPoint> controlPoints;

    /**
     * constructor
     */
    public Curve() {
	controlPoints = new ArrayList<>();
    }

    /**
     * Adds a control point to the curve.
     *
     * @param inputValue The input value stored in the control point.
     * @param outputValue The output value stored in the control point.
     *
     * @noise.pre No two control points have the same input value.
     *
     * @throws IllegalArgumentException An invalid parameter was specified; see
     * the preconditions for more information.
     *
     * It does not matter which order these points are added.
     */
    public void addControlPoint(double inputValue, double outputValue) {
	// Find the insertion point for the new control point and insert the new
	// point at that position.  The control point array will remain sorted by
	// input value.
	int insertionPos = findInsertionPos(inputValue);
	insertAtPos(insertionPos, inputValue, outputValue);
    }

    /**
     * Deletes all control points.
     */
    public void clearAllControlPoints() {
	controlPoints.clear();
    }

    /**
     * Determines the array index in which to insert the control point into the
     * internal control point array.
     *
     * @param inputValue The input value of the control point.
     *
     * @return The array index in which to insert the control point.
     *
     * @noise.pre No two control points have the same input value.
     *
     * @throws IllegalArgumentException An invalid parameter was specified; see
     * the preconditions for more information.
     *
     * By inserting the control point at the returned array index, this class
     * ensures that the control point array is sorted by input value. The code
     * that maps a value onto the curve requires a sorted control point array.
     */
    protected int findInsertionPos(double inputValue) {
	int insertionPos;
	for (insertionPos = 0; insertionPos < controlPoints.size();
		insertionPos++) {
	    if (inputValue < controlPoints.get(insertionPos).inputalue) {
		//we found the array index in which to insert the new control point
		break;
	    } else if (inputValue == controlPoints.get(insertionPos).inputalue) {
		//each control point is requied to contain a unique input value,
		//so throw an exception.
		throw new IllegalArgumentException(
			"Control points must have a unique input value");
	    }
	}
	return insertionPos;
    }

    /**
     * Returns a pointer to the array of control points on the curve.
     *
     * @return A pointer to the array of control points.
     *
     * Before calling this method, call GetControlPointCount() to determine the
     * number of control points in this array.
     *
     * It is recommended that an application does not store this pointer for
     * later use since the pointer to the array may change if the application
     * calls another method of this object.
     */
    public List<ControlPoint> getControlPointArray() {
	return controlPoints;
    }

    /**
     * Returns the number of control points on the curve.
     *
     * @return The number of control points on the curve.
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
	//find the first element in the control point array that has an input
	//value larger than the output value from the source module
	int indexPos;
	for (indexPos = 0; indexPos < controlPoints.size(); indexPos++) {
	    if (sourceModuleValue < controlPoints.get(indexPos).inputalue) {
		break;
	    }
	}
	//find the four nearest control points so that we can perform cubic interpolation.
	int index0
		= clampValue(indexPos - 2, 0, controlPoints.size()
			- 1);
	int index1
		= clampValue(indexPos - 1, 0, controlPoints.size()
			- 1);
	int index2 = clampValue(indexPos, 0, controlPoints.size() - 1);
	int index3
		= clampValue(indexPos + 1, 0, controlPoints.size()
			- 1);
	//If some control point are missing (which occurs if the value from the
	//source module is greater than the larges input value or less than the
	//smallest input value of the control point array), get the corresponding
	//output value of the nearest control point and exit early
	if (index1 == index2) {
	    return controlPoints.get(index1).outputValue;
	}
	//compute the alpha value used for cubic interpolation.
	double input0 = controlPoints.get(index1).inputalue;
	double input1 = controlPoints.get(index2).inputalue;
	double alpha = (sourceModuleValue - input0) / (input1 - input0);
	//return the interpolated value
	return cubicInterp(controlPoints.get(index0).outputValue,
		controlPoints.get(index1).outputValue,
		controlPoints.get(index2).outputValue,
		controlPoints.get(index3).outputValue, alpha);
    }

    /**
     * Inserts the control point at the specified position in the internal
     * control point array.
     *
     * @param insertionPos The zero-based array position in which to insert the
     * control point.
     * @param inputValue The input value stored in the control point.
     * @param outputValue The output value stored in the control point.
     *
     * To make room for this new control point, this method reallocates the
     * control point array and shifts all control points occurring after the
     * insertion position up by one.
     *
     * Because the curve mapping algorithm used by this noise module requires
     * that all control points in the array must be sorted by input value, the
     * new control point should be inserted at the position in which the order
     * is still preserved.
     */
    protected void insertAtPos(int insertionPos, double inputValue,
	    double outputValue) {
	//because we swithced to List, we don't have to do the bookkeeping
	//that the C++ version of the code does
	//We just wrap the values in a control point and insert
	ControlPoint cp = new ControlPoint();
	cp.inputalue = inputValue;
	cp.outputValue = outputValue;
	controlPoints.add(insertionPos, cp);
    }

    /**
     * this class defines a control point.
     *
     * control points are used for defining splines.
     */
    public static final class ControlPoint {

	/**
	 * the input value
	 */
	double inputalue;

	/**
	 * the output value that is mapped from the input value.
	 */
	double outputValue;

    }

    private static final Logger LOG = Logger.getLogger(Curve.class.getName());

}
