package org.example.service;

import org.example.model.Qualification;
import org.example.model.User;
import org.example.repository.QualificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class QualificationService {

    private final QualificationRepository qualificationRepository;

    public QualificationService(QualificationRepository qualificationRepository) {
        this.qualificationRepository = qualificationRepository;
    }

    public Qualification createQualification(User user, String title, String issuer, LocalDate issueDate, String description) {
        Qualification qualification = Qualification.builder()
                .user(user)
                .title(title)
                .issuer(issuer)
                .issueDate(issueDate)
                .description(description)
                .verified(false)
                .createdAt(LocalDateTime.now())
                .build();

        return qualificationRepository.save(qualification);
    }

    public Qualification saveQualification(Qualification qualification) {
        return qualificationRepository.save(qualification);
    }

    public Optional<Qualification> findById(Long id) {
        return qualificationRepository.findById(id);
    }

    public List<Qualification> findByUser(User user) {
        return qualificationRepository.findByUser(user);
    }

    public List<Qualification> findVerifiedByUser(User user) {
        return qualificationRepository.findByUserAndVerified(user, true);
    }

    public List<Qualification> findUnverifiedByUser(User user) {
        return qualificationRepository.findByUserAndVerified(user, false);
    }

    public List<Qualification> findAllUnverified() {
        return qualificationRepository.findByVerifiedFalse();
    }

    public List<Qualification> findAll() {
        return qualificationRepository.findAll();
    }

    public Qualification verifyQualification(Long id) {
        Qualification qualification = qualificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Qualification not found"));
        qualification.setVerified(true);
        qualification.setUpdatedAt(LocalDateTime.now());
        return qualificationRepository.save(qualification);
    }

    public void deleteQualification(Long id) {
        qualificationRepository.deleteById(id);
    }

    public Integer getTotalQualifications() {
        return (int) qualificationRepository.count();
    }

    public Integer getTotalQualificationsByUser(User user) {
        return qualificationRepository.countByUser(user);
    }

    public Integer getTotalVerifiedQualificationsByUser(User user) {
        return qualificationRepository.countByUserAndVerified(user, true);
    }

    public Integer getPendingQualifications() {
        return qualificationRepository.countByVerifiedFalse();
    }
}

