package org.df;

import processing.core.PImage;

/**
 * PImageStack
 *
 * Captures a fixed number of PImages in a stack, disposing of the oldest
 * when a new image putting it over its size limit
 *
 * All image must be of the dimensions specified in the constructor
 *
 * @author Dietrich Featherston
 */
public class PImageStack {

    // the stack of images
    PImage[] stack;

    // max number of images to keep
    int maxSize;

    // pointer to the next position in the stack to overwrite
    // new PImages are never reallocated for the same index, the pixels
    // are simply overwritten with the new values provided
    int stackPtr;

    // image dimensions
    int imgwidth, imgheight;

    boolean full;

    PImageStack(int maxSize, int imgwidth, int imgheight) {

        this.maxSize = maxSize;
        this.imgwidth = imgwidth;
        this.imgheight = imgheight;

        stack = new PImage[this.maxSize];
    }

    public void push(PImage img) {

        img.loadPixels();
        if(stack[stackPtr] == null) {
            stack[stackPtr] = img;
        }
        else {
            stack[stackPtr].pixels = img.pixels;
            stack[stackPtr].updatePixels();
        }

        stackPtr = (stackPtr + 1) % maxSize;

        if(stackPtr == 0) {
            full = true;
        }
    }

    public PImage getNewest() {
        int ptr = stackPtr - 1;
        if(ptr == -1) {
            ptr = maxSize-1;
        }
        return stack[ptr];
    }

    public PImage getOldest() {
        return stack[stackPtr];
    }

    public PImage getRandom() {
        return stack[(int)Math.round((Math.random()*(maxSize-1)))];
    }

    public boolean isFull() {
        return full;
    }
}
