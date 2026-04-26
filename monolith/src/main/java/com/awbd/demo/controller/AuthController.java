package com.awbd.demo.controller;

import com.awbd.demo.dto.RegisterRequest;
import com.awbd.demo.exception.BadRequestException;
import com.awbd.demo.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;
    private final AuthenticationManager authenticationManager;
    private final TokenBasedRememberMeServices rememberMeServices;

    public AuthController(AuthService service,
                          AuthenticationManager authenticationManager,
                          TokenBasedRememberMeServices rememberMeServices) {
        this.service = service;
        this.authenticationManager = authenticationManager;
        this.rememberMeServices = rememberMeServices;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody @Valid RegisterRequest request) {
        service.register(request);
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, Object> request,
                                     HttpServletRequest httpRequest,
                                     HttpServletResponse httpResponse) {
        try {
            String username = (String) request.get("username");
            String password = (String) request.get("password");
            boolean rememberMe = Boolean.TRUE.equals(request.get("rememberMe"));

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            HttpSession session = httpRequest.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            if (rememberMe) {
                rememberMeServices.loginSuccess(httpRequest, httpResponse, authentication);
            }

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("message", "Autentificare reusita");
            response.put("username", authentication.getName());
            response.put("authorities", authentication.getAuthorities());

            return response;
        } catch (AuthenticationException ex) {
            throw new BadRequestException("Username sau parola incorecta.");
        }
    }

    @PostMapping("/logout")
    public Map<String, String> logout(HttpServletRequest request,
                                      HttpServletResponse response,
                                      Authentication authentication) {
        rememberMeServices.logout(request, response, authentication);

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        SecurityContextHolder.clearContext();

        Map<String, String> result = new LinkedHashMap<>();
        result.put("message", "Logout reusit");
        return result;
    }

    @GetMapping("/me")
    public Map<String, Object> me(Authentication authentication) {
        if (authentication == null) {
            throw new BadRequestException("Utilizator neautentificat.");
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("username", authentication.getName());
        response.put("authorities", authentication.getAuthorities());
        return response;
    }
}