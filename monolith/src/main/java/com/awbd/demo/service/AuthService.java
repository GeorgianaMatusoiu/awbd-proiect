package com.awbd.demo.service;

import com.awbd.demo.dto.RegisterRequest;
import com.awbd.demo.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final JdbcUserDetailsManager jdbcUserDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public AuthService(JdbcUserDetailsManager jdbcUserDetailsManager,
                       PasswordEncoder passwordEncoder) {
        this.jdbcUserDetailsManager = jdbcUserDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterRequest request) {
        log.info("Se incearca inregistrarea unui utilizator nou cu username={}", request.getUsername());

        if (jdbcUserDetailsManager.userExists(request.getUsername())) {
            log.error("Inregistrarea a esuat: username-ul {} exista deja", request.getUsername());
            throw new BadRequestException("Username-ul exista deja.");
        }

        var user = User.withUsername(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles("USER")
                .build();

        jdbcUserDetailsManager.createUser(user);

        log.info("Utilizatorul {} a fost inregistrat cu succes", request.getUsername());
    }
}