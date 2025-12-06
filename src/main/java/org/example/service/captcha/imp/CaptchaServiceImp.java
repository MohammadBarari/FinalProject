package org.example.service.captcha.imp;

import org.example.dto.captcha.CaptchaDto;
import org.example.exeptions.captcha.CaptchaDoesNotMatchException;
import org.example.service.captcha.CaptchaService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CaptchaServiceImp implements CaptchaService {
    private final Map<String,String> captchaMapHolder = new ConcurrentHashMap<>();
    @Override
    public CaptchaDto generateCaptcha() {
        String captchaId = UUID.randomUUID().toString();
        String captcha = String.valueOf(new Random().nextInt(8999) + 1000);
        captchaMapHolder.put(captchaId,captcha);
        return new CaptchaDto(captchaId,captcha);
    }

    @Override
    public void validateCaptcha(String id , String userAnswer) {
        String correct = captchaMapHolder.get(id);
        if (!(correct != null && Objects.equals(correct,userAnswer)))
        {
            throw new CaptchaDoesNotMatchException();
        }
    }


}
