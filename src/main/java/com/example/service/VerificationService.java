package com.example.service;

import com.example.model.VerifiedImage;
import com.example.model.Usuario;
import com.example.repository.VerifiedImageRepository;
import com.example.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VerificationService {

    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
        "image/jpeg", "image/png", "image/gif", "image/webp",
        "application/pdf"
    );
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
        "jpg", "jpeg", "png", "gif", "webp", "pdf"
    );
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    @Autowired
    private VerifiedImageRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private NextcloudService nextcloudService;

    public VerifiedImage uploadImage(MultipartFile file) throws IOException {
        // Validaciones
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || filename.isBlank()) {
            throw new IllegalArgumentException("Nombre de archivo inválido");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("El archivo excede el tamaño máximo de 10MB");
        }

        String mimeType = file.getContentType();
        if (mimeType == null || !ALLOWED_MIME_TYPES.contains(mimeType.toLowerCase())) {
            throw new IllegalArgumentException("Tipo de archivo no permitido");
        }

        String extension = getFileExtension(filename);
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("Extensión de archivo no permitida");
        }

        // Generar nombre único
        String uniqueFilename = generateUniqueFilename(filename);

        // Subir a Nextcloud
        if (!nextcloudService.uploadFile(file.getBytes(), uniqueFilename)) {
            throw new RuntimeException("Error al subir el archivo a Nextcloud");
        }

        // Guardar en BD
        VerifiedImage image = new VerifiedImage(
            uniqueFilename,
            "/Verificador/imagenes/" + uniqueFilename,
            file.getSize(),
            mimeType
        );

        VerifiedImage saved = repository.save(image);
        log.info("Imagen subida y registrada: {}", uniqueFilename);
        return saved;
    }

    public VerifiedImage uploadCertificate(MultipartFile file, Usuario usuario) throws IOException {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no autenticado");
        }

        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || filename.isBlank()) {
            throw new IllegalArgumentException("Nombre de archivo inválido");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("El archivo excede el tamaño máximo de 10MB");
        }

        String mimeType = file.getContentType();
        if (mimeType == null || !ALLOWED_MIME_TYPES.contains(mimeType.toLowerCase())) {
            throw new IllegalArgumentException("Tipo de archivo no permitido");
        }

        String extension = getFileExtension(filename);
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("Extensión de archivo no permitida");
        }

        String uniqueFilename = generateUniqueFilename(filename);
        String dniFolder = usuario.getDni();
        String nextcloudPath = "/Certificados/" + dniFolder + "/" + uniqueFilename;

        if (!nextcloudService.uploadFile(file.getBytes(), nextcloudPath)) {
            throw new RuntimeException("Error al subir el certificado a Nextcloud");
        }

        VerifiedImage certificate = new VerifiedImage(usuario, uniqueFilename, nextcloudPath, file.getSize(), mimeType);
        VerifiedImage saved = repository.save(certificate);
        log.info("Certificado subido para usuario {}: {}", usuario.getDni(), uniqueFilename);
        return saved;
    }

    public List<VerifiedImage> getCertificatesByUsuario(Usuario usuario) {
        return repository.findByUsuarioOrderByUploadDateDesc(usuario);
    }

    public List<VerifiedImage> getPendingCertificatesByUsuario(Usuario usuario) {
        return repository.findByUsuarioAndStatus(usuario, VerifiedImage.VerificationStatus.PENDING);
    }

    public List<VerifiedImage> getPendingImages() {
        return repository.findByStatus(VerifiedImage.VerificationStatus.PENDING)
            .stream()
            .sorted(Comparator.comparing(VerifiedImage::getUploadDate).reversed())
            .collect(Collectors.toList());
    }

    public List<VerifiedImage> getVerifiedImages() {
        return repository.findAll()
            .stream()
            .filter(img -> img.getStatus() != VerifiedImage.VerificationStatus.PENDING)
            .sorted(Comparator.comparing(VerifiedImage::getVerificationDate).reversed())
            .collect(Collectors.toList());
    }

    public VerifiedImage verifyImage(Long id, VerifiedImage.VerificationStatus status,
                                     String comment, String verifiedBy) {
        VerifiedImage image = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Imagen no encontrada"));

        image.setStatus(status);
        image.setComment(comment);
        image.setVerifiedBy(verifiedBy);
        image.setVerificationDate(LocalDateTime.now());

        VerifiedImage updated = repository.save(image);
        log.info("Imagen verificada: {} - Status: {}", id, status);
        return updated;
    }

    public byte[] getImageContent(Long id) {
        VerifiedImage image = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Imagen no encontrada"));

        return nextcloudService.downloadFile(image.getFilename());
    }

    public byte[] downloadImage(Long id) {
        return getImageContent(id);
    }

    public void deleteImage(Long id) {
        VerifiedImage image = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Imagen no encontrada"));

        // Eliminar de Nextcloud
        if (!nextcloudService.deleteFile(image.getFilename())) {
            log.warn("Error al eliminar archivo de Nextcloud: {}", image.getFilename());
        }

        // Eliminar de BD
        repository.deleteById(id);
        log.info("Imagen eliminada: {}", id);
    }

    public Map<String, Object> getStatistics() {
        List<VerifiedImage> all = repository.findAll();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalImages", all.size());
        stats.put("pending", all.stream().filter(img -> img.getStatus() == VerifiedImage.VerificationStatus.PENDING).count());
        stats.put("approved", all.stream().filter(img -> img.getStatus() == VerifiedImage.VerificationStatus.APPROVED).count());
        stats.put("rejected", all.stream().filter(img -> img.getStatus() == VerifiedImage.VerificationStatus.REJECTED).count());
        stats.put("needsRevision", all.stream().filter(img -> img.getStatus() == VerifiedImage.VerificationStatus.NEEDS_REVISION).count());

        long totalSize = all.stream().mapToLong(img -> img.getFileSize() != null ? img.getFileSize() : 0).sum();
        stats.put("totalSize", formatBytes(totalSize));
        stats.put("totalSizeBytes", totalSize);

        return stats;
    }

    public VerifiedImage getImageById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Imagen no encontrada"));
    }

    private String generateUniqueFilename(String originalFilename) {
        String timestamp = System.currentTimeMillis() + "";
        String extension = getFileExtension(originalFilename);
        String nameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
        return nameWithoutExtension + "_" + timestamp + "." + extension;
    }

    private String getFileExtension(String filename) {
        int lastIndexOf = filename.lastIndexOf('.');
        if (lastIndexOf == -1) {
            return "";
        }
        return filename.substring(lastIndexOf + 1);
    }

    private String formatBytes(long bytes) {
        if (bytes <= 0) return "0 B";
        final String[] units = new String[]{"B", "KB", "MB", "GB"};
        int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));
        return String.format("%.2f %s", bytes / Math.pow(1024, digitGroups), units[digitGroups]);
    }
}
