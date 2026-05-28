package com.example.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Primary
@Service
public class NextcloudService implements FileStorageService {

    @Value("${nextcloud.url:http://nextcloud_app}")
    private String nextcloudUrl;

    @Value("${nextcloud.username:admin}")
    private String username;

    @Value("${nextcloud.password:}")
    private String password;

    @Value("${nextcloud.images-path:/Verificador/imagenes/}")
    private String imagesPath;

    private String webDavBaseUrl() {
        String base = trimTrailingSlash(nextcloudUrl);
        // Endpoint recomendado en Nextcloud actual.
        return base + "/remote.php/dav/files/" + encodePathSegment(username) + "/";
    }

    private String fileUrl(String filepath) {
        return webDavBaseUrl() + encodeRelativePath(filepath);
    }

    private String trimTrailingSlash(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }

    private String normalizeRelativePath(String filepath) {
        if (filepath == null) {
            return "";
        }
        return filepath.replace('\\', '/').replaceFirst("^/+", "");
    }

    private String encodePathSegment(String segment) {
        return URLEncoder.encode(segment, StandardCharsets.UTF_8).replace("+", "%20");
    }

    private String encodeRelativePath(String filepath) {
        String normalized = normalizeRelativePath(filepath);
        if (normalized.isBlank()) {
            return "";
        }

        String[] parts = normalized.split("/");
        StringBuilder encoded = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].isBlank()) {
                continue;
            }
            if (encoded.length() > 0) {
                encoded.append('/');
            }
            encoded.append(encodePathSegment(parts[i]));
        }
        return encoded.toString();
    }

    private String getAuthHeader() {
        String auth = username + ":" + password;
        return "Basic " + Base64.encodeBase64String(auth.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean fileExists(String filepath) {
        try {
            String url = fileUrl(filepath);
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("PROPFIND");
            conn.setRequestProperty("Authorization", getAuthHeader());
            conn.setRequestProperty("Depth", "0");
            int responseCode = conn.getResponseCode();
            return responseCode == 207 || responseCode == 200;
        } catch (Exception e) {
            log.warn("Error comprobando archivo en Nextcloud: {}", filepath, e);
            return false;
        }
    }

    @Override
    public boolean uploadFile(byte[] fileContent, String filepath) {
        try {
            ensureDirectoryExists(filepath);
            String url = fileUrl(filepath);

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Authorization", getAuthHeader());
            conn.setRequestProperty("Content-Length", String.valueOf(fileContent.length));
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(fileContent);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 201 || responseCode == 204) {
                log.info("Archivo subido a Nextcloud: {}", filepath);
                return true;
            } else {
                log.error("Error al subir a Nextcloud: código {}", responseCode);
                return false;
            }
        } catch (Exception e) {
            log.error("Error en uploadFile", e);
            return false;
        }
    }

    private void ensureDirectoryExists(String filepath) {
        try {
            String normalized = normalizeRelativePath(filepath);
            String[] parts = normalized.split("/");
            StringBuilder current = new StringBuilder();

            for (int i = 0; i < parts.length - 1; i++) {
                if (parts[i].isBlank()) {
                    continue;
                }

                if (current.length() > 0) {
                    current.append('/');
                }
                current.append(encodePathSegment(parts[i]));

                String url = webDavBaseUrl() + current;
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod("MKCOL");
                conn.setRequestProperty("Authorization", getAuthHeader());

                int responseCode = conn.getResponseCode();
                // 201 = creado, 405 = ya existe
                if (responseCode != 201 && responseCode != 405) {
                    log.debug("MKCOL {} devolvio codigo {}", url, responseCode);
                }
            }
        } catch (Exception e) {
            log.warn("Error al crear directorios en Nextcloud", e);
        }
    }

    public boolean uploadFileLegacy(byte[] fileContent, String filename) {
        try {
            String encodedPath = URLEncoder.encode(filename, StandardCharsets.UTF_8.name());
            String fullPath = imagesPath + encodedPath;
            String url = nextcloudUrl + "/remote.php/webdav" + fullPath;

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Authorization", getAuthHeader());
            conn.setRequestProperty("Content-Length", String.valueOf(fileContent.length));
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(fileContent);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 201 || responseCode == 204) {
                log.info("Archivo subido a Nextcloud: {}", filename);
                return true;
            } else {
                log.error("Error al subir a Nextcloud: código {}", responseCode);
                return false;
            }
        } catch (Exception e) {
            log.error("Error en uploadFile", e);
            return false;
        }
    }

    @Override
    public byte[] downloadFile(String filepath) {
        try {
            String url = fileUrl(filepath);

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", getAuthHeader());

            if (conn.getResponseCode() == 200) {
                try (InputStream is = conn.getInputStream()) {
                    return is.readAllBytes();
                }
            } else {
                log.error("Error al descargar de Nextcloud: código {}", conn.getResponseCode());
                return null;
            }
        } catch (Exception e) {
            log.error("Error en downloadFile", e);
            return null;
        }
    }

    @Override
    public boolean deleteFile(String filepath) {
        try {
            String url = fileUrl(filepath);

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Authorization", getAuthHeader());

            int responseCode = conn.getResponseCode();
            if (responseCode == 204) {
                log.info("Archivo eliminado de Nextcloud: {}", filepath);
                return true;
            } else {
                log.error("Error al eliminar de Nextcloud: código {}", responseCode);
                return false;
            }
        } catch (Exception e) {
            log.error("Error en deleteFile", e);
            return false;
        }
    }

    public List<String> listFiles() {
        List<String> files = new ArrayList<>();
        try {
            String url = webDavBaseUrl() + encodeRelativePath(imagesPath);
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("PROPFIND");
            conn.setRequestProperty("Authorization", getAuthHeader());
            conn.setRequestProperty("Depth", "1");

            if (conn.getResponseCode() == 207) {
                try (InputStream is = conn.getInputStream()) {
                    String response = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                    // Simple parsing XML response (basic implementation)
                    String[] lines = response.split("\n");
                    for (String line : lines) {
                        if (line.contains("<d:href>")) {
                            String href = line.substring(line.indexOf("<d:href>") + 8, line.indexOf("</d:href>"));
                            if (!href.endsWith("/")) {
                                files.add(href.substring(href.lastIndexOf('/') + 1));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error en listFiles", e);
        }
        return files;
    }

    public boolean validateConnection() {
        try {
            String url = webDavBaseUrl();
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("PROPFIND");
            conn.setRequestProperty("Authorization", getAuthHeader());
            conn.setRequestProperty("Depth", "0");

            return conn.getResponseCode() == 207;
        } catch (Exception e) {
            log.error("Error al validar conexión a Nextcloud", e);
            return false;
        }
    }
}
