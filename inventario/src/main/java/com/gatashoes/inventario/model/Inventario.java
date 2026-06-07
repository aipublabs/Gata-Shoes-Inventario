package com.gatashoes.inventario.model;

import jakarta.persistence.*;

/**
 * Entidad que representa una variante de producto en el inventario.
 * 
 * Una variante es la combinación de:
 * - Un producto específico (ej: Zapato de Cuero Negro)
 * - Una talla (ej: 42)
 * - Un color (ej: Negro)
 * - Una cantidad de stock disponible
 * 
 * Mapea a la tabla "inventario" en la base de datos.
 */
@Entity
@Table(name = "inventario")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventario")
    private Integer idInventario;

    @ManyToOne
    @JoinColumn(name = "id_producto", referencedColumnName = "id_producto")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "id_talla", referencedColumnName = "id_talla")
    private Talla talla;

    @ManyToOne
    @JoinColumn(name = "id_color", referencedColumnName = "id_color")
    private Color color;

    @Column(nullable = false)
    private Integer stock;

    public Inventario() {
    }

    /**
     * Obtiene el identificador único de la variante de inventario.
     * 
     * @return ID del inventario
     */
    public Integer getIdInventario() {
        return idInventario;
    }

    /**
     * Define el identificador único de la variante de inventario.
     * 
     * @param idInventario ID del inventario a establecer
     */
    public void setIdInventario(Integer idInventario) {
        this.idInventario = idInventario;
    }

    /**
     * Obtiene el producto asociado a esta variante de inventario.
     * 
     * @return Objeto Producto
     */
    public Producto getProducto() {
        return producto;
    }

    /**
     * Define el producto asociado a esta variante de inventario.
     * 
     * @param producto Objeto Producto a establecer
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    /**
     * Obtiene la talla asociada a esta variante de inventario.
     * 
     * @return Objeto Talla
     */
    public Talla getTalla() {
        return talla;
    }

    /**
     * Define la talla asociada a esta variante de inventario.
     * 
     * @param talla Objeto Talla a establecer
     */
    public void setTalla(Talla talla) {
        this.talla = talla;
    }

    /**
     * Obtiene el color asociado a esta variante de inventario.
     * 
     * @return Objeto Color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Define el color asociado a esta variante de inventario.
     * 
     * @param color Objeto Color a establecer
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Obtiene la cantidad de stock disponible para esta variante.
     * 
     * @return Número de unidades disponibles
     */
    public Integer getStock() {
        return stock;
    }

    /**
     * Define la cantidad de stock disponible para esta variante.
     * 
     * @param stock Número de unidades a establecer
     */
    public void setStock(Integer stock) {
        this.stock = stock;
    }
}