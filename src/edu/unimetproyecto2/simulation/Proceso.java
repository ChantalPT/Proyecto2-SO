/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.unimetproyecto2.simulation;

/**
 *
 * @author jesus alejandro
 */
public class Proceso {
    private int id;
    private String nombreOperacion; // Ejemplo: "Crear archivo1.txt"
    private Estado estadoActual;
    private int bloqueObjetivo;     // Para la planificación de disco (SSTF, SCAN)
    
    // Constructor
    public Proceso(int id, String operacion, int bloqueObjetivo) {
        this.id = id;
        this.nombreOperacion = operacion;
        this.estadoActual = Estado.NUEVO;
        this.bloqueObjetivo = bloqueObjetivo; // Posición del cabezal que requiere
    }

    // Getters y Setters
    public int getId() { 
        return id; 
    }
    
    public Estado getEstadoActual() { 
        return estadoActual; 
    }
    
    public void setEstadoActual(Estado estadoActual) { 
        this.estadoActual = estadoActual; 
    }
    
    public int getBloqueObjetivo() { 
        return bloqueObjetivo; 
    }
    
    public String getNombreOperacion() { 
        return nombreOperacion; 
    }
}
