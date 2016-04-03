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
import com.matrixpeckham.libnoise.util.Vec3;

/**
 * Model that defines the surface of a sphere.
 *
 * <img src="modelsphere.png" alt="MODEL_SPHERE_IMAGE" />
 *
 * This model returns an output value from a noise module given the coordinates
 * of an input value located on the surface of a sphere.
 *
 * To generate an output value, pass the (latitude, longitude) coordinates of an
 * input value to the GetValue() method.
 *
 * This model is useful for creating: - seamless textures that can be mapped
 * onto a sphere - terrain height maps for entire planets
 *
 * This sphere has a radius of 1.0 unit and its center is located at the origin.
 *
 * @author William Matrix Peckham
 */
public class Sphere {

    private Module module;

    public Sphere() {
    }

    public Sphere(Module m) {
        module = m;
    }

    /**
     * Returns the output value from the noise module given the (latitude,
     * longitude) coordinates of the specified input value located on the
     * surface of the sphere.
     *
     * @param lat The latitude of the input value, in degrees.
     * @param lon The longitude of the input value, in degrees.
     *
     * @returns The output value from the noise module.
     *
     * @pre A noise module was passed to the SetModule() method.
     *
     * This output value is generated by the noise module passed to the
     * SetModule() method.
     *
     * Use a negative latitude if the input value is located on the southern
     * hemisphere.
     *
     * Use a negative longitude if the input value is located on the western
     * hemisphere.
     */
    public double getValue(double lat, double lon) {

        Vec3 v = Globals.latLonToXYZ(lat, lon);
        return module.getValue(v.x, v.y, v.z);
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }
}
