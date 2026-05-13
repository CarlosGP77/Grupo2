package org.example.repository;

import org.example.model.Qualification;
import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QualificationRepository extends JpaRepository<Qualification, Long> {
    List<Qualification> findByUser(User user);

    List<Qualification> findByUserAndVerified(User user, Boolean verified);

    Integer countByUser(User user);

    Integer countByUserAndVerified(User user, Boolean verified);

    List<Qualification> findByVerifiedFalse();

    Integer countByVerifiedFalse();
}

