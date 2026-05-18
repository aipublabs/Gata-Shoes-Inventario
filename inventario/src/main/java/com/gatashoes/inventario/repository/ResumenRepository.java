package com.gatashoes.inventario.repository;

import com.gatashoes.inventario.model.Administrador;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ResumenRepository extends CrudRepository<Administrador, Integer> {

    @Query(value = "SELECT COUNT(*) FROM productos", nativeQuery = true)
    int totalProductos();

    @Query(value = "SELECT COUNT(*) FROM categorias", nativeQuery = true)
    int totalCategorias();

    @Query(value = "SELECT COUNT(*) FROM inventario WHERE stock < 10", nativeQuery = true)
    int stockBajo();
}