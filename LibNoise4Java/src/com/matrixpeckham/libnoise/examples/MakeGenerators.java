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
import com.matrixpeckham.libnoise.module.combiner.Max;
import com.matrixpeckham.libnoise.module.combiner.Min;
import com.matrixpeckham.libnoise.module.combiner.Multiply;
import com.matrixpeckham.libnoise.module.combiner.Power;
import com.matrixpeckham.libnoise.module.generator.Billow;
import com.matrixpeckham.libnoise.module.generator.Checkerboard;
import com.matrixpeckham.libnoise.module.generator.Const;
import com.matrixpeckham.libnoise.module.generator.Cylinders;
import com.matrixpeckham.libnoise.module.generator.Perlin;
import com.matrixpeckham.libnoise.module.generator.RidgedMulti;
import com.matrixpeckham.libnoise.module.generator.Spheres;
import com.matrixpeckham.libnoise.module.generator.Voronoi;
import com.matrixpeckham.libnoise.module.generator.Voronoi.DistanceFunction;
import com.matrixpeckham.libnoise.module.modifier.Abs;
import com.matrixpeckham.libnoise.module.modifier.Clamp;
import com.matrixpeckham.libnoise.module.modifier.Curve;
import com.matrixpeckham.libnoise.module.modifier.Exponent;
import com.matrixpeckham.libnoise.module.modifier.Invert;
import com.matrixpeckham.libnoise.module.modifier.ScaleBias;
import com.matrixpeckham.libnoise.module.modifier.Terrace;
import com.matrixpeckham.libnoise.module.selector.Blend;
import com.matrixpeckham.libnoise.module.selector.Select;
import com.matrixpeckham.libnoise.module.transform.Displace;
import com.matrixpeckham.libnoise.module.transform.RotatePoint;
import com.matrixpeckham.libnoise.module.transform.ScalePoint;
import com.matrixpeckham.libnoise.module.transform.TranslatePoint;
import com.matrixpeckham.libnoise.module.transform.Turbulence;
import com.matrixpeckham.libnoise.util.Color;
import com.matrixpeckham.libnoise.util.Image;
import com.matrixpeckham.libnoise.util.NoiseMap;
import com.matrixpeckham.libnoise.util.NoiseMapBuilderPlane;
import com.matrixpeckham.libnoise.util.RendererImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author William Matrix Peckham
 */
public class MakeGenerators {

    static final int IMAGE_SIZE = 256;

    static final TreeMap<String, Module> modules = new TreeMap<>();

    static {
        modules.put("Billow", new Billow());
        modules.put("Checkerboard", new Checkerboard());
        modules.put("Const", new Const());
        modules.put("Cylinders", new Cylinders());
        modules.put("Perlin", new Perlin());
        modules.put("RidgedMulti", new RidgedMulti());
        modules.put("Spheres", new Spheres());
        modules.put("Voronoi", new Voronoi());

        //for (NoiseQuality quality : NoiseQuality.values()) {
//        for (double frequency = 0.5; frequency <= 2.0; frequency += 0.5) {
//            for (double lacunarity = 1.0; lacunarity <= 2.0; lacunarity
//                    += 1.0) {
//                for (double persistance = 0.5; persistance <= 1.0;
//                        persistance
//                        += 0.5) {
//                    for (int octaves = 1; octaves < 17;
//                            octaves += octaves) {
//                        String name = octaves + "-octaves-"
//                                + frequency + "-frequency-" + lacunarity
//                                + "-lacunarity-" + persistance
//                                + "-persistance";//-" + quality;
//
//                        for (double offset = 0.0; offset <= 1.0; offset += 0.5) {
//                            RidgedMulti r = new RidgedMulti();
//                            r.setFrequency(frequency);
//                            r.setOctaveCount(octaves);
//                            r.setLacunarity(lacunarity);
//                            r.setGain(persistance);
//                            r.setOffset(offset);
//                            modules.put("RidgedMulti" + name + "-" + offset
//                                    + "-offset", r);
//                        }
//                        //p.setNoiseQuality(quality);
//                        Billow b = new Billow();
//                        b.setFrequency(frequency);
//                        b.setOctaveCount(octaves);
//                        b.setLacunarity(lacunarity);
//                        //p.setNoiseQuality(quality);
//                        b.setPersistence(persistance);
//                        Perlin p = new Perlin();
//                        p.setFrequency(frequency);
//                        p.setOctaveCount(octaves);
//                        p.setLacunarity(lacunarity);
//                        //p.setNoiseQuality(quality);
//                        p.setPersistence(persistance);
//                        modules.put("Perlin-" + name, p);
//                        modules.put("Billow-" + name, b);
//                    }
//                }
//            }
//        }
        //}
        for (DistanceFunction func : Voronoi.Distances.values()) {
            for (int enable = 0; enable <= 1; enable++) {
                for (double frequency = 0.5; frequency <= 2.0; frequency += 0.5) {
                    for (double displace = 0.0; displace <= 1.0; displace += 0.5) {
                        Voronoi v = new Voronoi();
                        v.setDistFunc(func);
                        v.setDisplacement(displace);
                        v.setEnableDistance(enable != 0);
                        v.setFrequency(frequency);
                        String name = frequency + "-frequency-" + func.
                                toString() + displace + "-displace"
                                + "-" + ((enable != 0) ? "dist" : "nodist");
                        modules.put("Voronoi-" + name, v);
                    }
                }
            }
        }

        modules.put("Abs", new Abs());
        modules.get("Abs").setSourceModule(0, modules.get("Perlin"));

        Clamp c = new Clamp();
        modules.put("Clamp", c);
        c.setSourceModule(0, modules.get("Perlin"));
        c.setBounds(-0.5, 0.5);

        Curve cur = new Curve();
        modules.put("Curve", cur);
        cur.setSourceModule(0, modules.get("Perlin"));
        cur.addControlPoint(-2., -2.);
        cur.addControlPoint(-1, -1);
        cur.addControlPoint(-0.5, 0.25);
        cur.addControlPoint(0.5, 0.0);
        cur.addControlPoint(1, 1);
        cur.addControlPoint(2., 2.);

        Exponent exp = new Exponent();
        modules.put("Exponent", exp);
        exp.setSourceModule(0, modules.get("Perlin"));
        exp.setExponent(2);

        Invert inv = new Invert();
        modules.put("Invert", inv);
        inv.setSourceModule(0, modules.get("Perlin"));

        ScaleBias sb = new ScaleBias();
        modules.put("ScaleBias", sb);
        sb.setSourceModule(0, modules.get("Perlin"));
        sb.setScale(0.5);
        sb.setBias(0.5);

        Terrace ter = new Terrace();
        modules.put("Terrace", ter);
        ter.setSourceModule(0, modules.get("Perlin"));
        ter.addControlPoint(-2);
        ter.addControlPoint(-1);
        ter.addControlPoint(-0.25);
        ter.addControlPoint(-0.3);
        ter.addControlPoint(0.5);
        ter.addControlPoint(1);
        ter.addControlPoint(2);

        TranslatePoint tra = new TranslatePoint();
        modules.put("TranslatePoint", tra);
        tra.setSourceModule(0, modules.get("Checkerboard"));
        tra.setTranslation(1, 0.5, 1);

        ScalePoint sca = new ScalePoint();
        modules.put("ScalePoint", sca);
        sca.setSourceModule(0, modules.get("Perlin"));
        sca.setScale(1, 4, 1);

        RotatePoint rot = new RotatePoint();
        modules.put("RotatePoint", rot);
        rot.setSourceModule(0, modules.get("Checkerboard"));
        rot.setAngles(0, 0, 30);

        Displace disp = new Displace();
        modules.put("Displace", disp);
        disp.setDisplaceModules(modules.get("ScaleBias"), modules.get(
                "Const"), modules.get("Const"));
        disp.setSourceModule(0, modules.get("Checkerboard"));

        Turbulence turb = new Turbulence();
        modules.put("Turbulance", turb);
        turb.setSourceModule(0, modules.get("Perlin"));

        Select select = new Select();
        modules.put("Select", select);
        select.setControlModule(modules.get("Checkerboard"));
        select.setSourceModule(0, modules.get("Perlin"));
        select.setSourceModule(1, modules.get("Voronoi"));
        select.setBounds(-2, 0);

        Blend blend = new Blend();
        modules.put("Blend", blend);
        blend.setControlModule(modules.get("Perlin"));
        blend.setSourceModule(0, modules.get("Voronoi"));
        blend.setSourceModule(1, turb);

        Add add = new Add();
        modules.put("Add", add);
        add.setSourceModule(0, modules.get("Perlin"));
        add.setSourceModule(1, modules.get("Cylinders"));

        Power power = new Power();
        modules.put("Power", power);
        power.setSourceModule(0, modules.get("Perlin"));
        power.setSourceModule(1, modules.get("Cylinders"));

        Multiply mult = new Multiply();
        modules.put("Multiply", mult);
        mult.setSourceModule(0, modules.get("Perlin"));
        mult.setSourceModule(1, modules.get("Cylinders"));

        Min min = new Min();
        modules.put("Min", min);
        min.setSourceModule(0, modules.get("Perlin"));
        min.setSourceModule(1, modules.get("Cylinders"));

        Max max = new Max();
        modules.put("Max", max);
        max.setSourceModule(0, modules.get("Perlin"));
        max.setSourceModule(1, modules.get("Cylinders"));

    }

    public static void main(String[] args) {
        for (Map.Entry<String, Module> m : modules.entrySet()) {
            createPlanarTexture(m.getValue(), false, IMAGE_SIZE, m.getKey()
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
