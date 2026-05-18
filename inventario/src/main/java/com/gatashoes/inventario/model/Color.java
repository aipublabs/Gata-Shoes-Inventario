package com.gatashoes.inventario.model;

import jakarta.persistence.*;

@Entity
@Table(name = "colores")
public class Color {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_color")
    private Integer idColor;

    @Column(name = "nombre_color", nullable = false, length = 30)
    private String nombreColor;

    public Color() {
    }

    public Integer getIdColor() {
        return idColor;
    }

    public void setIdColor(Integer idColor) {
        this.idColor = idColor;
    }

    public String getNombreColor() {
        return nombreColor;
    }

    public void setNombreColor(String nombreColor) {
        this.nombreColor = nombreColor;
    }
} // <-- Esta llave cierra la clase Color