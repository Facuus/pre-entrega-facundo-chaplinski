package com.techlab.pedidos;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private static int contadorId = 1;

    private final int id;
    private final List<LineaPedido> lineas = new ArrayList<>();

    public Pedido() {
        this.id = contadorId++;
    }

    public int getId() { return id; }

    public void agregarLinea(LineaPedido linea) {
        lineas.add(linea);
    }

    public List<LineaPedido> getLineas() {
        return new ArrayList<>(lineas); // se devuelve copia para proteger la lista
    }

    public double calcularTotal() {
        double total = 0;
        for (LineaPedido lp : lineas) {
            total += lp.subtotal();
        }
        return total;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pedido #").append(id).append("\n");
        for (LineaPedido lp : lineas) {
            sb.append("  - ").append(lp).append("\n");
        }
        sb.append(String.format("TOTAL: $%.2f", calcularTotal()));
        return sb.toString();
    }
}
