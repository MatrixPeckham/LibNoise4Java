/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.util;

import static com.matrixpeckham.libnoise.util.Globals.DEFAULT_METERS_PER_POINT;
import static com.matrixpeckham.libnoise.util.Globals.getMin;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import static java.lang.Math.floor;
import static java.nio.ByteBuffer.allocate;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import java.util.logging.Level;
import static java.util.logging.Logger.getLogger;

public class WriterTER {

    /**
     * file name to write to
     */
    String fileName;

    /**
     * distance separating adjacent points in the noise meters.
     */
    double metersPerPoint;

    /**
     * pointer to the map that will be written to the file
     */
    NoiseMap sourceNoiseMap;

    /**
     * constructor
     */
    public WriterTER() {
        sourceNoiseMap = null;
        metersPerPoint = DEFAULT_METERS_PER_POINT;
    }

    /**
     * Calculates the width of one horizontal line in the file, in bytes.
     *
     * @param width The width of the noise map, in points.
     *
     * @returns The width of one horizontal line in the file.
     */
    int calcWidthByteCount(int width) {

        return width * Integer.BYTES;
    }

    private byte[] getBytes(short sh) {
        return allocate(Short.BYTES).order(LITTLE_ENDIAN).
                putShort(sh).array();
    }

    private byte[] getBytes(int sh) {
        return allocate(Integer.BYTES).order(LITTLE_ENDIAN).
                putInt(sh).array();
    }

    private byte[] getBytes(float sh) {
        return allocate(Float.BYTES).order(LITTLE_ENDIAN).
                putFloat(sh).array();
    }

    private byte[] getBytes(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            getLogger(
                    getClass().getName()).log(Level.SEVERE, null,
                            ex);
            throw new RuntimeException(
                    "UTF-8 does not exist WTF");
        }
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
        try (BufferedOutputStream os
                = new BufferedOutputStream(new FileOutputStream(
                                fileName))) {
                            short heightScale = (short) floor(32768.0
                                    / metersPerPoint);
                            os.write(getBytes("TERRAGENTERRAIN "));
                            os.write(getBytes("SIZE"));
                            short size = getMin(width, height).shortValue();
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
                            for (int y = 0; y < height;
                                    y++) {
                                for (int x = 0; x < width;
                                        x++) {
                                    short scaledHeight = (short) floor(
                                            sourceNoiseMap.getValue(x, y) * 2.0);
                                    os.write(getBytes(scaledHeight));
                                }
                            }
                            os.flush();
                        }
    }

}
