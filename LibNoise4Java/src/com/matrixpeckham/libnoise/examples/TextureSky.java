/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.examples;

import com.matrixpeckham.libnoise.module.Module;
import com.matrixpeckham.libnoise.module.generator.Billow;
import com.matrixpeckham.libnoise.module.generator.Voronoi;
import com.matrixpeckham.libnoise.module.transform.ScalePoint;
import com.matrixpeckham.libnoise.module.transform.Turbulence;
import com.matrixpeckham.libnoise.util.Color;
import com.matrixpeckham.libnoise.util.Image;
import com.matrixpeckham.libnoise.util.NoiseMap;
import com.matrixpeckham.libnoise.util.NoiseMapBuilderPlane;
import com.matrixpeckham.libnoise.util.NoiseMapBuilderSphere;
import static com.matrixpeckham.libnoise.util.NoiseQuality.BEST;
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
public class TextureSky {
// Height of the texture.

    static final int TEXTURE_HEIGHT = 256;

    public static void main(String[] args) {
  // This texture map is made up two layers.  The bottom layer is a wavy water
        // texture.  The top layer is a cloud texture.  These two layers are
        // combined together to create the final texture map.

  // Lower layer: water texture
        // --------------------------
        // Base of the water texture.  The Voronoi polygons generate the waves.  At
        // the center of the polygons, the values are at their lowest.  At the edges
        // of the polygons, the values are at their highest.  The values smoothly
        // change between the center and the edges of the polygons, producing a
        // wave-like effect.
        Voronoi baseWater = new Voronoi();
        baseWater.setSeed(0);
        baseWater.setFrequency(8.0);
        baseWater.enableDistance(true);
        baseWater.setDisplacement(0.0);

        // Stretch the waves along the z axis.
        ScalePoint baseStretchedWater = new ScalePoint();
        baseStretchedWater.setSourceModule(0, baseWater);
        baseStretchedWater.setScale(1.0, 1.0, 3.0);

        // Smoothly perturb the water texture for more realism.
        Turbulence finalWater = new Turbulence();
        finalWater.setSourceModule(0, baseStretchedWater);
        finalWater.setSeed(1);
        finalWater.setFrequency(8.0);
        finalWater.setPower(1.0 / 32.0);
        finalWater.setRoughness(1);

  // Upper layer: cloud texture
        // --------------------------
        // Base of the cloud texture.  The billowy noise produces the basic shape
        // of soft, fluffy clouds.
        Billow cloudBase = new Billow();
        cloudBase.setSeed(2);
        cloudBase.setFrequency(2.0);
        cloudBase.setPersistence(0.375);
        cloudBase.setLacunarity(2.12109375);
        cloudBase.setOctaveCount(4);
        cloudBase.setNoiseQuality(BEST);

        // Perturb the cloud texture for more realism.
        Turbulence finalClouds = new Turbulence();
        finalClouds.setSourceModule(0, cloudBase);
        finalClouds.setSeed(3);
        finalClouds.setFrequency(16.0);
        finalClouds.setPower(1.0 / 64.0);
        finalClouds.setRoughness(2);

        // Given the water and cloud noise modules, create a non-seamless texture
        // map, a seamless texture map, and a spherical texture map.
        createPlanarTexture(finalWater, finalClouds, false, TEXTURE_HEIGHT,
                "sky-textureplane.png");
        createPlanarTexture(finalWater, finalClouds, true, TEXTURE_HEIGHT,
                "sky-textureseamless.png");
        createSphericalTexture(finalWater, finalClouds, TEXTURE_HEIGHT,
                "sky-texturesphere.png");

    }

    static void createTextureColorLayer1(RendererImage renderer) {
        // Create a water palette with varying shades of blue.
        renderer.clearGradient();
        renderer.addGradientPoint(-1.00, new Color(48, 64, 192, 255));
        renderer.addGradientPoint(0.50, new Color(96, 192, 255, 255));
        renderer.addGradientPoint(1.00, new Color(255, 255, 255, 255));
    }

    static void createTextureColorLayer2(RendererImage renderer) {
        // Create an entirely white palette with varying alpha (transparency) values
        // for the clouds.  These transparent values allows the water to show
        // through.
        renderer.clearGradient();
        renderer.addGradientPoint(-1.00, new Color(255, 255, 255, 0));
        renderer.addGradientPoint(-0.50, new Color(255, 255, 255, 0));
        renderer.addGradientPoint(1.00, new Color(255, 255, 255, 255));
    }

    static void createPlanarTexture(final Module lowerNoiseModule,
            final Module upperNoiseModule, boolean seamless, int height,
            final String filename) {
        // Map the output values from both noise module onto two planes.  This will
        // create two two-dimensional noise maps which can be rendered as two flat
        // texture maps.
        NoiseMapBuilderPlane plane = new NoiseMapBuilderPlane();
        NoiseMap lowerNoiseMap = new NoiseMap();
        NoiseMap upperNoiseMap = new NoiseMap();
        plane.setBounds(-1.0, 1.0, -1.0, 1.0);
        plane.setDestSize(height, height);
        plane.enableSeamless(seamless);

        // Generate the lower noise map.
        plane.setSourceModule(lowerNoiseModule);
        plane.setDestNoiseMap(lowerNoiseMap);
        plane.build();

        // Generate the upper noise map.
        plane.setSourceModule(upperNoiseModule);
        plane.setDestNoiseMap(upperNoiseMap);
        plane.build();

        // Given these two noise maps, render the lower texture map, then render the
        // upper texture map on top of the lower texture map.
        RenderTexture(lowerNoiseMap, upperNoiseMap, filename);
    }

    static void createSphericalTexture(final Module lowerNoiseModule,
            final Module upperNoiseModule, int height, final String filename) {
        // Map the output values from both noise module onto two spheres.  This will
        // create two two-dimensional noise maps which can be rendered as two
        // spherical texture maps.
        NoiseMapBuilderSphere sphere = new NoiseMapBuilderSphere();
        NoiseMap lowerNoiseMap = new NoiseMap();
        NoiseMap upperNoiseMap = new NoiseMap();
        sphere.setBounds(-90.0, 90.0, -180.0, 180.0); // degrees
        sphere.setDestSize(height * 2, height);

        // Generate the lower noise map.
        sphere.setSourceModule(lowerNoiseModule);
        sphere.setDestNoiseMap(lowerNoiseMap);
        sphere.build();

        // Generate the upper noise map.
        sphere.setSourceModule(upperNoiseModule);
        sphere.setDestNoiseMap(upperNoiseMap);
        sphere.build();

        // Given these two noise maps, render the lower texture map, then render the
        // upper texture map on top of the lower texture map.
        RenderTexture(lowerNoiseMap, upperNoiseMap, filename);
    }

    static void RenderTexture(final NoiseMap lowerNoiseMap,
            final NoiseMap upperNoiseMap, final String filename) {
        // Create the color gradients for the lower texture.
        RendererImage textureRenderer = new RendererImage();
        createTextureColorLayer1(textureRenderer);

        // set up us the texture renderer and pass the lower noise map to it.
        Image destTexture = new Image();
        textureRenderer.setSourceNoiseMap(lowerNoiseMap);
        textureRenderer.setDestImage(destTexture);
        textureRenderer.enableLight(true);
        textureRenderer.setLightAzimuth(135.0);
        textureRenderer.setLightElev(60.0);
        textureRenderer.setLightContrast(2.0);
        textureRenderer.setLightColor(new Color(255, 255, 255, 0));

        // Render the texture.
        textureRenderer.render();

        // Create the color gradients for the upper texture.
        createTextureColorLayer2(textureRenderer);

        // set up us the texture renderer and pass the upper noise map to it.  Also
        // use the lower texture map as a background so that the upper texture map
        // can be rendered on top of the lower texture map.
        textureRenderer.setSourceNoiseMap(upperNoiseMap);
        textureRenderer.setBackgroundImage(destTexture);
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
