package com.gatashoes.inventario.repository;

import com.gatashoes.inventario.api.dto.response.CategoriaStockResponse;
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
     * Suma total de stock.
     */
    @Query("SELECT COALESCE(SUM(i.stock), 0) FROM Inventario i")
    Long sumTotalStock();

    /**
     * Cuenta los registros con stock bajo.
     */
    @Query("SELECT COUNT(i) FROM Inventario i WHERE i.stock <= 3")
    Long countStockBajo();

    /**
     * Obtiene las categorías ordenadas por stock total.
     */
    @Query("SELECT new com.gatashoes.inventario.api.dto.response.CategoriaStockResponse(" +
            "c.nombreCategoria, SUM(i.stock)) " +
            "FROM Inventario i " +
            "JOIN i.producto p " +
            "JOIN p.categoria c " +
            "GROUP BY c.idCategoria, c.nombreCategoria " +
            "ORDER BY SUM(i.stock) DESC")
    List<CategoriaStockResponse> findTopCategoriasByStock(Pageable pageable);

}