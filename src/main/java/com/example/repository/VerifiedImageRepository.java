package com.example.repository;

import com.example.model.VerifiedImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VerifiedImageRepository extends JpaRepository<VerifiedImage, Long> {
    List<VerifiedImage> findByStatus(VerifiedImage.VerificationStatus status);
    List<VerifiedImage> findByOrderByUploadDateDesc();
}
