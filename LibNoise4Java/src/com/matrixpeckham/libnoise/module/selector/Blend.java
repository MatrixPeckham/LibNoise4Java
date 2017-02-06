/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module.selector;

import com.matrixpeckham.libnoise.module.AbstractModule;
import com.matrixpeckham.libnoise.module.Module;
import static com.matrixpeckham.libnoise.util.Globals.linearInterp;
import com.matrixpeckham.libnoise.util.exceptions.ExceptionNoModule;
import java.util.logging.Logger;

/**
 * Noise module that outputs a weighted blend of the output values from two
 * source modules given the output value supplied by a control module.
 *
 * @image html moduleblend.png
 *
 * Unlike most other noise modules, the index value assigned to a source module
 * determines its role in the blending operation: - Source module 0 (upper left
 * in the diagram) outputs one of the values to blend. - Source module 1 (lower
 * left in the diagram) outputs one of the values to blend. - Source module 2
 * (bottom of the diagram) is known as the <i>control module</i>. The control
 * module determines the weight of the blending operation. Negative values weigh
 * the blend towards the output value from the source module with an index value
 * of 0. Positive values weigh the blend towards the output value from the
 * source module with an index value of 1.
 *
 * An application can pass the control module to the SetControlModule() method
 * instead of the SetSourceModule() method. This may make the application code
 * easier to read.
 *
 * This noise module uses linear interpolation to perform the blending
 * operation.
 *
 * This noise module requires three source modules.
 */
public class Blend extends AbstractModule {

    /**
     * Returns the control module.
     *
     * @return A reference to the control module.
     *
     * @noise.pre A control module has been added to this noise module via a
     * call to SetSourceModule() or SetControlModule().
     *
     * @throws ExceptionNoModule See the preconditions for more information.
     *
     * The control module determines the weight of the blending operation.
     * Negative values weigh the blend towards the output value from the source
     * module with an index value of 0. Positive values weigh the blend towards
     * the output value from the source module with an index value of 1.
     */
    public Module getControlModule() {
        return getSourceModule(2);
    }

    @Override
    public int getSourceModuleCount() {
        return 3;
    }

    @Override
    public double getValue(double x, double y, double z) {
        double v = sourceModule[0].getValue(x, y, z);
        double v1 = sourceModule[1].getValue(x, y, z);
        double alpha = (sourceModule[2].getValue(x, y, z) + 1.0) / 2.0;
        return linearInterp(v, v1, alpha);
    }

    /**
     * Sets the control module.
     *
     * @param m The control module.
     *
     * The control module determines the weight of the blending operation.
     * Negative values weigh the blend towards the output value from the source
     * module with an index value of 0. Positive values weigh the blend towards
     * the output value from the source module with an index value of 1.
     *
     * This method assigns the control module an index value of 2. Passing the
     * control module to this method produces the same results as passing the
     * control module to the SetSourceModule() method while assigning that noise
     * module an index value of 2.
     *
     * This control module must exist throughout the lifetime of this noise
     * module unless another control module replaces that control module.
     *
     */
    public void setControlModule(Module m) {
        sourceModule[2] = m;
    }

    private static final Logger LOG = Logger.getLogger(Blend.class.getName());

}
