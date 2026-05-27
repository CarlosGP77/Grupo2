package com.example.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class NextcloudService {

    @Value("${nextcloud.url:http://nextcloud_app}")
    private String nextcloudUrl;

    @Value("${nextcloud.username:admin}")
    private String username;

    @Value("${nextcloud.password:}")
    private String password;

    @Value("${nextcloud.images-path:/Verificador/imagenes/}")
    private String imagesPath;

    private String getAuthHeader() {
        String auth = username + ":" + password;
        return "Basic " + Base64.encodeBase64String(auth.getBytes(StandardCharsets.UTF_8));
    }

    public boolean uploadFile(byte[] fileContent, String filename) {
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

    public byte[] downloadFile(String filename) {
        try {
            String encodedPath = URLEncoder.encode(filename, StandardCharsets.UTF_8.name());
            String fullPath = imagesPath + encodedPath;
            String url = nextcloudUrl + "/remote.php/webdav" + fullPath;

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

    public boolean deleteFile(String filename) {
        try {
            String encodedPath = URLEncoder.encode(filename, StandardCharsets.UTF_8.name());
            String fullPath = imagesPath + encodedPath;
            String url = nextcloudUrl + "/remote.php/webdav" + fullPath;

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Authorization", getAuthHeader());

            int responseCode = conn.getResponseCode();
            if (responseCode == 204) {
                log.info("Archivo eliminado de Nextcloud: {}", filename);
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
            String url = nextcloudUrl + "/remote.php/webdav" + imagesPath;
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
            String url = nextcloudUrl + "/remote.php/webdav" + imagesPath;
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
