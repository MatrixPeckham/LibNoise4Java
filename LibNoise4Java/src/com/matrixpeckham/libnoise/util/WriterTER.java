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

package com.matrixpeckham.libnoise.util;

import static com.matrixpeckham.libnoise.util.Globals.DEFAULT_METERS_PER_POINT;
import static com.matrixpeckham.libnoise.util.Globals.getMin;
import com.matrixpeckham.libnoise.util.exceptions.ExceptionUnknown;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import static java.lang.Math.floor;
import static java.nio.ByteBuffer.allocate;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Terragen Terrain writer class.
 *
 * This class creates a file in Terrage Terrain (*.ter) format given the
 * contents of a noise map object. This class treats the values in the noise map
 * as elevations measured in meters.
 *
 * <a href=http://www.planetside.co.uk/terragen/>Terragen</a> is a terrain
 * application that renders realistic landscapes. Terragen is available for
 * Windows and MacOS; unfortunately, Terragen does not have UN*X versions.
 *
 * <b>Writing the noise map</b>
 *
 * To write the noise map, perform the following steps: - Pass the filename to
 * the SetDestFilename() method. - Pass a NoiseMap object to the
 * SetSourceNoiseMap() method. - Call the WriteDestFile() method.
 *
 * The SetDestFilename() and SetSourceNoiseMap() methods must be called before
 * calling the WriteDestFile() method.
 */
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
     * @return The width of one horizontal line in the file.
     */
    int calcWidthByteCount(int width) {

	return width * Integer.SIZE / 8;
    }
    //////////////////////////////////////////////
    //These four methods use New I/O ByteBuffers
    //temporarily to get a byte array of the right
    //endian-ness. There's one short, int, float
    //and one for string.
    //////////////////////////////////////////////

    private byte[] getBytes(short sh) {
	return allocate(Short.SIZE / 8).order(LITTLE_ENDIAN).
		putShort(sh).array();
    }

    private byte[] getBytes(int sh) {
	return allocate(Integer.SIZE / 8).order(LITTLE_ENDIAN).
		putInt(sh).array();
    }

    private byte[] getBytes(float sh) {
	return allocate(Float.SIZE / 8).order(LITTLE_ENDIAN).
		putFloat(sh).array();
    }

    private byte[] getBytes(String str) {
	try {
	    return str.getBytes("UTF-8");
	} catch (UnsupportedEncodingException ex) {
	    //This REALLY should NEVER happen, Java ALWAYS has UTF-8.
	    LOG.log(Level.SEVERE, null,
		    ex);
	    throw new RuntimeException(
		    "UTF-8 does not exist WTF");
	}
    }

    /**
     * Writes the contents of the noise map object to the file.
     *
     * @throws java.io.FileNotFoundException
     * @noise.pre SetDestFilename() has been previously called.
     * @noise.pre SetSourceNoiseMap() has been previously called.
     *
     * @throws IllegalArgumentException See the preconditions.
     * @throws ExceptionUnknown An unknown exception occurred. Possibly the file
     * could not be written.
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
	//straight forward implementation of writing file.
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

    /**
     * Simple getter for the file name
     *
     * @return
     */
    public String getFileName() {
	return fileName;
    }

    /**
     * Sets the file name that we will write to, synonym for setFilename
     *
     * @param fileName
     */
    public void setDestFilename(String fileName) {
	setFileName(fileName);
    }

    /**
     * sets the file name that we will write to
     *
     * @param fileName
     */
    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    /**
     * simple getter for meters per point
     *
     * @return
     */
    public double getMetersPerPoint() {
	return metersPerPoint;
    }

    /**
     * setter for meters per point.
     *
     * @param metersPerPoint
     */
    public void setMetersPerPoint(double metersPerPoint) {
	this.metersPerPoint = metersPerPoint;
    }

    /**
     * gets the source noise map that we will write the terrain for.
     *
     * @return
     */
    public NoiseMap getSourceNoiseMap() {
	return sourceNoiseMap;
    }

    /**
     * sets the source noise map that we will write the terrain for.
     *
     * @param sourceNoiseMap
     */
    public void setSourceNoiseMap(NoiseMap sourceNoiseMap) {
	this.sourceNoiseMap = sourceNoiseMap;
    }

    //logger to prevent "no logger" warnings
    private static final Logger LOG
	    = Logger.getLogger(WriterTER.class.getName());

}
