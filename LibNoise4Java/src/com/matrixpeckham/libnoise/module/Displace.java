/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module;

import com.matrixpeckham.libnoise.util.exceptions.ExceptionNoModule;

/**
 * Noise module that uses three source modules to displace each coordinate of
 * the input value before returning the output value from a source module.
 *
 * <img src="moduledisplace.png" alt="MODULE_DISPLACE_IMAGE" />
 *
 * Unlike most other noise modules, the index value assigned to a source module
 * determines its role in the displacement operation:
 *
 * - Source module 0 (left in the diagram) outputs a value.
 *
 * - Source module 1 (lower left in the diagram) specifies the offset to apply
 * to the @a x coordinate of the input value.
 *
 * - Source module 2 (lower center in the diagram) specifies the offset to apply
 * to the @a y coordinate of the input value.
 *
 * - Source module 3 (lower right in the diagram) specifies the offset to apply
 * to the @a z coordinate of the input value.
 *
 * The GetValue() method modifies the ( @a x, @a y, @a z ) coordinates of the
 * input value using the output values from the three displacement modules
 * before retrieving the output value from the source module.
 *
 * The noise::module::Turbulence noise module is a special case of the
 * displacement module; internally, there are three Perlin-noise modules that
 * perform the displacement operation.
 *
 * This noise module requires four source modules.
 *
 * @author William Matrix Peckham
 */
public class Displace extends Module {

    @Override
    public int getSourceModuleCount() {
        return 4;
    }

    @Override
    public double getValue(double x, double y, double z) {

        //get the output values from the three displacement modules. add each
        //value to the corresponding coordinte in the input value.
        double xD = x + sourceModule[1].getValue(x, y, z);
        double yD = y + sourceModule[2].getValue(x, y, z);
        double zD = z + sourceModule[3].getValue(x, y, z);

        //retrieve the output value using the offsetted input value instead of
        // the original input value
        return sourceModule[0].getValue(xD, yD, zD);
    }

    /**
     * Returns the @a x displacement module.
     *
     * @returns A reference to the @a x displacement module.
     *
     * @pre This displacement module has been added to this noise module via a
     * call to SetSourceModule() or SetXDisplaceModule().
     *
     * @throw noise::ExceptionNoModule See the preconditions for more
     * information.
     *
     * The GetValue() method displaces the input value by adding the output
     * value from this displacement module to the @a x coordinate of the input
     * value before returning the output value from the source module.
     */
    public Module getXDisplaceModule() {
        if (sourceModule == null || sourceModule[1] == null) {
            throw new ExceptionNoModule();
        }
        return sourceModule[1];
    }

    /**
     * Returns the @a y displacement module.
     *
     * @returns A reference to the @a y displacement module.
     *
     * @pre This displacement module has been added to this noise module via a
     * call to SetSourceModule() or SetYDisplaceModule().
     *
     * @throw noise::ExceptionNoModule See the preconditions for more
     * information.
     *
     * The GetValue() method displaces the input value by adding the output
     * value from this displacement module to the @a y coordinate of the input
     * value before returning the output value from the source module.
     */
    public Module getyDisplaceModule() {
        if (sourceModule == null || sourceModule[2] == null) {
            throw new ExceptionNoModule();
        }
        return sourceModule[2];
    }

    /**
     * Returns the @a z displacement module.
     *
     * @returns A reference to the @a z displacement module.
     *
     * @pre This displacement module has been added to this noise module via a
     * call to SetSourceModule() or SetZDisplaceModule().
     *
     * @throw noise::ExceptionNoModule See the preconditions for more
     * information.
     *
     * The GetValue() method displaces the input value by adding the output
     * value from this displacement module to the @a z coordinate of the input
     * value before returning the output value from the source module.
     */
    public Module getZDisplaceModule() {
        if (sourceModule == null || sourceModule[3] == null) {
            throw new ExceptionNoModule();
        }
        return sourceModule[3];
    }

    /**
     * Sets the @a x, @a y, and @a z displacement modules.
     *
     * @param x Displacement module that displaces the @a x coordinate of the
     * input value.
     * @param y Displacement module that displaces the @a y coordinate of the
     * input value.
     * @param z Displacement module that displaces the @a z coordinate of the
     * input value.
     *
     * The GetValue() method displaces the input value by adding the output
     * value from each of the displacement modules to the corresponding
     * coordinates of the input value before returning the output value from the
     * source module.
     *
     * This method assigns an index value of 1 to the @a x displacement module,
     * an index value of 2 to the @a y displacement module, and an index value
     * of 3 to the @a z displacement module.
     *
     * These displacement modules must exist throughout the lifetime of this
     * noise module unless another displacement module replaces it.
     */
    public void setDisplaceModules(Module x, Module y, Module z) {
        setXDisplaceModule(x);
        setYDisplaceModule(y);
        setZDisplaceModule(z);
    }

    /**
     * Sets the @a x displacement module.
     *
     * @param xDisplaceModule Displacement module that displaces the @a x
     * coordinate.
     *
     * The GetValue() method displaces the input value by adding the output
     * value from this displacement module to the @a x coordinate of the input
     * value before returning the output value from the source module.
     *
     * This method assigns an index value of 1 to the @a x displacement module.
     * Passing this displacement module to this method produces the same results
     * as passing this displacement module to the SetSourceModule() method while
     * assigning it an index value of 1.
     *
     * This displacement module must exist throughout the lifetime of this noise
     * module unless another displacement module replaces it.
     */
    public void setXDisplaceModule(Module xDisplaceModule) {
        sourceModule[1] = xDisplaceModule;
    }

    /**
     * Sets the @a y displacement module.
     *
     * @param yDisplaceModule Displacement module that displaces the @a y
     * coordinate.
     *
     * The GetValue() method displaces the input value by adding the output
     * value from this displacement module to the @a y coordinate of the input
     * value before returning the output value from the source module.
     *
     * This method assigns an index value of 1 to the @a y displacement module.
     * Passing this displacement module to this method produces the same results
     * as passing this displacement module to the SetSourceModule() method while
     * assigning it an index value of 1.
     *
     * This displacement module must exist throughout the lifetime of this noise
     * module unless another displacement module replaces it.
     */
    public void setYDisplaceModule(Module yDisplaceModule) {
        sourceModule[2] = yDisplaceModule;
    }

    /**
     * Sets the @a z displacement module.
     *
     * @param zDisplaceModule Displacement module that displaces the @a z
     * coordinate.
     *
     * The GetValue() method displaces the input value by adding the output
     * value from this displacement module to the @a z coordinate of the input
     * value before returning the output value from the source module.
     *
     * This method assigns an index value of 1 to the @a z displacement module.
     * Passing this displacement module to this method produces the same results
     * as passing this displacement module to the SetSourceModule() method while
     * assigning it an index value of 1.
     *
     * This displacement module must exist throughout the lifetime of this noise
     * module unless another displacement module replaces it.
     */
    public void setZDisplaceModule(Module zDisplaceModule) {
        sourceModule[3] = zDisplaceModule;
    }

}
