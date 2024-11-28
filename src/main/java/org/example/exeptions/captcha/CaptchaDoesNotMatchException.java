package org.example.exeptions.captcha;

public class CaptchaDoesNotMatchException extends RuntimeException{
    public CaptchaDoesNotMatchException(){
        super("Captcha doesn't match");
    }
}
