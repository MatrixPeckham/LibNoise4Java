/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.model;

import com.matrixpeckham.libnoise.module.Module;
import com.matrixpeckham.libnoise.util.Globals;

/**
 * Model that defines the surface of a cylinder.
 *
 * <img src="modelcylinder.png" alt="MODEL_CYLINDER_IMAGE" />
 *
 * This model returns an output value from a noise module given the coordinates
 * of an input value located on the surface of a cylinder.
 *
 * To generate an output value, pass the (angle, height) coordinates of an input
 * value to the GetValue() method.
 *
 * This model is useful for creating: - seamless textures that can be mapped
 * onto a cylinder
 *
 * This cylinder has a radius of 1.0 unit and has infinite height. It is
 * oriented along the @a y axis. Its center is located at the origin.
 *
 * @author William Matrix Peckham
 */
public class Cylinder {

    /**
     * module to generate noise with
     */
    private Module module;

    /**
     * constructor
     */
    public Cylinder() {
    }

    /**
     * Constructor
     *
     * @param module module that is used to generate the output values
     */
    public Cylinder(Module module) {
        this.module = module;
    }

    /**
     * returns the noise module that is used to generate the output values
     *
     * @return
     */
    public Module getModule() {
        return module;
    }

    /**
     * sets the noise module that is used to generate the output value
     *
     * @param module
     */
    public void setModule(Module module) {
        this.module = module;
    }

    /**
     * Returns the output value from the noise module given the (angle, height)
     * coordinates of the specified input value located on the surface of the
     * cylinder.
     *
     * @param angle The angle around the cylinder's center, in degrees.
     * @param height The height along the @a y axis.
     *
     * @returns The output value from the noise module.
     *
     * @pre A noise module was passed to the SetModule() method.
     *
     * This output value is generated by the noise module passed to the
     * SetModule() method.
     *
     * This cylinder has a radius of 1.0 unit and has infinite height. It is
     * oriented along the @a y axis. Its center is located at the origin.
     */
    public double getValue(double angle, double height) {
        double x = Math.cos(angle * Globals.DEG_TO_RAD);
        double y = height;
        double z = Math.sin(angle * Globals.DEG_TO_RAD);
        return module.getValue(x, y, z);
    }

}
