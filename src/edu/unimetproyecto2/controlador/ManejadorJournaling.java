package edu.unimetproyecto2.controlador;

import edu.unimetproyecto2.estructuras.ListaEnlazada;
import java.io.*;

public class ManejadorJournaling {
    private ListaEnlazada transacciones;
    private final String RUTA_ARCHIVO = "journal.txt"; 
    
    public ManejadorJournaling() {
        this.transacciones = new ListaEnlazada();
        cargarDesdeArchivo(); 
    }
    
    // Registrar una nueva operación con TAMAÑO incluido
    public RegistroJournaling registrar(String operacion, String nombre, int tamano) {
        RegistroJournaling nuevaTX = new RegistroJournaling(operacion, nombre, tamano);
        transacciones.insertar(nuevaTX);
        actualizarArchivo(); 
        return nuevaTX;
    }
    
    // Guardar una transacción individual (si se usa por separado)
    private void guardarEnArchivo(RegistroJournaling tx) {
        try (PrintWriter out = new PrintWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            // Guardamos: OPERACION, NOMBRE, TAMANO, ESTADO
            out.println(tx.getOperacion() + "," + tx.getNombreEntrada() + "," + tx.getTamano() + "," + tx.getEstado());
        } catch (IOException e) {
            System.err.println("Error al escribir en el Journal: " + e.getMessage());
        }
    }

    // Leer el archivo al abrir el programa para recuperar fallos
    private void cargarDesdeArchivo() {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                // Ahora verificamos que tenga 4 partes (incluyendo el tamaño)
                if (partes.length == 4) {
                    int tam = Integer.parseInt(partes[2]); // Recuperamos el 30
                    RegistroJournaling tx = new RegistroJournaling(partes[0], partes[1], tam);
                    tx.setEstado(partes[3]); 
                    transacciones.insertar(tx);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar el Journal: " + e.getMessage());
        }
    }

    // Reescribe el archivo completo con los estados actuales
    public void actualizarArchivo() {
        // 'false' para que limpie el archivo y escriba todo de nuevo
        try (PrintWriter out = new PrintWriter(new FileWriter(RUTA_ARCHIVO, false))) {
            for (int i = 0; i < transacciones.getTamano(); i++) {
                RegistroJournaling tx = (RegistroJournaling) transacciones.obtener(i);
                // Usamos 'out' que es el nombre de nuestro PrintWriter
                out.println(tx.getOperacion() + "," + tx.getNombreEntrada() + "," + tx.getTamano() + "," + tx.getEstado());
            }
        } catch (IOException e) {
            System.err.println("Error al actualizar el Journal: " + e.getMessage());
        }
    }

    // Obtener solo los que no se terminaron (PENDIENTE)
    public ListaEnlazada obtenerPendientes() {
        ListaEnlazada fallidas = new ListaEnlazada();
        for (int i = 0; i < transacciones.getTamano(); i++) {
            RegistroJournaling tx = (RegistroJournaling) transacciones.obtener(i);
            if (tx.getEstado().equals("PENDIENTE")) {
                fallidas.insertar(tx);
            }
        }
        return fallidas;
    }
}