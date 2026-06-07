package com.gatashoes.inventario.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Entidad que representa un producto del catálogo de Gata Shoes.
 * 
 * Un producto es el artículo base del inventario. Contiene la información
 * general del producto como nombre, descripción, precio e imagen.
 * 
 * Cada producto puede tener múltiples variantes en el inventario
 * (diferentes tallas, colores y cantidades).
 * 
 * Mapea a la tabla "productos" en la base de datos.
 */
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    @ManyToOne
    @JoinColumn(name = "id_categoria", referencedColumnName = "id_categoria")
    private Categoria categoria;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "url_imagen", length = 255)
    private String urlImagen;

    public Producto() {
    }

    /**
     * Obtiene el identificador único del producto.
     * 
     * @return ID del producto
     */
    public Integer getIdProducto() {
        return idProducto;
    }

    /**
     * Define el identificador único del producto.
     * 
     * @param idProducto ID del producto a establecer
     */
    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    /**
     * Obtiene la categoría a la que pertenece este producto.
     * 
     * @return Objeto Categoria
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * Define la categoría a la que pertenece este producto.
     * 
     * @param categoria Objeto Categoria a establecer
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /**
     * Obtiene el nombre comercial del producto.
     * 
     * @return Nombre del producto (ej: "Zapato Formal Negro")
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Define el nombre comercial del producto.
     * 
     * @param nombre Nombre del producto a establecer
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la descripción detallada del producto.
     * 
     * @return Descripción del producto con características y detalles
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Define la descripción detallada del producto.
     * 
     * @param descripcion Descripción del producto a establecer
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el precio de venta del producto en pesos colombianos (COP).
     * 
     * @return Precio en BigDecimal con 2 dígitos decimales
     */
    public BigDecimal getPrecio() {
        return precio;
    }

    /**
     * Define el precio de venta del producto.
     * 
     * @param precio Precio en pesos colombianos (COP) a establecer
     */
    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    /**
     * Obtiene la URL de la imagen del producto.
     * 
     * @return URL de la imagen (puede ser null si no se ha cargado)
     */
    public String getUrlImagen() {
        return urlImagen;
    }

    /**
     * Define la URL de la imagen del producto.
     * 
     * @param urlImagen URL de la imagen a establecer
     */
    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }
}