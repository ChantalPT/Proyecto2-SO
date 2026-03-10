/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.unimetproyecto2.modelo;

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

    // Buscar el primer bloque libre disponible
    public int buscarBloqueLibre() {
        for (int i = 0; i < tamanoTotal; i++) {
            if (!bloques[i].isOcupado()) {
                return i;
            }
        }
        return -1; // Disco lleno
    }
    
    public Bloque getBloque(int id) {
        return bloques[id];
    }
}
