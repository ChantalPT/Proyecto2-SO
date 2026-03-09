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
    private Nodo<T> cabeza; // Inicio, final y tamaño de la cola
    private Nodo<T> cola;
    private int size; 
    
    public Cola() {
        this.cabeza = null;
        this.cola = null;
        this.size = 0;
    }
    
    public boolean isEmpty () { // Confirma que la cola esta vacia
        return cabeza == null;
    }
    
    public void encolar(T dato) { //Encolar. Agrega al final de la cola o si esta
                                  //vacia newNodo se convierte en cabeza y cola
        Nodo<T>  newNodo = new Nodo<>(dato);
        if (isEmpty()) {
            cabeza = newNodo;
            cola = newNodo;
        } else {
            cola.setSiguiente(newNodo);
            cola = newNodo;
        }
        size++;
    }
    
    public T desencolar() {
        if (isEmpty()){
            return null;
        }
        T dato = cabeza.getDato(); // Guarda el dato antes de cambiar head
        cabeza = cabeza.getSiguiente(); //El head.getNext se vuelve la principal
        
        if (cabeza == null) { //Si queda vacia, todo debe ser null (cabeza y cola)
            cola = null;
        }
        
        size--;
        return dato;
    }
    
    public int getSize() {
        return size;
    }
    
    public Nodo<T> getHead() {
        return cabeza;
    }
}


