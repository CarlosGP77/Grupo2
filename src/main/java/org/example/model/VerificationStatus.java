package org.example.model;

public enum VerificationStatus {
    PENDING("Pendiente"),
    VERIFIED("Verificado"),
    REJECTED("Rechazado");

    private final String description;

    VerificationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

