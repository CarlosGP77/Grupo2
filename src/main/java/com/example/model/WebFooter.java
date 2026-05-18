package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "webfooters")
@SuppressWarnings("unused")
public class WebFooter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_footer")
    private Integer idFooter;

    @Column(name = "tipo_info", length = 255)
    private String tipoInfo;

    @Column(columnDefinition = "TEXT")
    private String contenido;

    public Integer getIdFooter() {
        return idFooter;
    }

    public void setIdFooter(Integer idFooter) {
        this.idFooter = idFooter;
    }

    public String getTipoInfo() {
        return tipoInfo;
    }

    public void setTipoInfo(String tipoInfo) {
        this.tipoInfo = tipoInfo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}

