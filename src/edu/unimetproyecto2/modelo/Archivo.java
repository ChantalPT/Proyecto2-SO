/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.unimetproyecto2.modelo;

/**
 *
 * @author jesus alejandro
 */
import edu.unimetproyecto2.estructuras.ListaEnlazada;
import java.awt.Color;

public class Archivo extends Entrada {
    private int tamanoBloques;
    private int bloqueInicial;
    private Color color;
    private ListaEnlazada<Integer> bloquesAsignados;
    private int lectoresActivos; // lecturas simultaneas
    private boolean bloqueadoEscritura;

    public Archivo(String nombre, String dueno, int tamano, int bloqueInicial, Color color, Entrada padre) {
        super(nombre, dueno, false, padre); // false porque no es directorio
        this.tamanoBloques = tamano;
        this.bloqueInicial = bloqueInicial;
        this.color = color;
        this.bloquesAsignados = new ListaEnlazada<>();
        this.lectoresActivos = 0;
        this.bloqueadoEscritura = false;
    }
    
    public void agregarBloque(int index) {
        this.bloquesAsignados.insertar(index);
    }
    
    public ListaEnlazada<Integer> getBloquesAsignados() {
        return bloquesAsignados;
    }
    
    public boolean isBloqueadoEscritura() { 
        return bloqueadoEscritura; 
    }
    
    public void setBloqueadoEscritura(boolean bloqueadoEscritura) { 
        this.bloqueadoEscritura = bloqueadoEscritura; 
    }
    
    public int getLectoresActivos() { 
        return lectoresActivos; 
    }
    
    public void agregarLector() { 
        this.lectoresActivos++; 
    }
    
    public void removerLector() { 
        if(this.lectoresActivos > 0) 
            this.lectoresActivos--; 
    }

    // Getters y Setters específicos
    public int getTamanoBloques() { 
        return tamanoBloques; 
    }
    
    public int getBloqueInicial() { 
        return bloqueInicial; 
    }
    
    public Color getColor() { 
        return color; 
    }
    
    public void setBloqueInicial(int bloqueInicial) {
    this.bloqueInicial = bloqueInicial;
}
}
