package com.gatashoes.inventario.controller;

import com.gatashoes.inventario.dto.InventarioForm;
import com.gatashoes.inventario.model.Categoria;
import com.gatashoes.inventario.model.Color;
import com.gatashoes.inventario.model.Inventario;
import com.gatashoes.inventario.model.Producto;
import com.gatashoes.inventario.model.Talla;
import com.gatashoes.inventario.repository.CategoriaRepository;
import com.gatashoes.inventario.repository.ColorRepository;
import com.gatashoes.inventario.repository.InventarioRepository;
import com.gatashoes.inventario.repository.ProductoRepository;
import com.gatashoes.inventario.repository.TallaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;

@Controller
public class ProductoController {

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private TallaRepository tallaRepository;

    @Autowired
    private ColorRepository colorRepository;

    @PostMapping("/productos/guardar")
    public String guardarProducto(@ModelAttribute("inventarioForm") InventarioForm form) {

        // 1. Buscar la categoría directamente por el ID real seleccionado en el HTML
        Integer idCategoria = form.getProducto().getCategoria().getIdCategoria();
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new IllegalArgumentException("La categoría seleccionada no existe en la base de datos: " + idCategoria));

        // 2. Construir y guardar el Producto
        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre(form.getProducto().getNombre());
        nuevoProducto.setUrlImagen(form.getProducto().getUrlImagen());
        nuevoProducto.setCategoria(categoria);
        nuevoProducto.setPrecio(BigDecimal.valueOf(form.getProducto().getPrecio().doubleValue()));

        nuevoProducto = productoRepository.save(nuevoProducto);

        // 3. Buscar o registrar la Talla
        String numeroTalla = String.valueOf(form.getTalla().getNumero()).trim();
        Talla talla = tallaRepository.findByNumero(numeroTalla)
                .orElseGet(() -> {
                    Talla nuevaTalla = new Talla();
                    nuevaTalla.setNumero(numeroTalla);
                    return tallaRepository.save(nuevaTalla);
                });

        // 4. Buscar o registrar el Color
        String nombreColor = form.getColor().getNombreColor().trim();
        Color color = colorRepository.findByNombreColorIgnoreCase(nombreColor)
                .orElseGet(() -> {
                    Color nuevoColor = new Color();
                    nuevoColor.setNombreColor(nombreColor);
                    return colorRepository.save(nuevoColor);
                });

        // 5. Consolidar el registro en la tabla Inventario
        Inventario nuevoInventario = new Inventario();
        nuevoInventario.setProducto(nuevoProducto);
        nuevoInventario.setTalla(talla);
        nuevoInventario.setColor(color);
        nuevoInventario.setStock(form.getStock());

        inventarioRepository.save(nuevoInventario);

        // Redirección limpia para refrescar la vista del catálogo
        return "redirect:/resumen";
    }
}