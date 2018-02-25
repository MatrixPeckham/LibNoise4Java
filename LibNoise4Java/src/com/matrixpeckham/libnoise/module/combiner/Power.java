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

package com.matrixpeckham.libnoise.module.combiner;

import com.matrixpeckham.libnoise.module.AbstractModule;
import static java.lang.Math.pow;
import java.util.logging.Logger;

/**
 * Noise module that raises the output value from a first source module to the
 * power of the output value from a second source module.
 *
 * <img src="modulepower.png" alt="MODULE_POWER_IMAGE" />
 *
 * The first source module must have an index value of 0.
 *
 * The second source module must have an index value of 1.
 *
 * This noise module requires two source modules.
 *
 * @author William Matrix Peckham
 */
public class Power extends AbstractModule {

    @Override
    public int getSourceModuleCount() {
        return 2;
    }

    @Override
    public double getValue(double x, double y, double z) {
        return pow(sourceModule[0].getValue(x, y, z), sourceModule[1].
                getValue(x, y, z));
    }

    private static final Logger LOG = Logger.getLogger(Power.class.getName());

}
