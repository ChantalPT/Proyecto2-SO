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

public class Directorio extends Entrada {
    private ListaEnlazada<Entrada> hijos;

    public Directorio(String nombre, String dueno, Entrada padre) {
        super(nombre, dueno, true, padre); // true porque ES directorio
        this.hijos = new ListaEnlazada<>();
    }

    public void agregarHijo(Entrada nuevaEntrada) {
        this.hijos.insertar(nuevaEntrada);
    }
    
    public void removerHijo(Entrada entradaAEliminar) { //Para CRUD
        this.hijos.remover(entradaAEliminar);
    }

    public ListaEnlazada<Entrada> getHijos() {
        return hijos;
    }
}
