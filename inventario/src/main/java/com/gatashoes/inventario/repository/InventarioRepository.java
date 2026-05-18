package com.gatashoes.inventario.repository;

import com.gatashoes.inventario.dto.CategoriaStockDTO;
import com.gatashoes.inventario.model.Inventario;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Integer> {

    /**
     * Obtiene los últimos 5 registros de inventario
     * ordenados por ID descendente.
     */
    List<Inventario> findTop5ByOrderByIdInventarioDesc();

    /**
     * Obtiene los 3 productos con mayor stock.
     */
    List<Inventario> findTop3ByOrderByStockDesc();

    /**
     * Obtiene las categorías ordenadas por stock total.
     */
    @Query("SELECT new com.gatashoes.inventario.dto.CategoriaStockDTO(c.nombreCategoria, SUM(i.stock)) " +
            "FROM Inventario i " +
            "JOIN i.producto p " +
            "JOIN p.categoria c " +
            "GROUP BY c.idCategoria, c.nombreCategoria " +
            "ORDER BY SUM(i.stock) DESC")
    List<CategoriaStockDTO> findTopCategoriasByStock(Pageable pageable);

}