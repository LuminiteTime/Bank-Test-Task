package com.example.bank.controller;

import com.example.bank.dto.AuthResponse;
import com.example.bank.dto.BankUserDTO;
import com.example.bank.dto.LoginRequest;
import com.example.bank.dto.RegisterUserRequest;
import com.example.bank.model.UserEntity;
import com.example.bank.repository.UserRepository;
import com.example.bank.security.JwtGenerator;
import com.example.bank.service.BankUserService;
import com.example.bank.utils.MappingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final BankUserService bankUserService;

    private final JwtGenerator tokenGenerator;

    @PostMapping("/register")
    public ResponseEntity<BankUserDTO> registerUser(@RequestBody RegisterUserRequest request) {

        if (Boolean.TRUE.equals(userRepository.existsByUsername(request.getUsername()))) {
            throw new IllegalArgumentException("User with login " + request.getUsername() + " already exists.");
        }

        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
        log.info("User registered successfully.");

        return ResponseEntity.ok(MappingUtils.mapToBankUserDTO(bankUserService.registerUser(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("User logged in successfully.");

        String token = tokenGenerator.generateToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
