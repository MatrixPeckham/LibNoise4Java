/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.examples;

import com.matrixpeckham.libnoise.module.Module;
import com.matrixpeckham.libnoise.module.combiner.Add;
import com.matrixpeckham.libnoise.module.generator.Cylinders;
import com.matrixpeckham.libnoise.module.generator.RidgedMulti;
import com.matrixpeckham.libnoise.module.modifier.ScaleBias;
import com.matrixpeckham.libnoise.module.transform.RotatePoint;
import com.matrixpeckham.libnoise.module.transform.Turbulence;
import com.matrixpeckham.libnoise.util.Color;
import com.matrixpeckham.libnoise.util.Image;
import com.matrixpeckham.libnoise.util.NoiseMap;
import com.matrixpeckham.libnoise.util.NoiseMapBuilderPlane;
import com.matrixpeckham.libnoise.util.NoiseMapBuilderSphere;
import static com.matrixpeckham.libnoise.util.NoiseQuality.STD;
import com.matrixpeckham.libnoise.util.RendererImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author William Matrix Peckham
 */
public class TextureJade {

// Height of the texture.
    static final int TEXTURE_HEIGHT = 256;

    public static void main(String[] args) {
        // Primary jade texture.  The ridges from the ridged-multifractal module
        // produces the veins.
        RidgedMulti primaryJade = new RidgedMulti();
        primaryJade.setSeed(0);
        primaryJade.setFrequency(2.0);
        primaryJade.setLacunarity(2.20703125);
        primaryJade.setOctaveCount(6);
        primaryJade.setNoiseQuality(STD);

        // Base of the secondary jade texture.  The base texture uses concentric
        // cylinders aligned on the z axis, which will eventually be perturbed.
        Cylinders baseSecondaryJade = new Cylinders();
        baseSecondaryJade.setFrequency(2.0);

        // Rotate the base secondary jade texture so that the cylinders are not
        // aligned with any axis.  This produces more variation in the secondary
        // jade texture since the texture is parallel to the y-axis.
        RotatePoint rotatedBaseSecondaryJade = new RotatePoint();
        rotatedBaseSecondaryJade.setSourceModule(0, baseSecondaryJade);
        rotatedBaseSecondaryJade.setAngles(90.0, 25.0, 5.0);

        // Slightly perturb the secondary jade texture for more realism.
        Turbulence perturbedBaseSecondaryJade = new Turbulence();
        perturbedBaseSecondaryJade.setSourceModule(0, rotatedBaseSecondaryJade);
        perturbedBaseSecondaryJade.setSeed(1);
        perturbedBaseSecondaryJade.setFrequency(4.0);
        perturbedBaseSecondaryJade.setPower(1.0 / 4.0);
        perturbedBaseSecondaryJade.setRoughness(4);

        // Scale the secondary jade texture so it contributes a small part to the
        // final jade texture.
        ScaleBias secondaryJade = new ScaleBias();
        secondaryJade.setSourceModule(0, perturbedBaseSecondaryJade);
        secondaryJade.setScale(0.25);
        secondaryJade.setBias(0.0);

        // Add the two jade textures together.  These two textures were produced
        // using different combinations of coherent noise, so the final texture will
        // have a lot of variation.
        Add combinedJade = new Add();
        combinedJade.setSourceModule(0, primaryJade);
        combinedJade.setSourceModule(1, secondaryJade);

        // Finally, perturb the combined jade textures to produce the final jade
        // texture.  A low roughness produces nice veins.
        Turbulence finalJade = new Turbulence();
        finalJade.setSourceModule(0, combinedJade);
        finalJade.setSeed(2);
        finalJade.setFrequency(4.0);
        finalJade.setPower(1.0 / 16.0);
        finalJade.setRoughness(2);

        // Given the jade noise module, create a non-seamless texture map, a
        // seamless texture map, and a spherical texture map.
        createPlanarTexture(finalJade, false, TEXTURE_HEIGHT,
                "jade-textureplane.png");
        createPlanarTexture(finalJade, true, TEXTURE_HEIGHT,
                "jade-textureseamless.png");
        createSphericalTexture(finalJade, TEXTURE_HEIGHT,
                "jade-texturesphere.png");

    }

    static void createTextureColor(RendererImage renderer) {
        // Create a nice jade palette.
        renderer.clearGradient();
        renderer.addGradientPoint(-1.000, new Color(24, 146, 102, 255));
        renderer.addGradientPoint(0.000, new Color(78, 154, 115, 255));
        renderer.addGradientPoint(0.250, new Color(128, 204, 165, 255));
        renderer.addGradientPoint(0.375, new Color(78, 154, 115, 255));
        renderer.addGradientPoint(1.000, new Color(29, 135, 102, 255));
    }

    static void createPlanarTexture(final Module noiseModule, boolean seamless,
            int height, final String filename) {
        // Map the output values from the noise module onto a plane.  This will
        // create a two-dimensional noise map which can be rendered as a flat
        // texture map.
        NoiseMapBuilderPlane plane = new NoiseMapBuilderPlane();
        NoiseMap noiseMap = new NoiseMap();
        plane.setBounds(-1.0, 1.0, -1.0, 1.0);
        plane.setDestSize(height, height);
        plane.setSourceModule(noiseModule);
        plane.setDestNoiseMap(noiseMap);
        plane.enableSeamless(seamless);
        plane.build();

        renderTexture(noiseMap, filename);
    }

    static void createSphericalTexture(final Module noiseModule, int height,
            final String filename) {
        // Map the output values from the noise module onto a sphere.  This will
        // create a two-dimensional noise map which can be rendered as a spherical
        // texture map.
        NoiseMapBuilderSphere sphere = new NoiseMapBuilderSphere();
        NoiseMap noiseMap = new NoiseMap();
        sphere.setBounds(-90.0, 90.0, -180.0, 180.0); // degrees
        sphere.setDestSize(height * 2, height);
        sphere.setSourceModule(noiseModule);
        sphere.setDestNoiseMap(noiseMap);
        sphere.build();

        renderTexture(noiseMap, filename);
    }

    static void renderTexture(final NoiseMap noiseMap, final String filename) {
        // Create the color gradients for the texture.
        RendererImage textureRenderer = new RendererImage();
        createTextureColor(textureRenderer);

        // set up us the texture renderer and pass the noise map to it.
        Image destTexture = new Image();
        textureRenderer.setSourceNoiseMap(noiseMap);
        textureRenderer.setDestImage(destTexture);
        textureRenderer.enableLight(false);

        // Render the texture.
        textureRenderer.render();

        try {
            // Write the texture as a Windows bitmap file (*.bmp).
            //WriterBMP textureWriter;
            //textureWriter.setSourceImage(destTexture);
            //textureWriter.setDestFilename(filename);
            //textureWriter.WriteDestFile();
            File file = new File(filename);

            System.out.println(file.getAbsolutePath());

            ImageIO.write(destTexture.asBufferedImage(), "PNG", file);
        } catch (IOException ex) {
            Logger.getLogger(TextureGranite.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

}
