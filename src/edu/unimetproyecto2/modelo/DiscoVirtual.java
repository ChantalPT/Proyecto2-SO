/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.unimetproyecto2.modelo;

import java.awt.Color;

/**
 *
 * @author jesus alejandro
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
    
    //Cant. de bloques libres total
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
    
    public boolean asignarArchivo(Archivo archivo) {
        if (getEspacioLibre() < archivo.getTamanoBloques()) {
            return false; // False pn no hay espacio suficiente en el disco
        }

        int bloquesNecesarios = archivo.getTamanoBloques();
        int bloqueAnterior = -1;

        for (int i = 0; i < bloquesNecesarios; i++) {
            int indiceLibre = buscarBloqueLibre();
            Bloque b = bloques[indiceLibre];
            
            //Llenar el bloque con los datos del archivo
            b.setOcupado(true);
            b.setNombreArchivo(archivo.getNombre());
            b.setColorArchivo(archivo.getColor());
            archivo.agregarBloque(indiceLibre); //Guardar en la lista interna del archivo

            if (bloqueAnterior != -1) { //Se encadena al siguiente bloque
                bloques[bloqueAnterior].setSiguienteBloque(indiceLibre);
            }

            bloqueAnterior = indiceLibre; 
        }
        return true; 
    }
    
    public void liberarArchivo(Archivo archivo) {
        //Buscar primer bloque desde la lista del archivo
        if (archivo.getBloquesAsignados().estaVacia()) 
            return;
        
        int indiceActual = archivo.getBloquesAsignados().obtener(0); 
        
        // Recorremos la cadena de bloques en el disco y limpiarlos
        while (indiceActual != -1) {
            Bloque b = bloques[indiceActual];
            int siguiente = b.getSiguienteBloque(); //Guardar su apuntador
            
            // Formatear el bloque
            b.setOcupado(false);
            b.setNombreArchivo("LIBRE");
            b.setColorArchivo(Color.WHITE);
            b.setSiguienteBloque(-1);
            
            indiceActual = siguiente; 
        }
    }
    
    //Getters
    public Bloque getBloque(int id) {
        return bloques[id];
    }
    
    public int getTamanoTotal() {
        return tamanoTotal;
    }
}
