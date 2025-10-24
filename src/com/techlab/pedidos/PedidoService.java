package com.techlab.pedidos;
// Paquete donde esta la clase.

import com.techlab.excepciones.StockInsuficienteException;
import com.techlab.productos.Producto;
import com.techlab.productos.ProductoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Clase que maneja la logica de los pedidos: crearlos, listarlos y validar stock.
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
                System.out.println("Ingrese un numero valido.\n");
                continue;
            }

            Producto producto = productoService.buscarPorId(id);
            if (producto == null) {
                System.out.println("No existe un producto con ese ID.\n");
                continue;
            }

            //  Nueva validacion: si el producto tiene stock 0, no permite avanzar
            if (producto.getStock() <= 0) {
                System.out.println("!!! El producto '" + producto.getNombre() + "' está sin stock.\n");
                continue;
            }

            //  mensaje de confirmación al seleccionar producto 
            System.out.printf("Seleccionaste: %s | Precio: $%.2f | Stock disponible: %d%n",
                    producto.getNombre(), producto.getPrecio(), producto.getStock());

            System.out.print("Cantidad deseada: ");
            int cantidad;
            try {
                cantidad = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese una cantidad valida.\n");
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
                System.out.println(" Producto agregado al pedido.\n");

            } catch (StockInsuficienteException e) {
                System.out.println("!!!! " + e.getMessage());

                // Preguntar si desea continuar igual con el stock disponible
                System.out.print("¿Desea continuar con las " + producto.getStock() +
                        " unidades disponibles? (s/n): ");
                String respuesta = sc.nextLine().trim().toLowerCase();

                if (respuesta.equals("s")) {
                    int disponible = producto.getStock();
                    pedido.agregarLinea(new LineaPedido(producto, disponible));
                    producto.setStock(0);
                    System.out.println("Producto agregado con " + disponible + " unidades.\n");
                } else {
                    System.out.println("Producto no agregado al pedido.\n");
                }
            }
        }

        if (pedido.getLineas().isEmpty()) {
            System.out.println("Pedido vacio, cancelado.\n");
        } else {
            pedidos.add(pedido);
            System.out.printf("Pedido #%d creado con exito. Total: $%.2f%n%n",
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

    // ============24/10
    //  Metodo privado: valida si hay stock suficiente antes de vender

    private void validarStock(Producto producto, int cantidad)
            throws StockInsuficienteException {
        if (cantidad > producto.getStock()) {
            throw new StockInsuficienteException(
                    String.format("Stock insuficiente para %s. Disponible: %d unidades.",
                            producto.getNombre(), producto.getStock()));
        }
    }
}
