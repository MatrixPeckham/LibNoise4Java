/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.module;

import static com.matrixpeckham.libnoise.util.Globals.linearInterp;


public class Blend extends Module {

    /**
     * Returns the control module.
     *
     * @returns A reference to the control module.
     *
     * @pre A control module has been added to this noise module via a call to
     * SetSourceModule() or SetControlModule().
     *
     * @throw noise::ExceptionNoModule See the preconditions for more
     * information.
     *
     * The control module determines the weight of the blending operation.
     * Negative values weigh the blend towards the output value from the source
     * module with an index value of 0. Positive values weigh the blend towards
     * the output value from the source module with an index value of 1.
     * @return
     */
    public Module getControlModule() {
        return sourceModule[2];
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

}
