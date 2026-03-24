/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.unimetproyecto2.modelo;

/**
 *
 * @author jesus alejandro
 */

import java.awt.Color;

public class Bloque {
    private int id;
    private boolean ocupado;
    private String nombreArchivo; 
    private int siguienteBloque;  
    private Color colorArchivo;   

    public Bloque(int id) {
        this.id = id;
        this.ocupado = false;
        this.nombreArchivo = "LIBRE";
        this.siguienteBloque = -1; 
        this.colorArchivo = Color.WHITE; 
    }

    
    // Getters (Para leer los datos)

    public int getId() {
        return id;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public int getSiguienteBloque() {
        return siguienteBloque;
    }

    public Color getColorArchivo() {
        return colorArchivo;
    }

    
    // Setters (Para modificar los datos)

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public void setSiguienteBloque(int siguienteBloque) {
        this.siguienteBloque = siguienteBloque;
    }

    public void setColorArchivo(Color colorArchivo) {
        this.colorArchivo = colorArchivo;
    }
}
