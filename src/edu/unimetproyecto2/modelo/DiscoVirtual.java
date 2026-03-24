package edu.unimetproyecto2.modelo;

import java.awt.Color;

/**
 * @author jesus alejandro
 * 
 */
public class DiscoVirtual {
    private Bloque[] bloques;
    private int tamanoTotal;

    public DiscoVirtual(int numBloques) {
        this.tamanoTotal = numBloques;
        this.bloques = new Bloque[numBloques];
        
        // Inicializamos cada bloque del disco
        for (int i = 0; i < numBloques; i++) {
            bloques[i] = new Bloque(i);
        }
    }
    
    // --- BOTÓN: CONFIGURAR DISCO ---
    // Este método permite al Gestor resetear el hardware
    public void reconfigurar(int nuevoTamano) {
        this.tamanoTotal = nuevoTamano;
        this.bloques = new Bloque[nuevoTamano];
        for (int i = 0; i < nuevoTamano; i++) {
            bloques[i] = new Bloque(i);
        }
    }

    // Cant. de bloques libres total
    public int getEspacioLibre() {
        int libres = 0;
        for (int i = 0; i < tamanoTotal; i++) {
            if (!bloques[i].isOcupado()) libres++;
        }
        return libres;
    }

    // Buscar el primer bloque libre disponible
    public int buscarBloqueLibre() {
        for (int i = 0; i < tamanoTotal; i++) {
            if (!bloques[i].isOcupado()) {
                return i;
            }
        }
        return -1; // Disco lleno
    }
    
    // --- BOTÓN: CREAR ARCHIVO ---
    public boolean asignarArchivo(Archivo archivo) {
        // Validación de espacio
        if (getEspacioLibre() < archivo.getTamanoBloques()) {
            return false; 
        }

        int bloquesNecesarios = archivo.getTamanoBloques();
        int bloqueAnteriorIdx = -1;

        for (int i = 0; i < bloquesNecesarios; i++) {
            int indiceLibre = buscarBloqueLibre();
            
            // Si es el primer bloque, se lo asignamos al archivo como su dirección de inicio
            if (i == 0) {
                archivo.setBloqueInicial(indiceLibre);
            }

            Bloque b = bloques[indiceLibre];
            
            // Marcamos el bloque como ocupado y le damos identidad
            b.setOcupado(true);
            b.setNombreArchivo(archivo.getNombre());
            b.setColorArchivo(archivo.getColor());
            
            // Guardamos el índice en la lista interna del archivo (Fase 1)
            archivo.agregarBloque(indiceLibre); 

            // Creamos el enlace (Puntero) entre bloques
            if (bloqueAnteriorIdx != -1) { 
                bloques[bloqueAnteriorIdx].setSiguienteBloque(indiceLibre);
            }

            bloqueAnteriorIdx = indiceLibre; 
        }
        
        // El último bloque de la cadena apunta a -1 (Fin de archivo)
        if (bloqueAnteriorIdx != -1) {
            bloques[bloqueAnteriorIdx].setSiguienteBloque(-1);
        }
        
        return true; 
    }
    
    // --- BOTÓN: ELIMINAR ---
    public void liberarArchivo(Archivo archivo) {
        // Obtenemos el inicio de la cadena de bloques
        int indiceActual = archivo.getBloqueInicial(); 
        
        // Recorremos y limpiamos físicamente el disco
        while (indiceActual != -1 && indiceActual < tamanoTotal) {
            Bloque b = bloques[indiceActual];
            int siguiente = b.getSiguienteBloque(); // Guardamos a dónde iba antes de borrar
            
            // Formatear el bloque (Reset)
            b.setOcupado(false);
            b.setNombreArchivo("LIBRE");
            b.setColorArchivo(Color.WHITE);
            b.setSiguienteBloque(-1);
            
            indiceActual = siguiente; 
        }
        
        // Limpiamos la lista de índices del objeto archivo
        archivo.getBloquesAsignados().vaciar();
    }
    
    // Getters y Setters necesarios para la GUI y el Simulador
    public Bloque[] getBloques() {
        return bloques;
    }

    public Bloque getBloque(int id) {
        if (id >= 0 && id < tamanoTotal) {
            return bloques[id];
        }
        return null;
    }
    
    public int getTamanoTotal() {
        return tamanoTotal;
    }
}