package com.propertyrental.service;

import com.propertyrental.dto.AuthRequest;
import com.propertyrental.dto.AuthResponse;
import com.propertyrental.dto.RegisterRequest;
import com.propertyrental.entity.Role;
import com.propertyrental.entity.User;
import com.propertyrental.exception.BadRequestException;
import com.propertyrental.exception.UnauthorizedException;
import com.propertyrental.repository.UserRepository;
import com.propertyrental.security.JwtUtils;
import com.propertyrental.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("Error: Email is already in use!");
        }

        // Restrict public registration to ROLE_TENANT or ROLE_OWNER
        Role role = registerRequest.getRole();
        if (role == null || role == Role.ROLE_ADMIN) {
            role = Role.ROLE_TENANT;
        }

        User user = new User(
                registerRequest.getName(),
                registerRequest.getEmail().toLowerCase().trim(),
                registerRequest.getPhone(),
                encoder.encode(registerRequest.getPassword()),
                role
        );

        userRepository.save(user);

        // Auto authenticate after registration
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), registerRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        return new AuthResponse(jwt, user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getRole());
    }

    public AuthResponse login(AuthRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail().toLowerCase().trim(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        if (!user.isEnabled()) {
            throw new UnauthorizedException("Your account is disabled. Please contact administrator.");
        }

        return new AuthResponse(jwt, user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getRole());
    }

    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new UnauthorizedException("User is not authenticated");
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UnauthorizedException("User not found"));
    }
}
