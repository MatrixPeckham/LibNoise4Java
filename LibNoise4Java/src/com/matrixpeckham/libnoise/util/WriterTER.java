/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.util;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class creates a file in Terrage Terrain (*.ter) format given the
 * contents of a noise map object. This class treats the values in the noise map
 * as elevations measured in meters.
 *
 * <a href=http://www.planetside.co.uk/terragen/>Terragen</a> is a terrain
 * application that renders realistic landscapes. Terragen is available for
 * Windows and MacOS; unfortunately, Terragen does not have UN*X versions.
 *
 * <b>Writing the noise map</b>
 * To write the noise map, perform the following steps:
 *
 * - Pass the filename to the SetDestFilename() method.
 *
 * - Pass a NoiseMap object to the SetSourceNoiseMap() method.
 *
 * - Call the WriteDestFile() method.
 *
 * The SetDestFilename() and SetSourceNoiseMap() methods must be called before
 * calling the WriteDestFile() method.
 *
 * @author William Matrix Peckham
 */
public class WriterTER {

    /**
     * pointer to the map that will be written to the file
     */
    NoiseMap sourceNoiseMap;

    /**
     * distance separating adjacent points in the noise meters.
     */
    double metersPerPoint;

    /**
     * file name to write to
     */
    String fileName;

    /**
     * constructor
     */
    public WriterTER() {
        sourceNoiseMap = null;
        metersPerPoint = Globals.DEFAULT_METERS_PER_POINT;
    }

    /**
     * Writes the contents of the noise map object to the file.
     *
     * @pre SetDestFilename() has been previously called.
     * @pre SetSourceNoiseMap() has been previously called.
     *
     * @throw noise::ExceptionInvalidParam See the preconditions.
     * @throw noise::ExceptionOutOfMemory Out of memory.
     * @throw noise::ExceptionUnknown An unknown exception occurred. Possibly
     * the file could not be written.
     *
     * This method encodes the contents of the noise map and writes it to a
     * file. Before calling this method, call the SetSourceNoiseMap() method to
     * specify the noise map, then call the SetDestFilename() method to specify
     * the name of the file to write.
     *
     * This object assumes that the noise values represent elevations in meters.
     */
    public void writeDestFile() throws FileNotFoundException, IOException {

        if (sourceNoiseMap == null) {
            throw new IllegalArgumentException(
                    "Cannot write TER file with no source");
        }

        int width = sourceNoiseMap.getWidth();
        int height = sourceNoiseMap.getHeight();

        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(
                fileName));

        short heightScale = (short) Math.
                floor(32768.0 / (double) metersPerPoint);

        os.write(getBytes("TERRAGENTERRAIN "));
        os.write(getBytes("SIZE"));
        short size = (short) (Globals.getMin(width, height).intValue());
        os.write(getBytes(size));
        byte[] nullBuff2 = {0, 0};
        os.write(nullBuff2);
        os.write(getBytes("XPTS"));
        os.write(getBytes((short) width));
        os.write(nullBuff2);
        os.write(getBytes("YPTS"));
        os.write(getBytes((short) height));
        os.write(nullBuff2);
        os.write(getBytes("SCAL"));
        os.write(getBytes((float) metersPerPoint));
        os.write(getBytes((float) metersPerPoint));
        os.write(getBytes((float) metersPerPoint));
        os.write(getBytes("ALTW"));
        os.write(getBytes(heightScale));
        os.write(nullBuff2);
        for (int y = 0; y < height; y++) {
            double[] pSource = sourceNoiseMap.getSlabPtr(y);
            for (int x = 0; x < width; x++) {
                short scaledHeight = (short) Math.floor(pSource[x] * 2.0);
                os.write(getBytes(scaledHeight));
            }
        }
        os.flush();
        os.close();
    }

    private byte[] getBytes(short sh) {
        return ByteBuffer.allocate(Short.BYTES).order(ByteOrder.LITTLE_ENDIAN).
                putShort(sh).array();
    }

    private byte[] getBytes(int sh) {
        return ByteBuffer.allocate(Integer.BYTES).order(ByteOrder.LITTLE_ENDIAN).
                putInt(sh).array();
    }

    private byte[] getBytes(float sh) {
        return ByteBuffer.allocate(Float.BYTES).order(ByteOrder.LITTLE_ENDIAN).
                putFloat(sh).array();
    }

    private byte[] getBytes(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(WriterTER.class.getName()).log(Level.SEVERE, null,
                    ex);
            throw new RuntimeException("UTF-8 does not exist WTF");
        }
    }

    /**
     * Calculates the width of one horizontal line in the file, in bytes.
     *
     * @param width The width of the noise map, in points.
     *
     * @returns The width of one horizontal line in the file.
     */
    int calcWidthByteCount(int width) {
        return width * Short.BYTES;
    }

}
