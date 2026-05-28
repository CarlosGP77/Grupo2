package com.example.service;

public interface FileStorageService {
    boolean uploadFile(byte[] fileContent, String filepath);
    byte[] downloadFile(String filepath);
    boolean deleteFile(String filepath);
    boolean fileExists(String filepath);
}