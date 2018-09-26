/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.matrixpeckham.libnoise.module;

/**
 *
 * @author matri
 */
public class NoiseSample {

    public double value = 0;

    public final double[] gradient = new double[6];

    public NoiseSample() {
        for (int i = 0; i < 6; i++) {
            gradient[i] = 0;
        }
    }

}
