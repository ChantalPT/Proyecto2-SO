/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.unimetproyecto2.modelo;

/**
 *
 * @author jesus alejandro
 */
public abstract class Entrada {
    protected String nombre;
    protected String dueno;
    protected boolean esDirectorio;
    protected Entrada padre; // Para saber en qué carpeta está metida

    public Entrada(String nombre, String dueno, boolean esDirectorio, Entrada padre) {
        this.nombre = nombre;
        this.dueno = dueno;
        this.esDirectorio = esDirectorio;
        this.padre = padre;
    }

    // Getters y Setters básicos
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDueno() { return dueno; }
    public boolean isEsDirectorio() { return esDirectorio; }
    public Entrada getPadre() { return padre; }
}
