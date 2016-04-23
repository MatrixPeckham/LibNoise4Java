/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module;

import static com.matrixpeckham.libnoise.util.Globals.linearInterp;
import static com.matrixpeckham.libnoise.util.Globals.sCurve3;


public class Select extends Module {

    /**
     * default edge-falloff value for select
     */
    public static final double DEFAULT_SELECT_EDGE_FALLOFF = 0.0;

    /**
     * default lower bound of the selection range for select
     */
    public static final double DEFAULT_SELECT_LOWER_BOUND = -1.0;

    /**
     * default upper bound of the selection range for select
     */
    public static final double DEFAULT_SELECT_UPPER_BOUND = 1.0;

    /**
     * edge falloff value
     */
    protected double edgeFalloff;

    /**
     * lower bound of the selection range
     */
    protected double lowerBound;

    /**
     * upper bound of the selection range
     */
    protected double upperBound;

    public Select() {
        edgeFalloff = DEFAULT_SELECT_EDGE_FALLOFF;
        lowerBound = DEFAULT_SELECT_LOWER_BOUND;
        upperBound = DEFAULT_SELECT_UPPER_BOUND;
    }

    /**
     * Returns the falloff value at the edge transition.
     *
     * @returns The falloff value at the edge transition.
     *
     * The falloff value is the width of the edge transition at either edge of
     * the selection range.
     *
     * By default, there is an abrupt transition between the output values from
     * the two source modules at the selection-range boundary.
     */
    public double getEdgeFalloff() {
        return edgeFalloff;
    }

    /**
     * Returns the lower bound of the selection range.
     *
     * @returns The lower bound of the selection range.
     *
     * If the output value from the control module is within the selection
     * range, the GetValue() method outputs the value from the source module
     * with an index value of 1. Otherwise, this method outputs the value from
     * the source module with an index value of 0.
     */
    public double getLowerBound() {
        return lowerBound;
    }

    @Override
    public int getSourceModuleCount() {
        return 3;
    }

    /**
     * Returns the upper bound of the selection range.
     *
     * @returns The upper bound of the selection range.
     *
     * If the output value from the control module is within the selection
     * range, the GetValue() method outputs the value from the source module
     * with an index value of 1. Otherwise, this method outputs the value from
     * the source module with an index value of 0.
     */
    public double getUpperBound() {
        return upperBound;
    }

    @Override
    public double getValue(double x, double y, double z) {
        double controlValue = sourceModule[2].getValue(x, y, z);
        double alpha;
        if (edgeFalloff > 0) {
            if (controlValue < (lowerBound - edgeFalloff)) {
                //the output value from the control module is below the selector
                // threshold; return the output value from the first module.
                return sourceModule[0].getValue(x, y, z);
            } else if (controlValue < (lowerBound + edgeFalloff)) {
                //the output value from the control module is near the lower end of the
                //selector threshold and within the smooth curve. interpolate between
                //the output values from the first and second modules
                double lowerCurve = lowerBound - edgeFalloff;
                double upperCurve = lowerBound + edgeFalloff;
                alpha
                        = sCurve3((controlValue - lowerCurve)
                                / (upperCurve - lowerCurve));
                return linearInterp(sourceModule[0].getValue(x, y, z),
                        sourceModule[1].getValue(x, y, z), alpha);
            } else if (controlValue < (upperBound - edgeFalloff)) {
                //the output value from the control module is within the selector
                // threshold; return the output value from the second source module.
                return sourceModule[1].getValue(x, y, z);
            } else if (controlValue < (upperBound + edgeFalloff)) {
                //the output value from the control module is near the upper end of the
                //selector threshold and within the smooth curve. interpolate between
                //teh output values fromt he first and second source modules
                double lowerCurve = (upperBound - edgeFalloff);
                double upperCurve = upperBound + edgeFalloff;
                alpha
                        = sCurve3((controlValue - lowerCurve)
                                / (upperCurve - lowerCurve));
                return linearInterp(sourceModule[1].getValue(x, y, z),
                        sourceModule[0].getValue(x, y, z), alpha);
            } else {
                //output value from the control module is above the selector threshold
                //return the output from the first source module
                return sourceModule[0].getValue(x, y, z);
            }
        } else {
            if (controlValue < lowerBound || controlValue > upperBound) {
                return sourceModule[0].getValue(x, y, z);
            } else {
                return sourceModule[1].getValue(x, y, z);
            }
        }
    }

    /**
     * Sets the lower and upper bounds of the selection range.
     *
     * @param lowerBound The lower bound.
     * @param upperBound The upper bound.
     *
     * @pre The lower bound must be less than or equal to the upper bound.
     *
     * @throw noise::ExceptionInvalidParam An invalid parameter was specified;
     * see the preconditions for more information.
     *
     * If the output value from the control module is within the selection
     * range, the GetValue() method outputs the value from the source module
     * with an index value of 1. Otherwise, this method outputs the value from
     * the source module with an index value of 0.
     */
    public void setBounds(double lowerBound, double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        setEdgeFalloff(edgeFalloff);
    }

    /**
     * Sets the control module.
     *
     * @param m The control module.
     *
     * The control module determines the output value to select. If the output
     * value from the control module is within a range of values known as the
     * <i>selection range</i>, the GetValue() method outputs the value from the
     * source module with an index value of 1. Otherwise, this method outputs
     * the value from the source module with an index value of 0.
     *
     * This method assigns the control module an index value of 2. Passing the
     * control module to this method produces the same results as passing the
     * control module to the SetSourceModule() method while assigning that noise
     * module an index value of 2.
     *
     * This control module must exist throughout the lifetime of this noise
     * module unless another control module replaces that control module.
     */
    public void setControlModule(Module m) {
        sourceModule[2] = m;
    }

    /**
     * Sets the falloff value at the edge transition.
     *
     * @param edgeFalloff The falloff value at the edge transition.
     *
     * The falloff value is the width of the edge transition at either edge of
     * the selection range.
     *
     * By default, there is an abrupt transition between the values from the two
     * source modules at the boundaries of the selection range.
     *
     * For example, if the selection range is 0.5 to 0.8, and the edge falloff
     * value is 0.1, then the GetValue() method outputs: - the output value from
     * the source module with an index value of 0 if the output value from the
     * control module is less than 0.4 ( = 0.5 - 0.1). - a linear blend between
     * the two output values from the two source modules if the output value
     * from the control module is between 0.4 ( = 0.5 - 0.1) and 0.6 ( = 0.5 +
     * 0.1). - the output value from the source module with an index value of 1
     * if the output value from the control module is between 0.6 ( = 0.5 + 0.1)
     * and 0.7 ( = 0.8 - 0.1). - a linear blend between the output values from
     * the two source modules if the output value from the control module is
     * between 0.7 ( = 0.8 - 0.1 ) and 0.9 ( = 0.8 + 0.1). - the output value
     * from the source module with an index value of 0 if the output value from
     * the control module is greater than 0.9 ( = 0.8 + 0.1).
     */
    public void setEdgeFalloff(double edgeFalloff) {
        //make sure that the falloff curves do not ovrelap
        double boudSize = upperBound - lowerBound;
        this.edgeFalloff = (edgeFalloff > boudSize / 2) ? boudSize / 2
                : edgeFalloff;
    }

    /**
     * Sets the lower bounds of the selection range.
     *
     * @param lowerBound The lower bound.
     *
     * @pre The lower bound must be less than or equal to the upper bound.
     *
     * @throw noise::ExceptionInvalidParam An invalid parameter was specified;
     * see the preconditions for more information.
     *
     * If the output value from the control module is within the selection
     * range, the GetValue() method outputs the value from the source module
     * with an index value of 1. Otherwise, this method outputs the value from
     * the source module with an index value of 0.
     */
    public void setLowerBound(double lowerBound) {
        this.lowerBound = lowerBound;
        setEdgeFalloff(edgeFalloff);
    }

    /**
     * Sets the upper bounds of the selection range.
     *
     * @param upperBound The upper bound.
     *
     * @pre The lower bound must be less than or equal to the upper bound.
     *
     * @throw noise::ExceptionInvalidParam An invalid parameter was specified;
     * see the preconditions for more information.
     *
     * If the output value from the control module is within the selection
     * range, the GetValue() method outputs the value from the source module
     * with an index value of 1. Otherwise, this method outputs the value from
     * the source module with an index value of 0.
     */
    public void setUpperBound(double upperBound) {
        this.upperBound = upperBound;
        setEdgeFalloff(edgeFalloff);
    }

}
