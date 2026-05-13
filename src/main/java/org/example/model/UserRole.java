package org.example.model;

public enum UserRole {
    ADMIN("Administrador"),
    VERIFICADOR("Verificador"),
    USUARIO("Usuario");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

