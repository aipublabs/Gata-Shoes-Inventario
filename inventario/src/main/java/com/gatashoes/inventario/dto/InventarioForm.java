package com.gatashoes.inventario.dto;

public class InventarioForm {
    private ProductoForm producto = new ProductoForm();
    private TallaForm talla = new TallaForm();
    private ColorForm color = new ColorForm();
    private Integer stock;

    // Getters y Setters
    public ProductoForm getProducto() { return producto; }
    public void setProducto(ProductoForm producto) { this.producto = producto; }
    public TallaForm getTalla() { return talla; }
    public void setTalla(TallaForm talla) { this.talla = talla; }
    public ColorForm getColor() { return color; }
    public void setColor(ColorForm color) { this.color = color; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public static class ProductoForm {
        private String nombre;
        private Double precio;
        private String urlImagen;
        private CategoriaForm categoria = new CategoriaForm();

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public Double getPrecio() { return precio; }
        public void setPrecio(Double precio) { this.precio = precio; }
        public String getUrlImagen() { return urlImagen; }
        public void setUrlImagen(String urlImagen) { this.urlImagen = urlImagen; }
        public CategoriaForm getCategoria() { return categoria; }
        public void setCategoria(CategoriaForm categoria) { this.categoria = categoria; }
    }

    public static class CategoriaForm {
        private Integer idCategoria;
        public Integer getIdCategoria() { return idCategoria; }
        public void setIdCategoria(Integer idCategoria) { this.idCategoria = idCategoria; }
    }

    public static class TallaForm {
        private Integer numero;
        public Integer getNumero() { return numero; }
        public void setNumero(Integer numero) { this.numero = numero; }
    }

    public static class ColorForm {
        private String nombreColor;
        public String getNombreColor() { return nombreColor; }
        public void setNombreColor(String nombreColor) { this.nombreColor = nombreColor; }
    }
}