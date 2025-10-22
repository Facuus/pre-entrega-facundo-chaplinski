package com.techlab.app;

import com.techlab.pedidos.PedidoService;

import com.techlab.productos.Producto;
import com.techlab.productos.ProductoService;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final PedidoService pedidoService = new PedidoService();

    private static final Scanner sc = new Scanner(System.in);
    private static final ProductoService productoService = new ProductoService();

    public static void main(String[] args) {
        productoService.agregar("Café Premium", 3500.50, 20);
        productoService.agregar("Té Verde", 2100, 35);

        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            int opcion = leerInt("Elija una opcion: ");
            System.out.println();

            switch (opcion) {
                case 1 -> opcionAgregarProducto();
                case 2 -> opcionListarProductos();
                case 3 -> opcionBuscarActualizar();
                case 4 -> opcionEliminar();
                case 5 -> pedidoService.crearPedido(productoService);
                case 6 -> pedidoService.listarPedidos();
                case 7 -> {
                    System.out.println("Saliendo... ¡Gracias!");
                    salir = true;
                }

                default -> System.out.println("Opcion invalida. Intente nuevamente.\n");
            }
            if (!salir) pausar();
        }
    }

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

    // === PASO 1 ===
    private static void opcionAgregarProducto() {
        System.out.println("== Agregar producto ==");
        String nombre = leerTexto("Nombre: ");
        double precio = leerDouble("Precio: ");
        int stock = leerInt("Stock: ");
        try {
            Producto p = productoService.agregar(nombre, precio, stock);
            System.out.println("Producto agregado con ID " + p.getId() + ".\n");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void opcionListarProductos() {
        System.out.println("== Listado de productos ==");
        List<Producto> lista = productoService.listar();
        if (lista.isEmpty()) {
            System.out.println("(No hay productos cargados)\n");
            return;
        }
        System.out.printf("%-4s | %-25s | %10s | %5s%n", "ID", "NOMBRE", "PRECIO", "STOCK");
        System.out.println("-------------------------------------------------------------");
        for (Producto p : lista) System.out.println(p);
        System.out.println();
    }

    // === PASO 2 ===
    private static void opcionBuscarActualizar() {
        System.out.println("== Buscar producto ==");
        String criterio = leerTexto("Ingrese nombre o ID: ");
        Producto encontrado = null;

        try {
            int id = Integer.parseInt(criterio);
            encontrado = productoService.buscarPorId(id);
        } catch (NumberFormatException e) {
            encontrado = productoService.buscarPorNombre(criterio);
        }

        if (encontrado == null) {
            System.out.println("No se encontró el producto.\n");
            return;
        }

        System.out.println("Producto encontrado:");
        System.out.println(encontrado);

        System.out.print("¿Desea actualizarlo? (s/n): ");
        String resp = sc.nextLine().trim();
        if (resp.equalsIgnoreCase("s")) {
            Double nuevoPrecio = null;
            Integer nuevoStock = null;

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

    private static void opcionEliminar() {
        System.out.println("== Eliminar producto ==");
        int id = leerInt("Ingrese el ID del producto a eliminar: ");
        Producto p = productoService.buscarPorId(id);
        if (p == null) {
            System.out.println("No se encontró ese ID.\n");
            return;
        }

        System.out.println("Seleccionado: " + p.getNombre() + " (stock " + p.getStock() + ")");
        System.out.print("¿Está seguro que desea eliminarlo? (s/n): ");
        String resp = sc.nextLine().trim();
        if (resp.equalsIgnoreCase("s")) {
            boolean ok = productoService.eliminar(id);
            if (ok) System.out.println("Producto eliminado.\n");
            else System.out.println("No se pudo eliminar.\n");
        } else {
            System.out.println("Operación cancelada.\n");
        }
    }

    // === Helpers ===
    private static int leerInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            try {
                return Integer.parseInt(s.trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número entero válido.");
            }
        }
    }

    private static double leerDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            try {
                return Double.parseDouble(s.trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número válido.");
            }
        }
    }

    private static String leerTexto(String prompt) {
        System.out.print(prompt);
        String s = sc.nextLine();
        while (s == null || s.isBlank()) {
            System.out.print("El texto no puede estar vacío. Intente nuevamente: ");
            s = sc.nextLine();
        }
        return s.trim();
    }

    private static void pausar() {
        System.out.print("Presione ENTER para continuar...");
        sc.nextLine();
        System.out.println();
    }
}
