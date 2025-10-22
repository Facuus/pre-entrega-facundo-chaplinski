package com.techlab.productos;

import java.util.ArrayList;
import java.util.List;

public class ProductoService {
    private final List<Producto> productos = new ArrayList<>(); // ArrayList<Producto>

    public Producto agregar(String nombre, double precio, int stock) {
        Producto p = new Producto(nombre, precio, stock);
        productos.add(p);
        return p;
    }

    public List<Producto> listar() {
        // devolvemos una copia para proteger la lista interna (encapsulamiento)
        return new ArrayList<>(productos);
    }

    // Métodos que implementaremos en el Paso 2
    public Producto buscarPorId(int id) {
        for (Producto p : productos) if (p.getId() == id) return p;
        return null;
    }

    public Producto buscarPorNombre(String nombre) {
        for (Producto p : productos)
            if (p.getNombre().equalsIgnoreCase(nombre)) return p;
        return null;
    }

    public boolean eliminar(int id) {
        return productos.removeIf(p -> p.getId() == id);
    }

    public boolean actualizar(int id, Double nuevoPrecio, Integer nuevoStock) {
        Producto p = buscarPorId(id);
        if (p == null) return false;
        if (nuevoPrecio != null) p.setPrecio(nuevoPrecio);
        if (nuevoStock != null) p.setStock(nuevoStock);
        return true;
    }
}
