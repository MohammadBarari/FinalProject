package org.example.controller.user;

import org.example.domain.PassAndUser;
import org.example.dto.user.LoginDto;
import org.example.repository.passAndUser.PassAndUserRepository;
import org.example.service.sercurity.CustomUserDetailsService;
import org.example.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final CustomUserDetailsService userDetailsService;
    private final PassAndUserRepository passAndUserRepository;
    private final JwtUtil jwtUtil;

    public AuthController(CustomUserDetailsService userDetailsService, JwtUtil jwtUtil,PassAndUserRepository passAndUserRepository) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.passAndUserRepository = passAndUserRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        if (loginDto.username() == null || loginDto.password() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username and password are required");
        }

        PassAndUser user = passAndUserRepository.findByUsername(loginDto.username());
        if (user == null || !userDetailsService.validatePassword(loginDto.password(), user.getPass())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        String token = jwtUtil.generateToken(loginDto.username(), user.getId(), user.getTypeOfUser().name());
        return ResponseEntity.ok(token);
    }
}
