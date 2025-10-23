package com.techlab.productos; 
// Paquete donde está esta clase.  ----- com\techlab\productos\ProductoService.java

import java.io.*; 
// para usar BufferedReader, BufferedWriter, File....

import java.util.ArrayList;
import java.util.List;
// SArrayList se usa para guardar varios productos temporales.

// Clase que maneja toda la lógica de los productos: agregar, listar, buscar, eliminar y guardar/cargar en archivo.
public class ProductoService {

    // Lista que almacena todos los productos cargados en memoria.
    private final List<Producto> productos = new ArrayList<>();

    // Nombre del archivo donde se guardan los productos. "final" porque no cambia durante la ejecución.
    private final String archivoDatos = "productos.txt"; // ruta del archivo en la carpeta del programa

    // Constructor de la clase. Se ejecuta cuando se crea un objeto ProductoService.
    // Llama automáticamente a "cargarDesdeArchivo()" para traer los productos ya guardados en el txt.
    public ProductoService() {
        cargarDesdeArchivo(); // se ejecuta al crear el servicio
    }

    // Método para agregar un nuevo producto.
    // Crea un objeto Producto con los datos recibidos, lo agrega a la lista y guarda todo en el archivo.
    public Producto agregar(String nombre, double precio, int stock) {
        Producto p = new Producto(nombre, precio, stock); // crea el producto con los datos dados
        productos.add(p); // lo agrega a la lista en memoria
        guardarEnTxt(); // actualiza el archivo con el nuevo producto
        return p; // devuelve el producto agregado
    }

    // Devuelver lista de los productos.
    // crrear nueva lista para evitar modificar la original .
    public List<Producto> listar() {
        return new ArrayList<>(productos);
    }

    // Busca un producto por su ID.
    // Recorre la lista y devuelve el primero que tenga el ID que se busca.
    public Producto buscarPorId(int id) {
        for (Producto p : productos)
            if (p.getId() == id) return p;
        return null; // si no lo encuentra, devuelve null
    }

    // Busca un producto por nombre, ignorando mayúsculas o minúsculas.
    public Producto buscarPorNombre(String nombre) {
        for (Producto p : productos)
            if (p.getNombre().equalsIgnoreCase(nombre)) return p;
        return null; // si no lo encuentra, devuelve null
    }

    // Elimina un producto por ID.
    // Si se elimina correctamente, guarda los cambios en el archivo.
    public boolean eliminar(int id) {
        boolean eliminado = productos.removeIf(p -> p.getId() == id); // elimina si el id coincide
        if (eliminado) guardarEnTxt(); // guarda si se borra algo
        return eliminado;
    }

    // Actualiza el precio y/o el stock de un producto.
    // Busca el producto por ID, y si lo encuentra, actualiza los campos que no sean nulos.
    public boolean actualizar(int id, Double nuevoPrecio, Integer nuevoStock) {
        Producto p = buscarPorId(id); // busca el producto
        if (p == null) return false; // si no lo encuentra, termina

        if (nuevoPrecio != null) p.setPrecio(nuevoPrecio); // cambia precio si se pasó
        if (nuevoStock != null) p.setStock(nuevoStock);   // cambia stock si se pasó
        guardarEnTxt(); // guarda los cambios en el archivo
        return true;
    }

    // =====================================================
    //  GUARDAR EN ARCHIVO TXT
    // NEUVo
    // =====================================================
    // Este método guarda todos los productos en un archivo de texto (productos.txt).
    // Cada línea representa un producto, separado por punto y coma (;)
    private void guardarEnTxt() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoDatos))) {
            // "try-with-resources": se cierra automáticamente el BufferedWriter al terminar.
            for (Producto p : productos) {
                // Escribe los datos de cada producto en una línea
                bw.write(p.getId() + ";" + p.getNombre() + ";" + p.getPrecio() + ";" + p.getStock());
                bw.newLine(); // salta a la siguiente línea
            }
        } catch (IOException e) {
            // Se ejecuta si ocurre un error al escribir el archivo
            System.out.println("ERROR al guardar productos: " + e.getMessage());
        }
    }

    // =====================================================
    //  CARGAR DESDE ARCHIVO TXT
    // Nuevo
    // =====================================================
    // Este método lee el archivo productos.txt y carga todos los productos en memoria (lista).
    private void cargarDesdeArchivo() {
        File archivo = new File(archivoDatos); // se crea el objeto File que apunta al txt
        if (!archivo.exists()) return; // si el archivo no existe, no hace nada

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            // "try-with-resources": se cierra automáticamente el BufferedReader al terminar.
            String linea;
            while ((linea = br.readLine()) != null) { // lee línea por línea
                String[] partes = linea.split(";"); // separa los campos usando el punto y coma
                if (partes.length == 4) { // valida que tenga los 4 datos esperados
                    String nombre = partes[1];
                    double precio = Double.parseDouble(partes[2]);
                    int stock = Integer.parseInt(partes[3]);
                    // Crea el producto y lo agrega a la lista en memoria
                    Producto p = new Producto(nombre, precio, stock);
                    productos.add(p);
                }
            }
        } catch (IOException e) {
            // Muestra el error si ocurre al leer el archivo
            System.out.println("Error al cargar los productos: " + e.getMessage());
        }
    }
}
