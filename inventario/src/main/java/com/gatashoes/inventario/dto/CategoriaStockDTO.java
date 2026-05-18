package com.gatashoes.inventario.dto; // Ajusta el paquete al tuyo

public class CategoriaStockDTO {
    private String nombreCategoria;
    private Long stock;

    public CategoriaStockDTO(String nombreCategoria, Long stock) {
        this.nombreCategoria = nombreCategoria;
        this.stock = stock;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }
}