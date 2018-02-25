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

package com.matrixpeckham.libnoise.examples;

import com.matrixpeckham.libnoise.module.Module;
import com.matrixpeckham.libnoise.module.generator.Billow;
import com.matrixpeckham.libnoise.module.generator.RidgedMulti;
import com.matrixpeckham.libnoise.module.modifier.ScaleBias;
import com.matrixpeckham.libnoise.module.selector.Select;
import com.matrixpeckham.libnoise.module.transform.Turbulence;
import com.matrixpeckham.libnoise.util.Color;
import com.matrixpeckham.libnoise.util.Image;
import com.matrixpeckham.libnoise.util.NoiseMap;
import com.matrixpeckham.libnoise.util.NoiseMapBuilderPlane;
import com.matrixpeckham.libnoise.util.NoiseMapBuilderSphere;
import static com.matrixpeckham.libnoise.util.NoiseQuality.BEST;
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
public class TextureSlime {
// Height of the texture.

    final static int TEXTURE_HEIGHT = 256;

    public static void main(String[] args) {
        // Large slime bubble texture.
        Billow largeSlime = new Billow();
        largeSlime.setSeed(0);
        largeSlime.setFrequency(4.0);
        largeSlime.setLacunarity(2.12109375);
        largeSlime.setOctaveCount(1);
        largeSlime.setNoiseQuality(BEST);

        // Base of the small slime bubble texture.  This texture will eventually
        // appear inside cracks in the large slime bubble texture.
        Billow smallSlimeBase = new Billow();
        smallSlimeBase.setSeed(1);
        smallSlimeBase.setFrequency(24.0);
        smallSlimeBase.setLacunarity(2.14453125);
        smallSlimeBase.setOctaveCount(1);
        smallSlimeBase.setNoiseQuality(BEST);

        // Scale and lower the small slime bubble values.
        ScaleBias smallSlime = new ScaleBias();
        smallSlime.setSourceModule(0, smallSlimeBase);
        smallSlime.setScale(0.5);
        smallSlime.setBias(-0.5);

        // Create a map that specifies where the large and small slime bubble
        // textures will appear in the final texture map.
        RidgedMulti slimeMap = new RidgedMulti();
        slimeMap.setSeed(0);
        slimeMap.setFrequency(2.0);
        slimeMap.setLacunarity(2.20703125);
        slimeMap.setOctaveCount(3);
        slimeMap.setNoiseQuality(STD);

        // Choose between the large or small slime bubble textures depending on the
        // corresponding value from the slime map.  Choose the small slime bubble
        // texture if the slime map value is within a narrow range of values,
        // otherwise choose the large slime bubble texture.  The edge falloff is
        // non-zero so that there is a smooth transition between the two textures.
        Select slimeChooser = new Select();
        slimeChooser.setSourceModule(0, largeSlime);
        slimeChooser.setSourceModule(1, smallSlime);
        slimeChooser.setControlModule(slimeMap);
        slimeChooser.setBounds(-0.375, 0.375);
        slimeChooser.setEdgeFalloff(0.5);

        // Finally, perturb the slime texture to add realism.
        Turbulence finalSlime = new Turbulence();
        finalSlime.setSourceModule(0, slimeChooser);
        finalSlime.setSeed(2);
        finalSlime.setFrequency(8.0);
        finalSlime.setPower(1.0 / 32.0);
        finalSlime.setRoughness(2);

        // Given the slime noise module, create a non-seamless texture map, a
        // seamless texture map, and a spherical texture map.
        createPlanarTexture(finalSlime, false, TEXTURE_HEIGHT,
                "slime-textureplane.png");
        createPlanarTexture(finalSlime, true, TEXTURE_HEIGHT,
                "slime-textureseamless.png");
        createSphericalTexture(finalSlime, TEXTURE_HEIGHT,
                "slime-texturesphere.png");

    }

    static void createTextureColor(RendererImage renderer) {
        // Create a green slime palette.  A dirt brown color is used for very low
        // values while green is used for the rest of the values.
        renderer.clearGradient();
        renderer.addGradientPoint(-1.0000, new Color(160, 64, 42, 255));
        renderer.addGradientPoint(0.0000, new Color(64, 192, 64, 255));
        renderer.addGradientPoint(1.0000, new Color(128, 255, 128, 255));
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
        textureRenderer.enableLight(true);
        textureRenderer.setLightAzimuth(135.0);
        textureRenderer.setLightElev(60.0);
        textureRenderer.setLightContrast(2.0);
        textureRenderer.setLightColor(new Color(255, 255, 255, 0));

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
