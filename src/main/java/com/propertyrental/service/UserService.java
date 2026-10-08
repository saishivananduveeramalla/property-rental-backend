package com.propertyrental.service;

import com.propertyrental.dto.UserDTO;
import com.propertyrental.dto.UserProfileUpdateDTO;
import com.propertyrental.entity.Role;
import com.propertyrental.entity.User;
import com.propertyrental.exception.BadRequestException;
import com.propertyrental.exception.ResourceNotFoundException;
import com.propertyrental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getUsersByRole(Role role) {
        return userRepository.findByRole(role).stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO updateProfile(Long id, UserProfileUpdateDTO dto) {
        User currentUser = authService.getCurrentAuthenticatedUser();
        if (!currentUser.getId().equals(id) && currentUser.getRole() != Role.ROLE_ADMIN) {
            throw new BadRequestException("You can only update your own profile");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setName(dto.getName());
        user.setPhone(dto.getPhone());

        if (dto.getNewPassword() != null && !dto.getNewPassword().trim().isEmpty()) {
            if (currentUser.getRole() != Role.ROLE_ADMIN) {
                if (dto.getCurrentPassword() == null || !passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
                    throw new BadRequestException("Current password does not match");
                }
            }
            if (dto.getNewPassword().length() < 6) {
                throw new BadRequestException("New password must be at least 6 characters");
            }
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        }

        userRepository.save(user);
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO toggleUserStatus(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (user.getRole() == Role.ROLE_ADMIN) {
            throw new BadRequestException("Cannot disable system Administrator");
        }

        user.setEnabled(!user.isEnabled());
        userRepository.save(user);
        return new UserDTO(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (user.getRole() == Role.ROLE_ADMIN) {
            throw new BadRequestException("Cannot delete system Administrator");
        }

        userRepository.delete(user);
    }
}
