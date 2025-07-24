package com.boot.quizapp.controller;

import com.boot.quizapp.model.User;
import com.boot.quizapp.dao.UserRepository;
import com.boot.quizapp.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserRepository userRepo;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @PostMapping("register")
    public String register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_ADMIN");
        userRepo.save(user);
        return "User registered!";
    }

    @PostMapping("login")
    public String login(@RequestBody User user) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        return jwtUtil.generateToken(user.getUsername());
    }
}
