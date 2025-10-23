package com.techlab.app;
// Paquete principal del programa. Agrupa la clase Main, punto de entrada de la aplicacion.

import com.techlab.pedidos.PedidoService;
import com.techlab.productos.Producto;
import com.techlab.productos.ProductoService;
// Se importan las clases necesarias para manejar productos y pedidos.

import java.util.List;
import java.util.Scanner;
// Se importan utilidades de Java para listas y lectura por consola.

// Clase principal que ejecuta el programa. Contiene el menu y las acciones del usuario.
public class Main {

    // Servicio que maneja los pedidos.
    private static final PedidoService pedidoService = new PedidoService();

    // Scanner para leer entradas desde consola.
    private static final Scanner sc = new Scanner(System.in);

    // Servicio que maneja los productos.
    private static final ProductoService productoService = new ProductoService();

    // Metodo principal. Es el punto de inicio del programa.
    public static void main(String[] args) {

        /*
        Ejemplo de carga manual de productos (se puede usar para pruebas).
        productoService.agregar("Choco", 3500.50, 20);
        productoService.agregar("perra", 2100, 35);
        */

        boolean salir = false; // variable para controlar el bucle principal del menu

        // Bucle que muestra el menu hasta que el usuario elija salir.
        while (!salir) {
            mostrarMenu(); // muestra las opciones
            int opcion = leerInt("Elija una opcion: ");
            System.out.println();

            // Se usa switch moderno (Java 14+) para ejecutar la opcion seleccionada.
            switch (opcion) {
                case 1 -> opcionAgregarProducto(); // agrega un nuevo producto
                case 2 -> opcionListarProductos(); // lista todos los productos
                case 3 -> opcionBuscarActualizar(); // busca o actualiza producto
                case 4 -> opcionEliminar(); // elimina producto
                case 5 -> pedidoService.crearPedido(productoService); // crea pedido
                case 6 -> pedidoService.listarPedidos(); // lista pedidos existentes
                case 7 -> { // salir del sistema
                    System.out.println("Saliendo... Gracias!");
                    salir = true;
                }
                default -> System.out.println("Opcion invalida. Intente nuevamente.\n");
            }

            // Si no se eligio salir, se pausa antes de mostrar el menu otra vez.
            if (!salir) pausar();
        }
    }

    // Muestra el menu principal con todas las opciones disponibles.
    private static void mostrarMenu() {
        System.out.println("===========================================");
        System.out.println("SISTEMA DE GESTION == TECHLAB");
        System.out.println("===========================================");
        System.out.println("1) Agregar producto");
        System.out.println("2) Listar productos");
        System.out.println("3) Buscar/Actualizar producto");
        System.out.println("4) Eliminar producto");
        System.out.println("5) Crear un pedido");
        System.out.println("6) Listar pedidos");
        System.out.println("7) Salir\n");
    }

    // === Opcion 1: Agregar un nuevo producto ===
    private static void opcionAgregarProducto() {
        System.out.println("== Agregar producto ==");
        String nombre = leerTexto("Nombre: ");
        double precio = leerDouble("Precio: ");
        int stock = leerInt("Stock: ");
        try {
            // Se agrega el producto al servicio
            Producto p = productoService.agregar(nombre, precio, stock);
            System.out.println("Producto agregado con ID " + p.getId() + ".\n");
        } catch (IllegalArgumentException e) {
            // Si hubo error de validacion, se muestra el mensaje.
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    // === Opcion 2: Listar todos los productos ===
    private static void opcionListarProductos() {
        System.out.println("== Listado de productos ==");
        List<Producto> lista = productoService.listar();
        if (lista.isEmpty()) {
            System.out.println("(No hay productos cargados)\n");
            return;
        }
        // Encabezado de tabla
        System.out.printf("%-4s | %-25s | %10s | %5s%n", "ID", "NOMBRE", "PRECIO", "STOCK");
        System.out.println("-------------------------------------------------------------");
        // Se muestra cada producto usando su metodo toString()
        for (Producto p : lista) System.out.println(p);
        System.out.println();
    }

    // === Opcion 3: Buscar producto por nombre o ID y actualizarlo ===
    private static void opcionBuscarActualizar() {
        System.out.println("== Buscar producto ==");
        String criterio = leerTexto("Ingrese nombre o ID: ");
        Producto encontrado = null;

        // Se intenta convertir el texto a ID; si no se puede, busca por nombre.
        try {
            int id = Integer.parseInt(criterio);
            encontrado = productoService.buscarPorId(id);
        } catch (NumberFormatException e) {
            encontrado = productoService.buscarPorNombre(criterio);
        }

        if (encontrado == null) {
            System.out.println("No se encontro el producto.\n");
            return;
        }

        System.out.println("Producto encontrado:");
        System.out.println(encontrado);

        // Permite decidir si se actualiza el producto o no.
        System.out.print("Desea actualizarlo? (s/n): ");
        String resp = sc.nextLine().trim();
        if (resp.equalsIgnoreCase("s")) {
            Double nuevoPrecio = null;
            Integer nuevoStock = null;

            // Si el usuario ingresa un nuevo valor, se actualiza. Si deja vacio, se mantiene.
            System.out.print("Nuevo precio (ENTER para mantener): ");
            String p = sc.nextLine().trim();
            if (!p.isEmpty()) nuevoPrecio = Double.parseDouble(p);

            System.out.print("Nuevo stock (ENTER para mantener): ");
            String s = sc.nextLine().trim();
            if (!s.isEmpty()) nuevoStock = Integer.parseInt(s);

            productoService.actualizar(encontrado.getId(), nuevoPrecio, nuevoStock);
            System.out.println("Producto actualizado correctamente.\n");
        }
    }

    // === Opcion 4: Eliminar un producto existente ===
    private static void opcionEliminar() {
        System.out.println("== Eliminar producto ==");
        int id = leerInt("Ingrese el ID del producto a eliminar: ");
        Producto p = productoService.buscarPorId(id);
        if (p == null) {
            System.out.println("No se encontro ese ID.\n");
            return;
        }

        System.out.println("Seleccionado: " + p.getNombre() + " (stock " + p.getStock() + ")");
        System.out.print("Esta seguro que desea eliminarlo? (s/n): ");
        String resp = sc.nextLine().trim();
        if (resp.equalsIgnoreCase("s")) {
            boolean ok = productoService.eliminar(id);
            if (ok) System.out.println("Producto eliminado.\n");
            else System.out.println("No se pudo eliminar.\n");
        } else {
            System.out.println("Operacion cancelada.\n");
        }
    }

    // === Metodos auxiliares para leer datos desde consola ===

    // Lee un numero entero y valida la entrada.
    private static int leerInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            try {
                return Integer.parseInt(s.trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero valido.");
            }
        }
    }

    // Lee un numero decimal (double) y valida el formato.
    private static double leerDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            try {
                // Reemplaza coma por punto para aceptar ambos formatos.
                return Double.parseDouble(s.trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero valido.");
            }
        }
    }

    // Lee texto y asegura que no este vacio.
    private static String leerTexto(String prompt) {
        System.out.print(prompt);
        String s = sc.nextLine();
        while (s == null || s.isBlank()) {
            System.out.print("El texto no puede estar vacio. Intente nuevamente: ");
            s = sc.nextLine();
        }
        return s.trim();
    }

    // Pausa el programa hasta que el usuario presione ENTER.
    private static void pausar() {
        System.out.print("Presione ENTER para continuar...");
        sc.nextLine();
        System.out.println();
    }
}
