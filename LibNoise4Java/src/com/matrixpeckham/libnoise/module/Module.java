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
package com.matrixpeckham.libnoise.module;

import com.matrixpeckham.libnoise.util.exceptions.ExceptionNoModule;

/**
 * Top Level Module Interface for the noise library. All producers of noise will
 * implement this interface in some way.
 *
 * @author William Matrix Peckham
 */
public interface Module {

    /**
     * Returns a reference to a source module connected to this noise module.
     *
     * @param index The index value assigned to the source module.
     *
     * @return A reference to the source module.
     *
     * @noise.pre The index value ranges from 0 to one less than the number of
     * source modules required by this noise module.
     * @noise.pre A source module with the specified index value has been added
     * to this noise module via a call to SetSourceModule().
     *
     * @throws ExceptionNoModule See the preconditions for more information.
     *
     * Each noise module requires the attachment of a certain number of source
     * modules before an application can call the GetValue() method.
     */
    public Module getSourceModule(int index);

    /**
     * Returns the number of source modules required by this noise module.
     *
     * @return The number of source modules required by this noise module.
     */
    public int getSourceModuleCount();

    /**
     * Generates an output value given the coordinates of the specified input
     * value.
     *
     * @param x The @a x coordinate of the input value.
     * @param y The @a y coordinate of the input value.
     * @param z The @a z coordinate of the input value.
     *
     * @return The output value.
     *
     * @noise.pre All source modules required by this noise module have been
     * passed to the SetSourceModule() method.
     *
     * Before an application can call this method, it must first connect all
     * required source modules via the SetSourceModule() method. If these source
     * modules are not connected to this noise module, this method raises a
     * debug assertion.
     *
     * To determine the number of source modules required by this noise module,
     * call the GetSourceModuleCount() method.
     */
    public double getValue(double x, double y, double z);

    /**
     * Sets a child module at a specific index.
     *
     * @param index
     * @param source
     */
    public void setSourceModule(int index, Module source);

    /**
     * gets a displayable name for this module.
     *
     * @return
     */
    public String getName();

    /**
     * sets a displayable name for this module
     *
     * @param name
     */
    public void setName(String name);
}
