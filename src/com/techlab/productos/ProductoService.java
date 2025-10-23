package com.techlab.productos;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoService {
    private final List<Producto> productos = new ArrayList<>();
    private final String archivoDatos = "productos.txt"; // ruta del archivo en la carpeta del programa

    public ProductoService() {
        cargarDesdeArchivo(); // se ejecuta al crear el servicio
    }

    public Producto agregar(String nombre, double precio, int stock) {
        Producto p = new Producto(nombre, precio, stock);
        productos.add(p);
        guardarEnTxt(); // guarda automáticamente
        return p;
    }

    public List<Producto> listar() {
        return new ArrayList<>(productos);
    }

    public Producto buscarPorId(int id) {
        for (Producto p : productos) if (p.getId() == id) return p;
        return null;
    }

    public Producto buscarPorNombre(String nombre) {
        for (Producto p : productos)
            if (p.getNombre().equalsIgnoreCase(nombre)) return p;
        return null;
    }

    public boolean eliminar(int id) {
        boolean eliminado = productos.removeIf(p -> p.getId() == id);
        if (eliminado) guardarEnTxt(); // guarda si se borra
        return eliminado;
    }

    public boolean actualizar(int id, Double nuevoPrecio, Integer nuevoStock) {
        Producto p = buscarPorId(id);
        if (p == null) return false;
        if (nuevoPrecio != null) p.setPrecio(nuevoPrecio);
        if (nuevoStock != null) p.setStock(nuevoStock);
        guardarEnTxt(); // guarda los cambios
        return true;
    }

    // =====================================================
    // 📦 GUARDAR EN ARCHIVO TXT
    // =====================================================
    private void guardarEnTxt() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoDatos))) {
            for (Producto p : productos) {
                bw.write(p.getId() + ";" + p.getNombre() + ";" + p.getPrecio() + ";" + p.getStock());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("ERROR al guardar productos: " + e.getMessage());
        }
    }

    // =====================================================
    // 📥 CARGAR DESDE ARCHIVO TXT
    // =====================================================
    private void cargarDesdeArchivo() {
        File archivo = new File(archivoDatos);
        if (!archivo.exists()) return; // si no existe, no hace nada

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length == 4) {
                    String nombre = partes[1];
                    double precio = Double.parseDouble(partes[2]);
                    int stock = Integer.parseInt(partes[3]);
                    Producto p = new Producto(nombre, precio, stock);
                    productos.add(p);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar los productos: " + e.getMessage());
        }
    }
}
