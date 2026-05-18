package com.gatashoes.inventario.repository;

import com.gatashoes.inventario.model.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ColorRepository extends JpaRepository<Color, Integer> {
    Optional<Color> findByNombreColorIgnoreCase(String nombreColor);
}