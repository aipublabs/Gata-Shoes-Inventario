package com.gatashoes.inventario.controller;

import com.gatashoes.inventario.dto.InventarioForm;
import com.gatashoes.inventario.dto.CategoriaStockDTO;
import com.gatashoes.inventario.repository.CategoriaRepository;
import com.gatashoes.inventario.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ResumenController {

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping("/resumen")
    public String mostrarResumen(Model model) {
        // 1. Catálogo completo (usado estrictamente para cálculos de totales globales y estadísticas)
        model.addAttribute("listaInventario", inventarioRepository.findAll());

        // 2. Consulta de novedades (Top 5 más recientes)
        model.addAttribute("listaNovedades", inventarioRepository.findTop5ByOrderByIdInventarioDesc());

        // 3. Consulta filtrada para la tabla (Top 3 artículos con mayor stock)
        model.addAttribute("listaTopStock", inventarioRepository.findTop3ByOrderByStockDesc());

        // 4. Categorías para el selector dinámico del pop-up modal
        model.addAttribute("listaCategorias", categoriaRepository.findAll());

        // 5. Contenedor del formulario
        model.addAttribute("inventarioForm", new InventarioForm());

        // 6. NUEVA: Consulta del Top 4 de categorías con mayor stock de forma 100% dinámica
        List<CategoriaStockDTO> topCategoriasStock = inventarioRepository.findTopCategoriasByStock(PageRequest.of(0, 4));
        model.addAttribute("topCategoriasStock", topCategoriasStock);

        return "Resumen";
    }
}