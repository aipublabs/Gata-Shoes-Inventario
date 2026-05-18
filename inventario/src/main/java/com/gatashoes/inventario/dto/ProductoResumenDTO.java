package com.gatashoes.inventario.dto;

public class ProductoResumenDTO {

    private Integer idProducto;
    private String nombre;
    private String categoria;
    private String imagen;
    private Integer stock;

    public ProductoResumenDTO(
            Integer idProducto,
            String nombre,
            String categoria,
            String imagen,
            Integer stock
    ) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.categoria = categoria;
        this.imagen = imagen;
        this.stock = stock;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getImagen() {
        return imagen;
    }

    public Integer getStock() {
        return stock;
    }
}