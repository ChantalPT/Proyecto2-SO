/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.unimetproyecto2.estructuras;

/**
 *
 * @author pinto
 */
public class Nodos <T>{
    private T dato;  // contenido del nodo
    private Nodos<T> siguiente; // apuntador al siguiente nodo
    
    public Nodos(T dato) {
        this.dato = dato;
        this.siguiente = null;
    }
    
    public T getDato(){
        return dato;
    }
    public void setDato(T dato) {
        this.dato = dato;
    }

    public Nodos<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodos<T> siguiente) {
        this.siguiente = siguiente;
    }
    
    
}


