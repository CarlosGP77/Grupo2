package com.example.repository;

import com.example.model.VerifiedImage;
import com.example.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VerifiedImageRepository extends JpaRepository<VerifiedImage, Long> {
    List<VerifiedImage> findByStatus(VerifiedImage.VerificationStatus status);
    List<VerifiedImage> findByOrderByUploadDateDesc();
    List<VerifiedImage> findByUsuario(Usuario usuario);
    List<VerifiedImage> findByUsuarioAndStatus(Usuario usuario, VerifiedImage.VerificationStatus status);
    List<VerifiedImage> findByUsuarioOrderByUploadDateDesc(Usuario usuario);
}
