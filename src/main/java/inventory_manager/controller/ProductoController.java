package inventory_manager.controller;

import inventory_manager.model.Producto;
import inventory_manager.model.Categoria;
import inventory_manager.repository.ProductoRepository;
import inventory_manager.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public List<Producto> getAll(
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Integer stockMenorQue) {

        if (categoriaId != null) {
            return productoRepository.findByCategoriaId(categoriaId);
        }
        if (stockMenorQue != null) {
            return productoRepository.findByStockLessThan(stockMenorQue);
        }
        return productoRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Producto producto) {
        // Si viene un categoria.id, cargamos la categoría real de la BBDD
        if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
            Optional<Categoria> categoriaOpt = categoriaRepository.findById(producto.getCategoria().getId());
            if (categoriaOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Categoría no encontrada");
            }
            producto.setCategoria(categoriaOpt.get());
        }
        Producto saved = productoRepository.save(producto);
        return ResponseEntity.status(201).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Producto datos) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Producto producto = productoOpt.get();
        producto.setNombre(datos.getNombre());
        producto.setDescripcion(datos.getDescripcion());
        producto.setStock(datos.getStock());
        producto.setPrecio(datos.getPrecio());

        if (datos.getCategoria() != null && datos.getCategoria().getId() != null) {
            categoriaRepository.findById(datos.getCategoria().getId())
                    .ifPresent(producto::setCategoria);
        }

        return ResponseEntity.ok(productoRepository.save(producto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!productoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}