package com.techlab.pedidos;
// Paquete donde se guarda la clase. 

import java.util.ArrayList;
import java.util.List;
// Se importan las clases necesarias para usar listas dinamicas.

// Clase que representa un pedido completo (con varias lineas de productos).
public class Pedido {

    // Contador estatico para asignar IDs unicos a cada pedido.
    // "static" hace que el valor sea compartido por todos los pedidos creados.
    private static int contadorId = 1;

    // Identificador unico del pedido.
    private final int id;

    // Lista que contiene las lineas de pedido (cada una con producto y cantidad).
    private final List<LineaPedido> lineas = new ArrayList<>();

    // Constructor: se ejecuta al crear un nuevo pedido.
    // Asigna un ID automatico y lo incrementa para el siguiente pedido.
    public Pedido() {
        this.id = contadorId++;
    }

    // Devuelve el ID del pedido.
    public int getId() { 
        return id; 
    }

    // Agrega una nueva linea (producto + cantidad) al pedido.
    public void agregarLinea(LineaPedido linea) {
        lineas.add(linea);
    }

    // Devuelve una copia de la lista de lineas.
    // Esto se hace para proteger la lista original y evitar que se modifique desde afuera.
    public List<LineaPedido> getLineas() {
        return new ArrayList<>(lineas); 
    }

    // Calcula el total del pedido sumando todos los subtotales de las lineas.
    public double calcularTotal() {
        double total = 0;
        for (LineaPedido lp : lineas) {
            total += lp.subtotal(); // suma el subtotal de cada linea
        }
        return total;
    }

    // Sobrescribe el metodo toString() para mostrar el pedido en formato de texto.
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pedido #").append(id).append("\n"); // muestra el numero del pedido

        // Recorre todas las lineas y las agrega al texto con un guion
        for (LineaPedido lp : lineas) {
            sb.append("  - ").append(lp).append("\n");
        }

        // Muestra el total al final
        sb.append(String.format("TOTAL: $%.2f", calcularTotal()));
        return sb.toString();
    }
}
