package org.example.exeptions;

public class CaptchaDoesNotMatchException extends RuntimeException{
    public CaptchaDoesNotMatchException(){
        super("Captcha doesn't match");
    }
}
