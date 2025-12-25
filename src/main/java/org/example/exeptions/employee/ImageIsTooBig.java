package org.example.exeptions.employee;

public class ImageIsTooBig extends RuntimeException {
    public ImageIsTooBig() {
        super("Image is too big");
    }
}
