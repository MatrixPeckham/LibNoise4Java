/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.examples;

import com.matrixpeckham.libnoise.module.Add;
import com.matrixpeckham.libnoise.module.Billow;
import com.matrixpeckham.libnoise.module.Module;
import com.matrixpeckham.libnoise.module.ScaleBias;
import com.matrixpeckham.libnoise.module.Turbulence;
import com.matrixpeckham.libnoise.module.Voronoi;
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
public class TextureGranite {

    // Height of the texture.
    final static int TEXTURE_HEIGHT = 256;

    public static void main(String[] args) {
        // Primary granite texture.  This generates the "roughness" of the texture
        // when lit by a light source.
        Billow primaryGranite = new Billow();
        primaryGranite.setSeed(0);
        primaryGranite.setFrequency(8.0);
        primaryGranite.setPersistence(0.625);
        primaryGranite.setLacunarity(2.18359375);
        primaryGranite.setOctaveCount(6);
        primaryGranite.setNoiseQuality(NoiseQuality.STD);
        // Use Voronoi polygons to produce the small grains for the granite texture.
        Voronoi baseGrains = new Voronoi();
        baseGrains.setSeed(1);
        baseGrains.setFrequency(16.0);
        baseGrains.enableDistance(true);
        // Scale the small grain values so that they may be added to the base
        // granite texture.  Voronoi polygons normally generate pits, so apply a
        // negative scaling factor to produce bumps instead.
        ScaleBias scaledGrains = new ScaleBias();
        scaledGrains.setSourceModule(0, baseGrains);
        scaledGrains.setScale(-0.5);
        scaledGrains.setBias(0.0);
        // Combine the primary granite texture with the small grain texture.
        Add combinedGranite = new Add();
        combinedGranite.setSourceModule(0, primaryGranite);
        combinedGranite.setSourceModule(1, scaledGrains);
        // Finally, perturb the granite texture to add realism.
        Turbulence finalGranite = new Turbulence();
        finalGranite.setSourceModule(0, combinedGranite);
        finalGranite.setSeed(2);
        finalGranite.setFrequency(4.0);
        finalGranite.setPower(1.0 / 8.0);
        finalGranite.setRoughness(6);

        // Given the granite noise module, create a non-seamless texture map, a
        // seamless texture map, and a spherical texture map.
        createPlanarTexture(finalGranite, false, TEXTURE_HEIGHT,
                "textureplane.png");
        createPlanarTexture(finalGranite, true, TEXTURE_HEIGHT,
                "textureseamless.png");
        createSphericalTexture(finalGranite, TEXTURE_HEIGHT,
                "texturesphere.png");

    }

    // Creates the color gradients for the texture.
    static void createTextureColor(RendererImage renderer) {
        // Create a gray granite palette.  Black and pink appear at either ends of
        // the palette; those colors provide the charactistic black and pink flecks
        // in granite.
        renderer.clearGradient();
        renderer.addGradientPoint(-1.0000, new Color(0, 0, 0, 255));
        renderer.addGradientPoint(-0.9375, new Color(0, 0, 0, 255));
        renderer.addGradientPoint(-0.8750, new Color(216, 216, 242, 255));
        renderer.addGradientPoint(0.0000, new Color(191, 191, 191, 255));
        renderer.addGradientPoint(0.5000, new Color(210, 116, 125, 255));
        renderer.addGradientPoint(0.7500, new Color(210, 113, 98, 255));
        renderer.addGradientPoint(1.0000, new Color(255, 176, 192, 255));
    }

    // Given a noise module, this function renders a flat square texture map and
// writes it to a Windows bitmap (*.bmp) file.  Because the texture map is
// square, its width is equal to its height.  The texture map can be seamless
// (tileable) or non-seamless.
    static void createPlanarTexture(Module noiseModule, boolean seamless,
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
    static void createSphericalTexture(Module noiseModule, int height,
            String filename) {
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

    // Given a noise map, this function renders a texture map and writes it to a
// Windows bitmap (*.bmp) file.
    static void renderTexture(NoiseMap noiseMap, String filename) {
        // Create the color gradients for the texture.
        RendererImage textureRenderer = new RendererImage();
        createTextureColor(textureRenderer);
        // Set up us the texture renderer and pass the noise map to it.
        Image destTexture = new Image();
        textureRenderer.setSourceNoiseMap(noiseMap);
        textureRenderer.setDestImage(destTexture);
        textureRenderer.enableLight(true);
        textureRenderer.setLightAzimuth(135.0);
        textureRenderer.setLightElev(60.0);
        textureRenderer.setLightContrast(2.0);
        textureRenderer.setLightColor(new Color(255, 255, 255, 255));

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
