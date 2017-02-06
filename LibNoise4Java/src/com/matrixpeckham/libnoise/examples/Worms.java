/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.examples;

import com.matrixpeckham.libnoise.module.generator.Perlin;
import static com.matrixpeckham.libnoise.util.Globals.PI;
import static com.matrixpeckham.libnoise.util.Globals.valueNoise3D;
import static com.matrixpeckham.libnoise.util.NoiseQuality.STD;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author William Matrix Peckham
 */
public class Worms {
// worms.cpp
//
// This program uses the libnoise library to draw and animate worm-like
// creatures.  On a fast computer (P4/AMD Athlon-class), hundreds of worms
// can be drawn and animated at very high frame rates.
//
// The worms do not interact with each other because that would use way too
// much CPU resources.
//
// See the documentation for the Worm class for information on how
// coherent-noise values are used to draw and animate a worm.
//
// Requires OpenGL 1.1+ and GLUT (a windowing toolkit for OpenGL.)  The GLUT
// webpage is http://www.opengl.org/resources/libraries/glut.html
//
// Keyboard controls:
//
// Esc: Exits the program.
//
// Q: Increase the number of worms by one.
// A: Decrease the number of worms by one.
//
// W: Increase the number of worm segments by one.
// S: Increase the number of worm segments by one.
//
// E: Increase the worms' speed.
// D: Decrease the worms' speed.
//
// R: Increase the lateral (thrashing) speed of the worms.
// F: Decrease the lateral (thrashing) speed of the worms.
//
// T: Increase the worms' thickness.
// G: Decrease the worms' thickness.
//
// Y: Increase the worms' "twistiness".
// H: Decrease the worms' "twistiness".
//
// Copyright (C) 2004, 2005 Jason Bevins
//
// This program is free software; you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the Free
// Software Foundation; either version 2 of the License, or (at your option)
// any later version.
//
// This program is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
// (COPYING.txt) for more details.
//
// You should have received a copy of the GNU General Public License along
// with this program; if not, write to the Free Software Foundation, Inc., 59
// Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// The developer's email is jlbezigvins@gmzigail.com (for great email, take
// off every 'zig'.)
//

// Default worm lateral speed.
    static final double WORM_LATERAL_SPEED = (2.0 / 8192.0);
// Default length of a worm segment, in screen units.
    static final double WORM_SEGMENT_LENGTH = (1.0 / 64.0);
// Default segment count for each worm.
    static final int WORM_SEGMENT_COUNT = 112;
// Default worm speed.
    static final double WORM_SPEED = (3.0 / 2048.0);
// Default worm thickness.
    static final double WORM_THICKNESS = (4.0 / 256.0);
// Default "twistiness" of the worms.
    static final double WORM_TWISTINESS = (4.0 / 256.0);
// Default worm lateral speed.
    //static double wormLateralSpeed = WORM_LATERAL_SPEED;
    static double wormLateralSpeed = WORM_LATERAL_SPEED;
// Default segment count for each worm.
    static int wormSegmentCount = WORM_SEGMENT_COUNT;
// Default worm speed.
    static double wormSpeed = WORM_SPEED;
// Default worm thickness.
    static double wormThickness = WORM_THICKNESS;
// Default "twistiness" of the worms.
    static double wormTwistiness = WORM_TWISTINESS;
// Number of worms rendered on the screen.
    static int curWormCount = 32;

// This template function clamps the specified value to the specified bounds.
    public static <T extends Comparable<? super T>> T clamp(T value,
            T lowerBound,
            T upperBound) {
        if (value.compareTo( lowerBound ) < 0) {
            return lowerBound;
        } else if (value.compareTo( upperBound ) > 0) {
            return upperBound;
        }
        return value;
    }

// A standard 2-component vector.
    static class Vector2 {

        public double x, y;
    };

// A standard 3-component vector.
    static class Vector3 {

        public double x, y, z;
    };

// Worm class
//
// This class uses three-dimensional coherent noise to draw and animate a
// single worm-like creature.
//
// A worm is made up of a chain of "segments".  The first segment is known as
// the "head" segment.  This class draws the worm starting from the head
// segment.
//
// A worm object contains a three-octave Perlin-noise module, which is used to
// specify the angles of each worm segment, in radians.  Three octaves is a
// good compromise between realism and speed.
//
// The coherent-noise values that specify the segment angles are generated by
// the input values that occur along a horizontal line in "noise space".  By
// shifting the (x, y, z) coordinates of the input value by a small constant
// amount each frame, the worm will slightly move.
//
// To draw the worm, call the Draw() method.
//
// To move the worm, call the Update() method.  That method moves the position
// of the head segment a small distance in the direction opposite its angle.
// Also, the x coordinate of the input value is shifted in a negative
// direction, which propagates the previous coherent-noise values over to
// subsequent segments.  This will produce a "slithering" effect.
//
// Each worm should have its own seed value because worms with the same seed
// value will act identically.
    static class Worm {

        // Coordinates of the input value that generates the Perlin noise in
        // "noise space".  This is used to specify the angles of the worm's
        // segments.
        Vector3 headNoisePos = new Vector3();
        // Position of the worm's head segment, in screen space.
        Vector2 headScreenPos = new Vector2();
        // Worm's lateral speed.
        double lateralSpeed;
        // Noise module used to draw the worm.
        Perlin noise = new Perlin();
        // Number of segments that make up the worm.
        int segmentCount;
        // Length of a worm segment.
        double segmentLength;
        // Worm speed.
        double speed;
        // Worm thickness.
        double thickness;
        // "Twistiness" of the worm.
        double twistiness;

        public Worm() {
            // The coordinates of the input value for the head segment must not
            // start at an integer boundary (for example, (0, 0, 0)).  At integer
            // boundaries, the coherent-noise values are always zero (blame gradient
            // noise for that), which would cause the worm to unrealistically
            // straighten those boundaries.
            headNoisePos.x = 7.0 / 2048.0;
            headNoisePos.y = 1163.0 / 2048.0;
            headNoisePos.z = 409.0 / 2048.0;

            // set up us the Perlin-noise module.
            noise.setSeed( 0 );
            noise.setFrequency( 1.0 );
            noise.setLacunarity( 2.375 );
            noise.setOctaveCount( 3 );
            noise.setPersistence( 0.5 );
            noise.setNoiseQuality( STD );

            // set the worm parameters with their default values.
            headScreenPos.x = 0.0;
            headScreenPos.y = 0.0;
            lateralSpeed = WORM_LATERAL_SPEED;
            segmentCount = WORM_SEGMENT_COUNT;
            segmentLength = WORM_SEGMENT_LENGTH;
            speed = WORM_SPEED;
            thickness = WORM_THICKNESS;
            twistiness = WORM_TWISTINESS;
        }

        // Updates the worm's segment positions.  This must be called after each
        // frame.
        void update() {
            // The angle of the head segment is used to determine the direction the worm
            // moves.  The worm moves in the opposite direction.
            double noiseValue = noise.getValue(
                    headNoisePos.x,
                    headNoisePos.y,
                    headNoisePos.z );
            headScreenPos.x -= (Math.cos( noiseValue * 2.0 * PI ) * speed);
            headScreenPos.y -= (Math.sin( noiseValue * 2.0 * PI ) * speed);

            // Slightly update the coordinates of the input value, in "noise space".
            // This causes the worm's shape to be slightly different in the next frame.
            // The x coordinate of the input value is shifted in a negative direction,
            // which propagates the previous Perlin-noise values over to subsequent
            // segments.  This produces a "slithering" effect.
            headNoisePos.x -= speed * 2.0;
            headNoisePos.y += lateralSpeed;
            headNoisePos.z += lateralSpeed;

            // Make sure the worm's head is within the window, otherwise the worm may
            // escape.  Horrible, horrible freedom!
            headScreenPos.x = clamp( headScreenPos.x, -1.0, 1.0 );
            headScreenPos.y = clamp( headScreenPos.y, -1.0, 1.0 );
        }

        void draw(Graphics2D g) {
            // The worm is drawn using a triangle strip.

            // Position of the current segment being drawn, in screen space.
            Vector2 curSegmentScreenPos = new Vector2();
            curSegmentScreenPos.x = headScreenPos.x;
            curSegmentScreenPos.y = headScreenPos.y;

            // The width of the worm's body at the current segment being drawn.
            Vector2 offsetPos = new Vector2();

            // Coordinates of the input value, in "noise space", that specifies the
            // current segment's angle.
            Vector3 curNoisePos = new Vector3();

            // The vector that is perpindicular to the center of the segment; used to
            // determine the position of the edges of the worm's body.
            Vector2 curNormalPos = new Vector2();

            List<Vector2> verts = new ArrayList<>( segmentCount * 8 );

            for (int curSegment = 0; curSegment < segmentCount; curSegment++) {

                // get the Perlin-noise value for this segment based on the segment
                // number.  This value is interpreted as an angle, in radians.
                curNoisePos.x = headNoisePos.x + (curSegment * twistiness);
                curNoisePos.y = headNoisePos.y;
                curNoisePos.z = headNoisePos.z;
                double noiseValue = noise.getValue(
                        curNoisePos.x,
                        curNoisePos.y,
                        curNoisePos.z );

                // Determine the width of the worm's body at this segment.
                double taperAmount = getTaperAmount( curSegment ) * thickness;

                // Determine the offset of this segment from the previous segment by
                // converting the angle from the Perlin-noise module to an (x, y)
                // coordinate.
                offsetPos.x = Math.cos( noiseValue * 2.0 * PI );
                offsetPos.y = Math.sin( noiseValue * 2.0 * PI );

                // Determine the coordinates of each corner of the segment.
                curNormalPos.x = (-offsetPos.y) * taperAmount;
                curNormalPos.y = (offsetPos.x) * taperAmount;
                offsetPos.x *= segmentLength;
                offsetPos.y *= segmentLength;

                double x0 = (curSegmentScreenPos.x + curNormalPos.x);
                double y0 = (curSegmentScreenPos.y + curNormalPos.y);
                double x1 = (curSegmentScreenPos.x - curNormalPos.x);
                double y1 = (curSegmentScreenPos.y - curNormalPos.y);
                Vector2 vert = new Vector2();
                vert.x = x0;
                vert.y = y0;
                verts.add( vert );
                Vector2 vert1 = new Vector2();
                vert1.x = x1;
                vert1.y = y1;
                verts.add( vert1 );
                //g.fill(new Rectangle2D.Double(x1, y1, x0 - x1, y0 - y1));
                // Draw the segment using OpenGL.
                // Prepare the next segment.
                ++curSegment;
                curSegmentScreenPos.x += offsetPos.x;
                curSegmentScreenPos.y += offsetPos.y;
            }
            for (int index = 0; index < verts.size() - 4; index += 2) {
                double x0 = verts.get( index ).x;
                double y0 = verts.get( index ).y;
                double x1 = verts.get( index + 1 ).x;
                double y1 = verts.get( index + 1 ).y;
                double x2 = verts.get( index + 2 ).x;
                double y2 = verts.get( index + 2 ).y;
                double x3 = verts.get( index + 3 ).x;
                double y3 = verts.get( index + 3 ).y;
                GeneralPath.Double path = new Path2D.Double();
                path.moveTo( x0, y0 );
                path.lineTo( x1, y1 );
                path.lineTo( x3, y3 );
                path.lineTo( x2, y2 );
                path.closePath();
                g.fill( path );
            }
        }

        // Returns the taper amount for a specified segment.  A taper value of 0.0
        // indicates full tapering, while a taper value of 1.0 indicates no
        // tapering.  Taper values are at a minimum at both ends of the worm and
        // are at a maximum at the middle of the worm.
        double getTaperAmount(int segment) {
            double curSegment = (double) segment;
            double segmentCount = (double) this.segmentCount;
            double halfSegmentCount = segmentCount / 2.0;
            double baseTaperAmount = 1.0 - Math.abs( (curSegment
                    / halfSegmentCount) - 1.0 );
            return Math.sqrt( baseTaperAmount ); // sqrt better defines the tapering.
        }

        // sets the position of the worm's head segment in screen space.
        void setHeadScreenPos(
                Vector2 pos) {
            headScreenPos.x = pos.x;
            headScreenPos.y = pos.y;
        }

        // sets the worm's lateral speed.  This is the amount the worm "thrashes
        // around" between frames.  Higher values increases the thrashing amount.
        void setLateralSpeed(double lateralSpeed) {
            this.lateralSpeed = lateralSpeed;
        }

        // sets the seed of the Perlin-noise module.
        void setSeed(int seed) {
            noise.setSeed( seed );
        }

        // sets the number of segments that make up the worm.
        void setSegmentCount(int segmentCount) {
            this.segmentCount = segmentCount;
        }

        // sets the length of a worm segment, in screen units.
        void setSegmentLength(double segmentLength) {
            this.segmentLength = segmentLength;
        }

        // sets the worm's speed.  Higher values increase the speed.
        void setSpeed(double speed) {
            this.speed = speed;
        }

        // sets the worm's thickness, in screen units.
        void setThickness(double thickness) {
            this.thickness = thickness;
        }

        // Defines the "twistiness" of the worms.  Higher values produce more
        // contorted worms.
        void setTwistiness(double twistiness) {
            this.twistiness = twistiness;
        }
    };
// Array used to store a bunch of worms.
    static final int MAX_WORM_COUNT = 1024;
    static Worm[] wormArray = new Worm[MAX_WORM_COUNT];
// This function is called by GLUT when a key is pressed.
    static KeyListener listener = new KeyAdapter() {
        @Override
        @SuppressWarnings("")
        public void keyTyped(KeyEvent e) {
            switch (e.getKeyChar()) {
                case 'Q':
                case 'q':
                    ++curWormCount;
                    break;
                case 'A':
                case 'a':
                    --curWormCount;
                    break;
                case 'W':
                case 'w':
                    ++wormSegmentCount;
                    break;
                case 'S':
                case 's':
                    --wormSegmentCount;
                    break;
                case 'E':
                case 'e':
                    wormSpeed += (1.0 / 2048.0);
                    break;
                case 'D':
                case 'd':
                    wormSpeed -= (1.0 / 2048.0);
                    break;
                case 'R':
                case 'r':
                    wormLateralSpeed += (1.0 / 8192.0);
                    break;
                case 'F':
                case 'f':
                    wormLateralSpeed -= (1.0 / 8192.0);
                    break;
                case 'T':
                case 't':
                    wormThickness += (1.0 / 256.0);
                    break;
                case 'G':
                case 'g':
                    wormThickness -= (1.0 / 256.0);
                    break;
                case 'Y':
                case 'y':
                    wormTwistiness += (1.0 / 256.0);
                    break;
                case 'H':
                case 'h':
                    wormTwistiness -= (1.0 / 256.0);
                    break;
                case 27:
                    System.exit( 0 );
            }

            // Make sure the worm parameters are legal.  If they are not, set them to
            // the nearest legal value.
            curWormCount = clamp( curWormCount, 1, 1024 );
            wormSegmentCount = clamp( wormSegmentCount, 1, 256 );
            wormSpeed = clamp( wormSpeed, (1.0 / 2048.0), (1024.0 / 2048.0) );
            wormLateralSpeed = clamp( wormLateralSpeed, (1.0 / 8192.0), (64.0
                    / 8192.0) );
            wormThickness =
                    clamp( wormThickness, (1.0 / 256.0), (16.0 / 256.0) );
            wormTwistiness = clamp( wormTwistiness, (1.0 / 256.0),
                    (16.0 / 256.0) );

            // set the parameters for each worm.
            for (int i = 0; i < curWormCount; i++) {
                wormArray[i].setSegmentCount( wormSegmentCount );
                wormArray[i].setSpeed( wormSpeed );
                wormArray[i].setLateralSpeed( wormLateralSpeed );
                wormArray[i].setThickness( wormThickness );
                wormArray[i].setTwistiness( wormTwistiness );
            }
        }
    };

// This function is called by GLUT when the window needs to be redrawn.
    static void repaint(Graphics g, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor( clearColor );
        g2.fillRect( 0, 0, width, height );
        AffineTransform b4 = g2.getTransform();
        AffineTransform transform = new AffineTransform();
        transform.translate( width / 2.0, height / 2.0 );
        transform.scale( width / 2.0, height / 2.0 );

        g2.setTransform( transform );

        //g2.setPaint(texturePaint);
        //g.setColor(new Color(255, 0, 0));
        // Draw all the worms.
        for (int i = 0; i < curWormCount; i++) {
            g2.setColor(
                    new Color( (i & 1) != 0 ? 255 : 0, (i & 2) != 0 ? 255 : 0,
                    (i & 4) != 0 ? 255 : 0 ) );
            wormArray[i].draw( g2 );
            wormArray[i].update();
        }
        g2.setTransform( b4 );
    }
    static Color clearColor;

    public static void main(String[] argv) {
        for (int i = 0; i < MAX_WORM_COUNT; i++) {
            wormArray[i] = new Worm();
            Vector2 pos = new Vector2();
            pos.x = valueNoise3D( i + 1000, i + 2000, i + 3000 );
            pos.y = valueNoise3D( i + 1001, i + 2001, i + 3001 );
            wormArray[i].setSeed( i );
            wormArray[i].setHeadScreenPos( pos );
            wormArray[i].update();
        }

        // set up us the GLUT function callbacks.
        // set the OpenGL texture mapping and blending parameters for this program.
        clearColor = new Color( 0.1f, 0.15f, 0.3f, 1.0f );

        // Upload the texture to the video card and build mipmaps.
        JFrame frame = new JFrame( "Worm demo" );
        final JPanel pane = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            public void paint(Graphics g) {
                super.paint( g ); //To change body of generated methods, choose Tools | Templates.
                Worms.repaint( g, getWidth(), getHeight() );
            }
        };

        pane.setPreferredSize( new Dimension( 500, 500 ) );
        pane.setSize( 500, 500 );
        pane.setFocusable( true );
        pane.requestFocus();
        pane.requestFocusInWindow();
        pane.addKeyListener( listener );

        frame.add( pane );
        frame.pack();
        frame.setDefaultCloseOperation( frame.EXIT_ON_CLOSE );
        frame.setVisible( true );
        Timer t = new Timer( 30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pane.repaint();
            }
        } );
        t.start();

    }
}
