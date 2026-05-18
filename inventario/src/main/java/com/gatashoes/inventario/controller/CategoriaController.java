package com.gatashoes.inventario.controller;

import com.gatashoes.inventario.model.Categoria;
import com.gatashoes.inventario.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    /**
     * Mostrar listado de categorías
     */
    @GetMapping
    public String listarCategorias(Model model) {

        model.addAttribute("categorias",
                categoriaService.listarCategorias());

        return "categorias";
    }

    /**
     * Mostrar formulario nueva categoría
     */
    @GetMapping("/nueva")
    public String mostrarFormularioNuevaCategoria(Model model) {

        model.addAttribute("categoria", new Categoria());

        return "categoria-form";
    }

    /**
     * Guardar categoría
     */
    @PostMapping("/guardar")
    public String guardarCategoria(@ModelAttribute Categoria categoria) {

        categoriaService.guardarCategoria(categoria);

        return "redirect:/categorias";
    }

    /**
     * Mostrar formulario editar categoría
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarCategoria(
            @PathVariable Integer id,
            Model model) {

        Categoria categoria =
                categoriaService.obtenerCategoriaPorId(id);

        model.addAttribute("categoria", categoria);

        return "categoria-form";
    }

    /**
     * Actualizar categoría
     */
    @PostMapping("/actualizar/{id}")
    public String actualizarCategoria(
            @PathVariable Integer id,
            @ModelAttribute Categoria categoria) {

        categoria.setIdCategoria(id);

        categoriaService.actualizarCategoria(categoria);

        return "redirect:/categorias";
    }

    /**
     * Eliminar categoría
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Integer id) {

        categoriaService.eliminarCategoria(id);

        return "redirect:/categorias";
    }
}