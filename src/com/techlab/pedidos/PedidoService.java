package com.techlab.pedidos;

import com.techlab.excepciones.StockInsuficienteException;

import com.techlab.productos.Producto;
import com.techlab.productos.ProductoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PedidoService {
    private final List<Pedido> pedidos = new ArrayList<>();
    private final Scanner sc = new Scanner(System.in);

    public void crearPedido(ProductoService productoService) {
        Pedido pedido = new Pedido();

        System.out.println("== Crear nuevo pedido ==");

        while (true) {
            System.out.print("Ingrese ID del producto (0 para finalizar): ");
            String input = sc.nextLine().trim();
            if (input.equals("0")) break;

            int id;
            try {
                id = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número válido.\n");
                continue;
            }

            Producto producto = productoService.buscarPorId(id);
            if (producto == null) {
                System.out.println("No existe un producto con ese ID.\n");
                continue;
            }

            System.out.print("Cantidad deseada: ");
            int cantidad;
            try {
                cantidad = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese una cantidad válida.\n");
                continue;
            }

            if (cantidad <= 0) {
                System.out.println("La cantidad debe ser mayor a 0.\n");
                continue;
            }

            try {
                validarStock(producto, cantidad);
                pedido.agregarLinea(new LineaPedido(producto, cantidad));
                producto.setStock(producto.getStock() - cantidad);
                System.out.println("Producto agregado al pedido.\n");
            } catch (StockInsuficienteException e) {
                System.out.println("!!!! " + e.getMessage() + "\n");
            }


            // Agrega la línea al pedido y descuenta stock
            pedido.agregarLinea(new LineaPedido(producto, cantidad));
            producto.setStock(producto.getStock() - cantidad);
            System.out.println("Producto agregado al pedido.\n");
        }

        if (pedido.getLineas().isEmpty()) {
            System.out.println("Pedido vacío, cancelado.\n");
        } else {
            pedidos.add(pedido);
            System.out.printf("Pedido #%d creado con éxito. Total: $%.2f%n%n",
                    pedido.getId(), pedido.calcularTotal());
        }
    }

    public void listarPedidos() {
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados.\n");
            return;
        }
        System.out.println("== Listado de pedidos ==");
        for (Pedido p : pedidos) {
            System.out.println(p);
            System.out.println("-------------------------------------");
        }
        System.out.println();
    }
    // 👇 ESTA ES LA NUEVA PARTE: método privado al final de la clase
    private void validarStock(Producto producto, int cantidad)
            throws StockInsuficienteException {
        if (cantidad > producto.getStock()) {
            throw new StockInsuficienteException(
                    String.format("Stock insuficiente para %s. Disponible: %d unidades.",
                            producto.getNombre(), producto.getStock()));
        }
    }
}
