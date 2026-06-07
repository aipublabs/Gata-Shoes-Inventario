package com.gatashoes.inventario.model;

import jakarta.persistence.*;

/**
 * Entidad que representa una categoría de clasificación de productos.
 * 
 * Las categorías organizan los productos del catálogo de Gata Shoes
 * por tipo (ej: Zapatos Formales, Deportivos, Casuales, etc.)
 * 
 * Cada producto pertenece a una categoría, permitiendo una mejor
 * estructura y navegación del catálogo.
 * 
 * Mapea a la tabla "categorias" en la base de datos.
 */
@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer idCategoria;

    @Column(name = "nombre_categoria", nullable = false, length = 50)
    private String nombreCategoria;

    public Categoria() {
    }

    /**
     * Obtiene el identificador único de la categoría.
     * 
     * @return ID de la categoría
     */
    public Integer getIdCategoria() {
        return idCategoria;
    }

    /**
     * Define el identificador único de la categoría.
     * 
     * @param idCategoria ID de la categoría a establecer
     */
    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    /**
     * Obtiene el nombre de la categoría.
     * 
     * @return Nombre descriptivo de la categoría (ej: "Zapatos Formales")
     */
    public String getNombreCategoria() {
        return nombreCategoria;
    }

    /**
     * Define el nombre de la categoría.
     * 
     * @param nombreCategoria Nombre descriptivo de la categoría a establecer
     */
    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }
}