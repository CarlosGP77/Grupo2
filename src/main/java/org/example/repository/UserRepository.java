package org.example.repository;

import org.example.model.User;
import org.example.model.UserRole;
import org.example.model.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findByRole(UserRole role);

    List<User> findByVerificationStatus(VerificationStatus status);

    List<User> findByRoleAndVerificationStatus(UserRole role, VerificationStatus status);

    Integer countByRole(UserRole role);

    Integer countByVerificationStatus(VerificationStatus status);
}

