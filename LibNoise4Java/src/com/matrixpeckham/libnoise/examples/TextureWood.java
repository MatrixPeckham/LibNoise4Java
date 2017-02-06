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
import com.matrixpeckham.libnoise.module.generator.Perlin;
import com.matrixpeckham.libnoise.module.modifier.ScaleBias;
import com.matrixpeckham.libnoise.module.transform.RotatePoint;
import com.matrixpeckham.libnoise.module.transform.ScalePoint;
import com.matrixpeckham.libnoise.module.transform.TranslatePoint;
import com.matrixpeckham.libnoise.module.transform.Turbulence;
import com.matrixpeckham.libnoise.util.Color;
import com.matrixpeckham.libnoise.util.Image;
import com.matrixpeckham.libnoise.util.NoiseMap;
import com.matrixpeckham.libnoise.util.NoiseMapBuilderPlane;
import com.matrixpeckham.libnoise.util.NoiseMapBuilderSphere;
import com.matrixpeckham.libnoise.util.NoiseQuality;
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
public class TextureWood {
// Height of the texture.

    final static int TEXTURE_HEIGHT = 256;

    public static void main(String[] args) {
        // Base wood texture.  The base texture uses concentric cylinders aligned
        // on the z axis, like a log.
        Cylinders baseWood = new Cylinders();
        baseWood.setFrequency(16.0);

        // Perlin noise to use for the wood grain.
        Perlin woodGrainNoise = new Perlin();
        woodGrainNoise.setSeed(0);
        woodGrainNoise.setFrequency(48.0);
        woodGrainNoise.setPersistence(0.5);
        woodGrainNoise.setLacunarity(2.20703125);
        woodGrainNoise.setOctaveCount(3);
        woodGrainNoise.setNoiseQuality(NoiseQuality.STD);

        // Stretch the Perlin noise in the same direction as the center of the
        // log.  This produces a nice wood-grain texture.
        ScalePoint scaledBaseWoodGrain = new ScalePoint();
        scaledBaseWoodGrain.setSourceModule(0, woodGrainNoise);
        scaledBaseWoodGrain.setyScale(0.25);

        // Scale the wood-grain values so that they may be added to the base wood
        // texture.
        ScaleBias woodGrain = new ScaleBias();
        woodGrain.setSourceModule(0, scaledBaseWoodGrain);
        woodGrain.setScale(0.25);
        woodGrain.setBias(0.125);

        // Add the wood grain texture to the base wood texture.
        Add combinedWood = new Add();
        combinedWood.setSourceModule(0, baseWood);
        combinedWood.setSourceModule(1, woodGrain);

        // Slightly perturb the wood texture for more realism.
        Turbulence perturbedWood = new Turbulence();
        perturbedWood.setSourceModule(0, combinedWood);
        perturbedWood.setSeed(1);
        perturbedWood.setFrequency(4.0);
        perturbedWood.setPower(1.0 / 256.0);
        perturbedWood.setRoughness(4);

        // Cut the wood texture a small distance from the center of the "log".
        TranslatePoint translatedWood = new TranslatePoint();
        translatedWood.setSourceModule(0, perturbedWood);
        translatedWood.setzTranslation(1.48);

        // Cut the wood texture on an angle to produce a more interesting wood
        // texture.
        RotatePoint rotatedWood = new RotatePoint();
        rotatedWood.setSourceModule(0, translatedWood);
        rotatedWood.setAngles(24.0, 0.0, 0.0);

        // Finally, perturb the wood texture to produce the final texture.
        Turbulence finalWood = new Turbulence();
        finalWood.setSourceModule(0, rotatedWood);
        finalWood.setSeed(2);
        finalWood.setFrequency(2.0);
        finalWood.setPower(1.0 / 64.0);
        finalWood.setRoughness(4);

        // Given the wood noise module, create a non-seamless texture map, a
        // seamless texture map, and a spherical texture map.
        createPlanarTexture(finalWood, false, TEXTURE_HEIGHT,
                "wood-textureplane.png");
        createPlanarTexture(finalWood, true, TEXTURE_HEIGHT,
                "wood-textureseamless.png");
        createSphericalTexture(finalWood, TEXTURE_HEIGHT,
                "wood-texturesphere.png");

    }

// Creates the color gradients for the texture.
    static void createTextureColor(RendererImage renderer) {
        // Create a dark-stained wood palette (oak?)
        renderer.clearGradient();
        renderer.addGradientPoint(-1.00, new Color(189, 94, 4, 255));
        renderer.addGradientPoint(0.50, new Color(144, 48, 6, 255));
        renderer.addGradientPoint(1.00, new Color(60, 10, 8, 255));
    }

// Given a noise module, this function renders a flat square texture map and
// writes it to a Windows bitmap (*.bmp) file.  Because the texture map is
// square, its width is equal to its height.  The texture map can be seamless
// (tileable) or non-seamless.
    static void createPlanarTexture(final Module noiseModule, boolean seamless,
            int height, String filename) {
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

// Given a noise module, this function renders a spherical texture map and
// writes it to a Windows bitmap (*.bmp) file.  The texture map's width is
// double its height.
    static void createSphericalTexture(final Module noiseModule, int height,
            final String filename
    ) {
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
