/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.unimetproyecto2.estructuras;

/**
 *
 * @author jesus alejandro
 */
public class ListaEnlazada<T> {
    private Nodo<T> cabeza;
    private int tamano;

    public ListaEnlazada() {
        this.cabeza = null;
        this.tamano = 0;
    }

    // Método para insertar al final (útil para la cola de procesos)
    public void insertar(T dato) {
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        if (cabeza == null) {
            cabeza = nuevoNodo;
        } else {
            Nodo<T> temporal = cabeza;
            while (temporal.getSiguiente() != null) {
                temporal = temporal.getSiguiente();
            }
            temporal.setSiguiente(nuevoNodo);
        }
        tamano++;
    }

    public int getTamano() { return tamano; }
    
    // Método para obtener un elemento por índice (para el JTable)
    public T obtener(int indice) {
        if (indice < 0 || indice >= tamano) return null;
        Nodo<T> temporal = cabeza;
        for (int i = 0; i < indice; i++) {
            temporal = temporal.getSiguiente();
        }
        return temporal.getDato();
    }
}
