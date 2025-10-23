package com.techlab.pedidos;
// Paquete donde esta la clase. Sirve para agrupar el codigo relacionado con pedidos.

import com.techlab.productos.Producto;
// Se importa la clase Producto porque cada linea de pedido contiene un producto.

// Clase que representa una linea dentro de un pedido (un producto + su cantidad).
public class LineaPedido {

    // Producto al que hace referencia esta linea.
    private final Producto producto;

    // Cantidad del producto pedida.
    private final int cantidad;

    // Constructor: se ejecuta al crear una nueva linea de pedido.
    // Recibe el producto y la cantidad, valida que la cantidad sea positiva.
    public LineaPedido(Producto producto, int cantidad) {
        if (cantidad <= 0) {
            // Se lanza una excepcion si se ingresa una cantidad invalida.
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }
        this.producto = producto;  // se asigna el producto
        this.cantidad = cantidad;  // se asigna la cantidad
    }

    // Devuelve el producto asociado a esta linea.
    public Producto getProducto() { 
        return producto; 
    }

    // Devuelve la cantidad pedida.
    public int getCantidad() { 
        return cantidad; 
    }

    // Calcula el subtotal de esta linea (precio * cantidad).
    public double subtotal() {
        return producto.getPrecio() * cantidad;
    }

    // Sobrescribe toString() para mostrar la linea en texto legible.
    // Ejemplo: "Cloro granulado x2 = $1500.00"
    @Override
    public String toString() {
        return String.format("%s x%d = $%.2f",
                producto.getNombre(), cantidad, subtotal());
    }
}
