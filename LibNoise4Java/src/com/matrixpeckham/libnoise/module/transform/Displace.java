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

package com.matrixpeckham.libnoise.module.transform;

import com.matrixpeckham.libnoise.module.AbstractModule;
import com.matrixpeckham.libnoise.module.Module;
import com.matrixpeckham.libnoise.util.exceptions.ExceptionNoModule;
import java.util.logging.Logger;

/**
 * Noise module that uses three source modules to displace each coordinate of
 * the input value before returning the output value from a source module.
 *
 * @image html moduledisplace.png
 *
 * Unlike most other noise modules, the index value assigned to a source module
 * determines its role in the displacement operation: - Source module 0 (left in
 * the diagram) outputs a value. - Source module 1 (lower left in the diagram)
 * specifies the offset to apply to the @a x coordinate of the input value. -
 * Source module 2 (lower center in the diagram) specifies the offset to apply
 * to the @a y coordinate of the input value. - Source module 3 (lower right in
 * the diagram) specifies the offset to apply to the @a z coordinate of the
 * input value.
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
 */
public class Displace extends AbstractModule {

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
     * @return A reference to the @a x displacement module.
     *
     * @noise.pre This displacement module has been added to this noise module
     * via a call to SetSourceModule() or SetXDisplaceModule().
     *
     * @throws ExceptionNoModule See the preconditions for more information.
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
     * Returns the @a z displacement module.
     *
     * @return A reference to the @a z displacement module.
     *
     * @noise.pre This displacement module has been added to this noise module
     * via a call to SetSourceModule() or SetZDisplaceModule().
     *
     * @throws ExceptionNoModule See the preconditions for more information.
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
     * Returns the @a y displacement module.
     *
     * @return A reference to the @a y displacement module.
     *
     * @noise.pre This displacement module has been added to this noise module
     * via a call to SetSourceModule() or SetYDisplaceModule().
     *
     * @throws ExceptionNoModule See the preconditions for more information.
     *
     * The GetValue() method displaces the input value by adding the output
     * value from this displacement module to the @a y coordinate of the input
     * value before returning the output value from the source module.
     */
    public Module getYDisplaceModule() {
        if (sourceModule == null || sourceModule[2] == null) {
            throw new ExceptionNoModule();
        }
        return sourceModule[2];
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
    public void setDisplaceModules(Module x, Module y,
            Module z) {
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

    private static final Logger LOG = Logger.getLogger(Displace.class.getName());

}
