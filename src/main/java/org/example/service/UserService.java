package org.example.service;

import org.example.model.User;
import org.example.model.UserRole;
import org.example.model.VerificationStatus;
import org.example.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String username, String password, String email, String firstName, String lastName) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .role(UserRole.USUARIO)
                .verificationStatus(VerificationStatus.PENDING)
                .enabled(true)
                .build();

        return userRepository.save(user);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    public List<User> findUnverifiedUsers() {
        return userRepository.findByVerificationStatus(VerificationStatus.PENDING);
    }

    public User verifyUser(Long userId, VerificationStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setVerificationStatus(status);
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Integer getTotalUsers() {
        return (int) userRepository.count();
    }

    public Integer getTotalAdmins() {
        return userRepository.countByRole(UserRole.ADMIN);
    }

    public Integer getTotalVerificadores() {
        return userRepository.countByRole(UserRole.VERIFICADOR);
    }

    public Integer getTotalUsuarios() {
        return userRepository.countByRole(UserRole.USUARIO);
    }

    public Integer getPendingVerifications() {
        return userRepository.countByVerificationStatus(VerificationStatus.PENDING);
    }
}

