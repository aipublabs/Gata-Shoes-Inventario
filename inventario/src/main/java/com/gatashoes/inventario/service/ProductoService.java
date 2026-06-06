package com.gatashoes.inventario.service;

import com.gatashoes.inventario.api.exception.ResourceNotFoundException;
import com.gatashoes.inventario.model.Producto;
import com.gatashoes.inventario.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto obtenerProductoPorId(Integer idProducto) {
        return productoRepository.findById(idProducto).orElse(null);
    }

    public Producto obtenerProductoPorIdOrThrow(Integer idProducto) {
        return productoRepository.findById(idProducto)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id " + idProducto));
    }

    public Producto actualizarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public void eliminarProducto(Integer idProducto) {
        if (!productoRepository.existsById(idProducto)) {
            throw new ResourceNotFoundException("Producto no encontrado con id " + idProducto);
        }
        productoRepository.deleteById(idProducto);
    }
}
