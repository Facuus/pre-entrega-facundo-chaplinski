package com.techlab.productos;

public class Producto {
    private static int contadorId = 1;

    private final int id;         // int: ID autogenerado
    private String nombre;        // String: nombre del producto
    private double precio;        // double: precio
    private int stock;            // int: stock

    public Producto(String nombre, double precio, int stock) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        this.id = contadorId++;
        this.nombre = nombre.trim();
        this.precio = precio;
        this.stock = stock;
    }

    // Getters / Setters (encapsulamiento)
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("Nombre inválido.");
        this.nombre = nombre.trim();
    }

    public void setPrecio(double precio) {
        if (precio < 0) throw new IllegalArgumentException("Precio negativo.");
        this.precio = precio;
    }

    public void setStock(int stock) {
        if (stock < 0) throw new IllegalArgumentException("Stock negativo.");
        this.stock = stock;
    }

    @Override
    public String toString() {
        return String.format("%-4d | %-25s | %10.2f | %5d",
                id, nombre, precio, stock);
    }
}
