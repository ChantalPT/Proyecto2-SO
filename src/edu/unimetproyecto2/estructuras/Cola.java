/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.unimetproyecto2.estructuras;

/**
 *
 * @author pinto
 * @param <T> tipo de dato que se guarda.
 */

public class Cola<T> {
    private Nodo<T> frente; // Inicio de la cola, el que sera tendido
    private Nodo<T> ultimo; // Final de la cola, el ultimo que llegó
    private int tamano; // Tamaño de la cola
    
    public Cola() {
        this.frente = null;
        this.ultimo = null;
        this.tamano = 0;
    }
    
    
    public void encolar(T dato) { //Encolar. Agrega al final de la cola o si esta
                                  //vacia newNodo se convierte en cabeza y cola
        Nodo<T>  nuevoNodo = new Nodo<>(dato);
        if (estaVacia()) {
            frente = nuevoNodo;
            ultimo = nuevoNodo;
        } else {
            ultimo.setSiguiente(nuevoNodo);
            ultimo = nuevoNodo;
        }
        tamano++;
    }
    
    public boolean estaVacia() { // Confirma que la cola esta vacia
    return frente == null;
    }
    
    public T desencolar() {
        if (estaVacia()){
            return null;
        }
        T dato = frente.getDato(); // Guarda el dato antes de cambiar head
        frente = frente.getSiguiente(); //El head.getNext se vuelve la principal
        
        if (frente == null) { //Si queda vacia, todo debe ser null (cabeza y cola)
            ultimo = null;
        }
        
        tamano--;
        return dato;
    }
    
    public T verFrente() {
    if (estaVacia()) 
        return null;
    return frente.getDato();
    }
    
    public T obtener(int indice) {
        if (estaVacia() || indice < 0 || indice >= tamano) {
            return null;
        }

        // Empezamos desde el frente
        Nodo<T> temporal = frente;
        int contador = 0;

        // Caminamos por los nodos hasta llegar a la posición deseada
        while (temporal != null) {
            if (contador == indice) {
                return temporal.getDato();
            }
            temporal = temporal.getSiguiente();
            contador++;
        }

        return null;
    }
    
    public int getTamano() {
        return tamano;
    }
}


