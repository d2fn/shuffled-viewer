package org.df;

import processing.core.*;

import java.awt.*;

/**
 * PImageUtils
 * @author Dietrich Featherston
 */
public class PImageUtils {

    // return a mirror of the given image
    public static PImage mirror(PImage image) {
        PImage mirror = new PImage(image.width, image.height);
        image.loadPixels();
        mirror.loadPixels();
        for (int y = 0; y < image.height; y++) {
            //int[] line = new int[image.width];
            //System.arraycopy(mirror.pixels,y*image.width,line,0,image.width);
            for (int x = 0; x < image.width; x++) {
                int c = image.pixels[y * image.width + (image.width - x - 1)];
                mirror.pixels[y * mirror.width + x] = c;
            }
        }
        return mirror;
    }

    public static void tile(PApplet applet, PImage img, int startx, int starty, int endx, int endy) {
        for(int x = startx; x < endx; x += img.width) {
            for(int y = starty; y < endy; y += img.height) {
                applet.image(img,x,y);            
            }
        }
    }

    public static PImage noise(int max, int width, int height) {
        PImage noise = new PImage(width,height);
        noise.loadPixels();
        for(int i = 0; i < noise.pixels.length; i++) {
            int whitevalue = (int)Math.round(Math.random()*max);
            noise.pixels[i] = (0xFF<<24) + (whitevalue << 16) + (whitevalue << 8) + (whitevalue);
        }
        noise.updatePixels();
        return noise;
    }

    public static void round(PApplet applet, PImage image, float radius) {

        if(image.width <= 2 || image.height <= 2) {
            return;
        }

        float diameter = 2.0f*radius;

        PGraphics g = applet.createGraphics(image.width,image.height,PApplet.P3D);
        g.beginDraw();
        g.smooth();
        g.background(0x00000000);
        g.fill(0x000000ff);
        g.noStroke();
        g.rect(radius,0,image.width-diameter,image.height);
        g.rect(0,radius,image.width,image.height-2*radius);
        g.arc(radius,radius,diameter,diameter,PApplet.PI, PApplet.TWO_PI-PApplet.PI/2);
        g.arc(image.width-radius,radius,diameter,diameter,PApplet.TWO_PI-PApplet.PI/2, PApplet.TWO_PI);
        g.arc(image.width-radius,image.height-radius,diameter,diameter,0,PApplet.PI/2);
        g.arc(radius,image.height-radius,diameter,diameter,PApplet.PI/2, PApplet.PI);
        g.endDraw();

        image.mask(g);
        image.updatePixels();
    }
}
