package org.example.controller.user;

import org.example.domain.PassAndUser;
import org.example.repository.passAndUser.PassAndUserRepository;
import org.example.service.sercurity.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private PassAndUserRepository passAndUserRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        PassAndUser user = passAndUserRepository.findByUsername(username);
        if (user == null || !customUserDetailsService.validatePassword(password, user.getPass())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
        return ResponseEntity.ok("Login successful");
    }
}
