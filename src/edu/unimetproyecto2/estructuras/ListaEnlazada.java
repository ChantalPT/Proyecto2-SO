package edu.unimetproyecto2.estructuras;

/**
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

    /**
     * MÉTODO CLAVE: Soluciona el error en DiscoVirtual
     * Limpia la lista por completo reseteando los punteros.
     */
    public void vaciar() {
        this.cabeza = null;
        this.ultimo = null;
        this.tamano = 0;
    }

    // Método para insertar al final (útil para la cola de procesos)
    public void insertar(T dato) {
        if (dato == null) return;
        
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

    // Método para remover un dato específico
    public void remover(T dato) {
        if (estaVacia() || dato == null) return;

        // Caso 1: El dato está en la cabeza
        if (cabeza.getDato().equals(dato)) {
            cabeza = cabeza.getSiguiente();
            tamano--;
            if (cabeza == null) {
                ultimo = null;
            }
            return;
        }

        // Caso 2: El dato está en medio o al final
        Nodo<T> actual = cabeza;
        while (actual.getSiguiente() != null && !actual.getSiguiente().getDato().equals(dato)) {
            actual = actual.getSiguiente();
        }

        // Si lo encontramos
        if (actual.getSiguiente() != null) {
            // Si el que vamos a borrar es el último, actualizamos el puntero 'ultimo'
            if (actual.getSiguiente() == ultimo) {
                ultimo = actual;
            }
            actual.setSiguiente(actual.getSiguiente().getSiguiente());
            tamano--;
        }
    }

    public int getTamano() { 
        return tamano; 
    }

    public T obtener(int indice) {
        if (indice < 0 || indice >= tamano) return null;
        
        Nodo<T> temporal = cabeza;
        for (int i = 0; i < indice; i++) {
            temporal = temporal.getSiguiente();
        }
        return temporal.getDato();
    }
}