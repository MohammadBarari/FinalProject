package org.example.service.sercurity;

import org.example.domain.PassAndUser;
import org.example.enumirations.TypeOfUser;
import org.example.repository.passAndUser.PassAndUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private PassAndUserRepository passAndUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PassAndUser user = passAndUserRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        System.out.println(passwordEncoder.encode(user.getPass()));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                passwordEncoder.encode(user.getPass()),
                getAuthorities(user.getTypeOfUser())
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(TypeOfUser type) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + type.name()));
    }


    public String registerUser(PassAndUser user) {
        user.setPass(passwordEncoder.encode(user.getPass())); // Hash password
        passAndUserRepository.save(user);
        return "User registered successfully";
    }

    private String encodingPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword); // Check if raw password matches the encoded one
    }
}
