package com.example.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class LocalFileStorageService implements FileStorageService {

    @Value("${file.storage.path:/data/uploads}")
    private String storagePath;

    @Override
    public boolean uploadFile(byte[] fileContent, String filepath) {
        try {
            Path path = Paths.get(storagePath, filepath);
            Path parentDir = path.getParent();

            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }

            Files.write(path, fileContent);
            log.info("Archivo guardado localmente: {}", filepath);
            return true;
        } catch (IOException e) {
            log.error("Error al guardar archivo: {}", filepath, e);
            return false;
        }
    }

    @Override
    public byte[] downloadFile(String filepath) {
        try {
            Path path = Paths.get(storagePath, filepath);
            if (Files.exists(path)) {
                return Files.readAllBytes(path);
            } else {
                log.warn("Archivo no encontrado: {}", filepath);
                return null;
            }
        } catch (IOException e) {
            log.error("Error al descargar archivo: {}", filepath, e);
            return null;
        }
    }

    @Override
    public boolean deleteFile(String filepath) {
        try {
            Path path = Paths.get(storagePath, filepath);
            if (Files.exists(path)) {
                Files.delete(path);
                log.info("Archivo eliminado: {}", filepath);
                return true;
            } else {
                log.warn("Archivo no encontrado para eliminar: {}", filepath);
                return false;
            }
        } catch (IOException e) {
            log.error("Error al eliminar archivo: {}", filepath, e);
            return false;
        }
    }

    @Override
    public boolean fileExists(String filepath) {
        Path path = Paths.get(storagePath, filepath);
        return Files.exists(path);
    }
}