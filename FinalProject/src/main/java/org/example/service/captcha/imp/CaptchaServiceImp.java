package org.example.service.captcha.imp;

import org.example.service.captcha.CaptchaService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CaptchaServiceImp implements CaptchaService {
    @Override
    public String generateCaptcha() {
        return String.valueOf(new Random().nextInt(8999) + 1000);
    }
}
