package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "verified_images")
public class VerifiedImage {

    public enum VerificationStatus {
        PENDING,
        APPROVED,
        REJECTED,
        NEEDS_REVISION
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String nextcloudPath;

    @Column(nullable = false)
    private LocalDateTime uploadDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus status = VerificationStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column
    private String verifiedBy;

    @Column
    private LocalDateTime verificationDate;

    @Column
    private Long fileSize;

    @Column
    private String mimeType;

    public VerifiedImage() {}

    public VerifiedImage(String filename, String nextcloudPath, Long fileSize, String mimeType) {
        this.filename = filename;
        this.nextcloudPath = nextcloudPath;
        this.uploadDate = LocalDateTime.now();
        this.status = VerificationStatus.PENDING;
        this.fileSize = fileSize;
        this.mimeType = mimeType;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getNextcloudPath() {
        return nextcloudPath;
    }

    public void setNextcloudPath(String nextcloudPath) {
        this.nextcloudPath = nextcloudPath;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public VerificationStatus getStatus() {
        return status;
    }

    public void setStatus(VerificationStatus status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(String verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public LocalDateTime getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(LocalDateTime verificationDate) {
        this.verificationDate = verificationDate;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
