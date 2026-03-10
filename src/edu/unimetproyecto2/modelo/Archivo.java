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

public class Archivo extends Entrada {
    private int tamanoBloques;
    private int bloqueInicial;
    private Color color;

    public Archivo(String nombre, String dueno, int tamano, int bloqueInicial, Color color, Entrada padre) {
        super(nombre, dueno, false, padre); // false porque no es directorio
        this.tamanoBloques = tamano;
        this.bloqueInicial = bloqueInicial;
        this.color = color;
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
}
