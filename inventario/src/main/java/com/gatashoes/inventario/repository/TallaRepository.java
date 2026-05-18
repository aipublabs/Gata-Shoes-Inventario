package com.gatashoes.inventario.repository;

import com.gatashoes.inventario.model.Talla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TallaRepository extends JpaRepository<Talla, Integer> {
    // Buscamos por String porque tu modelo define 'numero' como texto
    Optional<Talla> findByNumero(String numero);
}