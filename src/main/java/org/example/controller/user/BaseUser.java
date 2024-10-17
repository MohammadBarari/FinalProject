package org.example.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.service.user.BaseUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/baseUser")
@RequiredArgsConstructor
public class BaseUser {
    private final BaseUserService baseUserService;
}
