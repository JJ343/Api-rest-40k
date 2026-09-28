package com.warhammer.primarchs.auth;

import com.warhammer.primarchs.auth.dto.AuthResponse;
import com.warhammer.primarchs.auth.dto.LoginRequest;
import com.warhammer.primarchs.auth.dto.RegisterRequest;
import com.warhammer.primarchs.common.exception.ConflictException;
import com.warhammer.primarchs.security.CustomUserDetailsService;
import com.warhammer.primarchs.security.JwtService;
import com.warhammer.primarchs.user.Role;
import com.warhammer.primarchs.user.User;
import com.warhammer.primarchs.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager,
                       CustomUserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new ConflictException("The username is already taken");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("The email is already registered");
        }
        User user = new User(
                request.username(),
                request.email(),
                passwordEncoder.encode(request.password()),
                Role.USER);
        userRepository.save(user);
        UserDetails details = userDetailsService.loadUserByUsername(user.getUsername());
        return new AuthResponse(jwtService.generateToken(details), user.getUsername(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        UserDetails details = userDetailsService.loadUserByUsername(request.username());
        User user = userRepository.findByUsername(request.username()).orElseThrow();
        return new AuthResponse(jwtService.generateToken(details), user.getUsername(), user.getRole().name());
    }
}
