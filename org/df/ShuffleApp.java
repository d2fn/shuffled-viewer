package org.df;

import processing.core.PApplet;
import processing.core.PImage;
import hypermedia.video.OpenCV;

import java.awt.*;

/**
 * ShuffleApp
 *
 * @author Dietrich Featherston
 */
public class ShuffleApp extends PApplet {

    int frameWidth = 640;
    int frameHeight = 480;

    OpenCV opencv;
    int brightness,contrast;

    PImageStack faceStack;

    PImage boxMask150;
    PImage boxMask300;

    boolean debug = false;
    boolean splitFace = false;

    public void setup() {

        size(frameWidth, frameHeight);

        opencv = new OpenCV(this);
        opencv.capture(width, height);                   // open video stream
        opencv.cascade(OpenCV.CASCADE_FRONTALFACE_ALT);  // load detection description, here-> front face detection : "haarcascade_frontalface_alt.xml"

        faceStack = new PImageStack(20, 300, 300);

        boxMask150 = loadImage("boxmask-150x150.png");
        boxMask300 = loadImage("boxmask-300x300.png");
    }

    public void draw() {

        background(0xff000000);

        opencv.read();
        //opencv.convert(GRAY);
        opencv.contrast(contrast);
        opencv.brightness(brightness);

        // proceed detection
        Rectangle[] faces = opencv.detect(1.2f, 2, OpenCV.HAAR_DO_CANNY_PRUNING, 40, 40);

        // display the image
        PImage image = PImageUtils.mirror(opencv.image());

        PImage noise = PImageUtils.noise(80, 250, 250);
        PImageUtils.tile(this, noise, 0, 0, width, height);

        // draw face area(s)
        noFill();
        stroke(255, 0, 0);

        for (int i = 0; i < faces.length; i++) {

            PImage face = image.get(image.width - faces[i].x - faces[i].width, faces[i].y, faces[i].width, faces[i].height);
            face.filter(GRAY);
            face.resize(faceStack.imgwidth, faceStack.imgheight);

            faceStack.push(face);


            if (splitFace) {
                PImage faceq1 = (faceStack.isFull() ? faceStack.getRandom() : face).get(0, 0, face.width / 2, face.height / 2);
                PImage faceq2 = (faceStack.isFull() ? faceStack.getRandom() : face).get(face.width / 2, 0, face.width / 2, face.height / 2);
                PImage faceq3 = (faceStack.isFull() ? faceStack.getRandom() : face).get(0, face.height / 2, face.width / 2, face.height / 2);
                PImage faceq4 = (faceStack.isFull() ? faceStack.getRandom() : face).get(face.width / 2, face.height / 2, face.width / 2, face.height / 2);

                faceq1.mask(boxMask150);
                faceq2.mask(boxMask150);
                faceq3.mask(boxMask150);
                faceq4.mask(boxMask150);

                image(faceq1, width / 2 - faceq1.width, height / 2 - faceq1.height);
                image(faceq2, width / 2, height / 2 - faceq1.height);
                image(faceq3, width / 2 - faceq1.width, height / 2);
                image(faceq4, width / 2, height / 2);
            } else {
                PImage fullFace = faceStack.isFull() ? faceStack.getRandom() : face;
                fullFace.mask(boxMask300);
                image(fullFace, width / 2 - face.width / 2, height / 2 - face.height / 2);
            }

//            beginShape();
//            vertex(10,20);
//            bezierVertex(10,15,15,10,20,10);
//            vertex(10,10);
//            vertex(10,20);
//            endShape();

            //image(faceStack.isFull() ? faceStack.getRandom() : face, width / 2 - face.width / 2, height / 2 - face.height / 2);
        }

        if (debug) {
            image(image, width - 160, height - 120, 160, 120);
        }
    }

    public void keyPressed() {
        if (key == 'd') {
            debug = !debug;
        }
        if (key == 's') {
            splitFace = !splitFace;
        }
    }

    /**
     * Changes contrast/brigthness values
     */
    public void mouseDragged() {
        contrast = (int) map(mouseX, 0, width, -128, 128);
        brightness = (int) map(mouseY, 0, width, -128, 128);
    }

    public void stop() {
        opencv.stop();
        super.stop();
    }
}
