package org.example.exeptions;

public class ImageSizeIsOver extends Exception {
    public ImageSizeIsOver() {
        super("Image size is higher than standard");
    }
}
