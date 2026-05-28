package com.example.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class LocalFileStorageService implements FileStorageService {

    @Value("${file.storage.path:/data/uploads}")
    private String storagePath;

    private static final String FALLBACK_STORAGE_DIR = "grupo2-uploads";

    @Override
    public boolean uploadFile(byte[] fileContent, String filepath) {
        String normalizedPath = normalizeRelativePath(filepath);
        try {
            Path path = buildStoragePath(storagePath, normalizedPath);
            writeFile(path, fileContent);
            log.info("Archivo guardado localmente: {}", normalizedPath);
            return true;
        } catch (IOException e) {
            log.warn("Error al guardar archivo en ruta principal ({}): {}", storagePath, normalizedPath, e);
            try {
                Path fallbackPath = buildStoragePath(getFallbackStoragePath(), normalizedPath);
                writeFile(fallbackPath, fileContent);
                log.info("Archivo guardado en fallback: {}", fallbackPath);
                return true;
            } catch (IOException fallbackError) {
                log.error("Error al guardar archivo en fallback: {}", normalizedPath, fallbackError);
                return false;
            }
        }
    }

    @Override
    public byte[] downloadFile(String filepath) {
        String normalizedPath = normalizeRelativePath(filepath);
        try {
            Path path = resolveExistingPath(normalizedPath);
            if (Files.exists(path)) {
                return Files.readAllBytes(path);
            } else {
                log.warn("Archivo no encontrado: {}", normalizedPath);
                return null;
            }
        } catch (IOException e) {
            log.error("Error al descargar archivo: {}", normalizedPath, e);
            return null;
        }
    }

    @Override
    public boolean deleteFile(String filepath) {
        String normalizedPath = normalizeRelativePath(filepath);
        try {
            Path path = resolveExistingPath(normalizedPath);
            if (Files.exists(path)) {
                Files.delete(path);
                log.info("Archivo eliminado: {}", normalizedPath);
                return true;
            } else {
                log.warn("Archivo no encontrado para eliminar: {}", normalizedPath);
                return false;
            }
        } catch (IOException e) {
            log.error("Error al eliminar archivo: {}", normalizedPath, e);
            return false;
        }
    }

    @Override
    public boolean fileExists(String filepath) {
        String normalizedPath = normalizeRelativePath(filepath);
        Path path = resolveExistingPath(normalizedPath);
        return Files.exists(path);
    }

    private Path buildStoragePath(String basePath, String relativePath) {
        return Paths.get(basePath).resolve(relativePath).normalize();
    }

    private Path resolveExistingPath(String relativePath) {
        Path primary = buildStoragePath(storagePath, relativePath);
        if (Files.exists(primary)) {
            return primary;
        }
        return buildStoragePath(getFallbackStoragePath(), relativePath);
    }

    private void writeFile(Path path, byte[] fileContent) throws IOException {
        Path parentDir = path.getParent();
        if (parentDir != null) {
            Files.createDirectories(parentDir);
        }
        Files.write(path, fileContent);
    }

    private String normalizeRelativePath(String filepath) {
        if (filepath == null) {
            return "";
        }
        // Evita que una ruta con '/' o '\\' inicial se convierta en absoluta.
        return filepath.replace('\\', '/').replaceFirst("^/+", "");
    }

    private String getFallbackStoragePath() {
        return Paths.get(System.getProperty("java.io.tmpdir"), FALLBACK_STORAGE_DIR).toString();
    }
}