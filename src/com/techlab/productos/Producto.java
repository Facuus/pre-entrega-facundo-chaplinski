package com.techlab.productos; 
// Paquete donde se encuentra la clase. 

// Clase que representa un producto individual del sistema.
public class Producto {
    // ================================
    //  Atributos (propiedades)
    // ================================

    // Contador estatico para generar IDs unicos. 
    // "static" hace que sea compartido por todas las instancias de Producto.
    private static int contadorId = 1;

    private final int id;         // ID unico e inmutable del producto 
    private String nombre;        // Nombre del producto 
    private double precio;        // Precio actual del producto
    private int stock;            // Cantidad disponible 

    // ================================
    //  Constructor
    // ================================
    // Se ejecuta al crear un nuevo producto.
    // Recibe nombre, precio y stock, valida los datos y genera el ID automático.
    public Producto(String nombre, double precio, int stock) {
        // Validaciones para evitar datos incorrectos
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }

        // Asigna un ID unico y aumenta el contador para el siguiente producto.
        this.id = contadorId++;

        // Guarda los valores pasados al constructor en los atributos del objeto.
        this.nombre = nombre.trim(); // trim() elimina espacios extra
        this.precio = precio;
        this.stock = stock;
    }

    // ================================
    //  Metodos Getters (lectura)
    // ================================
    // Permiten acceder a los valores privados desde fuera de la clase (encapsulamiento).
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }

    // ================================
    //  Metodos Setters (modificacion)
    // ================================
    // Permiten cambiar valores, validando que los datos sean correctos.

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

    // ================================
    //  Metodo toString()
    // ================================
    // Se ejecuta automaticamente cuando se imprime el objeto.
    // Devuelve una representacion en texto con formato de columnas.
    @Override
    public String toString() {
        return String.format("%-4d | %-25s | %10.2f | %5d",
                id, nombre, precio, stock);
        // %-4d : número alineado a la izquierda con 4 espacios
        // %-25s : texto alineado a la izquierda con 25 espacios
        // %10.2f : número con 2 decimales y ancho de 10
        // %5d : número entero con ancho de 5
    }
}
