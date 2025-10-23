package com.techlab.pedidos;
// Paquete donde esta la clase. 

import com.techlab.excepciones.StockInsuficienteException;
// Se importa la excepcion personalizada que se lanza cuando el stock no alcanza.

import com.techlab.productos.Producto;
import com.techlab.productos.ProductoService;
// Se importan las clases de productos para poder usarlas dentro de esta clase.

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
// Se importan utilidades de Java: lista dinamica y lector por consola.

// Clase que maneja la logica de los pedidos: crearlos, listarlos y validar stock.
public class PedidoService {

    // Lista donde se guardan todos los pedidos realizados.
    private final List<Pedido> pedidos = new ArrayList<>();

    // Objeto Scanner para leer datos del usuario desde consola.
    private final Scanner sc = new Scanner(System.in);

    // Metodo que permite crear un nuevo pedido usando los productos existentes.
    public void crearPedido(ProductoService productoService) {
        // Se crea un nuevo pedido vacio.
        Pedido pedido = new Pedido();

        System.out.println("== Crear nuevo pedido ==");

        // Bucle principal que permite agregar varios productos al mismo pedido.
        while (true) {
            System.out.print("Ingrese ID del producto (0 para finalizar): ");
            String input = sc.nextLine().trim(); // lee el texto y elimina espacios
            if (input.equals("0")) break; // si se ingresa 0, termina el pedido

            int id;
            try {
                id = Integer.parseInt(input); // convierte el texto a numero
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero valido.\n");
                continue; // vuelve a pedir el ID
            }

            // Busca el producto por su ID en el servicio de productos.
            Producto producto = productoService.buscarPorId(id);
            if (producto == null) {
                System.out.println("No existe un producto con ese ID.\n");
                continue; // vuelve a pedir otro ID
            }

            System.out.print("Cantidad deseada: ");
            int cantidad;
            try {
                cantidad = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese una cantidad valida.\n");
                continue; // vuelve a pedir cantidad
            }

            if (cantidad <= 0) {
                System.out.println("La cantidad debe ser mayor a 0.\n");
                continue; // no permite cantidades negativas o cero
            }

            // Se intenta validar y agregar el producto al pedido.
            try {
                validarStock(producto, cantidad); // verifica si hay stock suficiente
                pedido.agregarLinea(new LineaPedido(producto, cantidad)); // agrega la linea
                producto.setStock(producto.getStock() - cantidad); // descuenta del stock
                System.out.println("Producto agregado al pedido.\n");
            } catch (StockInsuficienteException e) {
                // Si no hay stock suficiente, muestra el mensaje de error.
                System.out.println("!!!! " + e.getMessage() + "\n");
            }

            //  Este bloque repite la logica anterior (capaz conviene eliminar)
            // Agrega la linea al pedido y descuenta stock nuevamente.
            pedido.agregarLinea(new LineaPedido(producto, cantidad));
            producto.setStock(producto.getStock() - cantidad);
            System.out.println("Producto agregado al pedido.\n");
        }

        // Si el pedido no tiene productos, se cancela.
        if (pedido.getLineas().isEmpty()) {
            System.out.println("Pedido vacio, cancelado.\n");
        } else {
            // Si tiene productos, se guarda en la lista y se muestra el total.
            pedidos.add(pedido);
            System.out.printf("Pedido #%d creado con exito. Total: $%.2f%n%n",
                    pedido.getId(), pedido.calcularTotal());
        }
    }

    // Metodo para mostrar todos los pedidos registrados.
    public void listarPedidos() {
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados.\n");
            return;
        }

        System.out.println("== Listado de pedidos ==");
        // Recorre todos los pedidos y los imprime uno por uno.
        for (Pedido p : pedidos) {
            System.out.println(p);
            System.out.println("-------------------------------------");
        }
        System.out.println();
    }

    // ==============================================================
    //  Metodo privado: valida si hay stock suficiente antes de vender
    // ==============================================================
    private void validarStock(Producto producto, int cantidad)
            throws StockInsuficienteException {
        // Si la cantidad pedida supera el stock disponible, lanza excepcion.
        if (cantidad > producto.getStock()) {
            throw new StockInsuficienteException(
                    String.format("Stock insuficiente para %s. Disponible: %d unidades.",
                            producto.getNombre(), producto.getStock()));
        }
    }
}
