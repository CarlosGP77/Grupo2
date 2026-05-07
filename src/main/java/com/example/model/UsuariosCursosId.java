package com.example.model;

import java.io.Serializable;
import java.util.Objects;

public class UsuariosCursosId implements Serializable {
    private String usuario;
    private Integer curso;
    public UsuariosCursosId() {}

    public UsuariosCursosId(String usuario, Integer curso) {
        this.usuario = usuario;
        this.curso = curso;
    }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public Integer getCurso() { return curso; }
    public void setCurso(Integer curso) { this.curso = curso; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuariosCursosId that = (UsuariosCursosId) o;
        return Objects.equals(usuario, that.usuario) && Objects.equals(curso, that.curso);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuario, curso);
    }
}

