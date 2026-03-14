/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.unimetproyecto2.estructuras;

/**
 *
 * @author jesus alejandro
 * @param <T>
 */
public class ListaEnlazada<T> {
    private Nodo<T> cabeza;
    private Nodo<T> ultimo;
    private int tamano;
    

    public ListaEnlazada() {
        this.cabeza = null;
        this.ultimo = null;
        this.tamano = 0;
    }
    
    public boolean estaVacia() {
        return cabeza == null;
    }

    // Método para insertar al final (útil para la cola de procesos)
    public void insertar(T dato) {
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        if (estaVacia()) {
            cabeza = nuevoNodo;
            ultimo = nuevoNodo;
        } else {
            ultimo.setSiguiente(nuevoNodo);
            ultimo = nuevoNodo; 
        }
        tamano++;
    }
    
    public void remover(T dato) {
        if (estaVacia()) return;

        if (cabeza.getDato().equals(dato)) {
            cabeza = cabeza.getSiguiente();
            tamano--;
            if (cabeza == null) {
                ultimo = null;
            }
            return;
        }

        Nodo<T> actual = cabeza;
        while (actual.getSiguiente() != null && !actual.getSiguiente().getDato().equals(dato)) {
            actual = actual.getSiguiente();
        }

        if (actual.getSiguiente() != null) {
            if (actual.getSiguiente() == ultimo) {
                ultimo = actual;
            }
            actual.setSiguiente(actual.getSiguiente().getSiguiente());
            tamano--;
        }
    }

    public int getTamano() { return tamano; }
    
    public T obtener(int indice) {
        if (indice < 0 || indice >= tamano) return null;
        Nodo<T> temporal = cabeza;
        for (int i = 0; i < indice; i++) {
            temporal = temporal.getSiguiente();
        }
        return temporal.getDato();
    }
    
    
}
