/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.examples;

import com.matrixpeckham.libnoise.module.Billow;
import com.matrixpeckham.libnoise.module.Checkerboard;
import com.matrixpeckham.libnoise.module.Const;
import com.matrixpeckham.libnoise.module.Cylinders;
import com.matrixpeckham.libnoise.module.Module;
import com.matrixpeckham.libnoise.module.Perlin;
import com.matrixpeckham.libnoise.module.RidgedMulti;
import com.matrixpeckham.libnoise.module.Spheres;
import com.matrixpeckham.libnoise.module.Voronoi;
import com.matrixpeckham.libnoise.util.Color;
import com.matrixpeckham.libnoise.util.Image;
import com.matrixpeckham.libnoise.util.NoiseMap;
import com.matrixpeckham.libnoise.util.NoiseMapBuilderPlane;
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
public class MakeGenerators {

    static Module[] generators = new Module[]{
        new Billow(),
        new Checkerboard(),
        new Const(),
        new Cylinders(),
        new Perlin(),
        new RidgedMulti(),
        new Spheres(),
        new Voronoi()
    };

    public static void main(String[] args) {
        for (Module m : generators) {
            createPlanarTexture(m, false, 256, m.getClass().getSimpleName()
                    + ".png");
        }
    }

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

    static void renderTexture(NoiseMap noiseMap, String filename) {
        // Create the color gradients for the texture.
        RendererImage textureRenderer = new RendererImage();
        textureRenderer.buildGrayscaleGradient();
        // Set up us the texture renderer and pass the noise map to it.
        Image destTexture = new Image();
        textureRenderer.setSourceNoiseMap(noiseMap);
        textureRenderer.setDestImage(destTexture);
        textureRenderer.enableLight(false);
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
