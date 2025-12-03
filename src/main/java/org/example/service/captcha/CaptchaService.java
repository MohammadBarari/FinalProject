package org.example.service.captcha;

import org.example.dto.captcha.CaptchaDto;

public interface CaptchaService {
    CaptchaDto generateCaptcha();

    void validateCaptcha(String id , String userAnswer);

}
