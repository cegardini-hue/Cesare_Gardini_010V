package com.productos.producto_catalogo.service;

import com.productos.producto_catalogo.model.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<Producto> listarProductos();
    Optional<Producto> obtenerPorId(Long id);
    Producto crearProducto(Producto producto);
    void eliminarProducto(Long id);
}