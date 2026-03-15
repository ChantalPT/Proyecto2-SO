/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.unimetproyecto2.controlador;

import edu.unimetproyecto2.estructuras.ListaEnlazada;

/**
 *
 * @author pinto
 */
public class PlanificadorDisco {
    private int posicionCabezal;
    private int tamanoDisco;

    public PlanificadorDisco(int posicionInicial, int tamanoDisco) {
        this.posicionCabezal = posicionInicial;
        this.tamanoDisco = tamanoDisco;
    }
    
    public void setPosicionCabezal(int posicionCabezal) {
        this.posicionCabezal = posicionCabezal;
    }
    
    public class Resultado {
        public ListaEnlazada secuenciaAtencion; // Usamos tu lista
        public int desplazamientoTotal;

        public Resultado(ListaEnlazada secuencia, int desplazamiento) {
            this.secuenciaAtencion = secuencia;
            this.desplazamientoTotal = desplazamiento;
        }
    }
    
    //FIFO
    public Resultado ejecutarFIFO(ListaEnlazada solicitudes) {
        ListaEnlazada secuencia = new ListaEnlazada();
        int desplazamiento = 0;
        int actual = this.posicionCabezal;

        int cantidad = solicitudes.getTamano();
        for (int i = 0; i < cantidad; i++) {
            int solicitud = (int) solicitudes.obtener(i);  
            secuencia.insertar(solicitud);
            desplazamiento += Math.abs(solicitud - actual); 
            actual = solicitud;
        }
        
        return new Resultado(secuencia, desplazamiento);
    }
    
    //Shortest Seek Time Fisrt
    public Resultado ejecutarSSTF(ListaEnlazada solicitudes) {
        ListaEnlazada pendientes = new ListaEnlazada();
        int cantidadOriginal = solicitudes.getTamano();
        for(int i = 0; i < cantidadOriginal; i++) {
            pendientes.insertar(solicitudes.obtener(i));
        }

        ListaEnlazada secuencia = new ListaEnlazada();
        int desplazamiento = 0;
        int actual = this.posicionCabezal;

        while (!pendientes.estaVacia()) { 
            int indiceMasCercano = 0;
            int distanciaMinima = Math.abs((int)pendientes.obtener(0) - actual);

            for (int i = 1; i < pendientes.getTamano(); i++) {
                int distancia = Math.abs((int)pendientes.obtener(i) - actual);
                if (distancia < distanciaMinima) {
                    distanciaMinima = distancia;
                    indiceMasCercano = i;
                }
            }

            //Se anota el elegido
            int siguiente = (int) pendientes.obtener(indiceMasCercano);
            pendientes.remover(indiceMasCercano);
            secuencia.insertar(siguiente);
            desplazamiento += distanciaMinima;
            actual = siguiente;
        }

        return new Resultado(secuencia, desplazamiento);
    }
    
    //Algoritmo del ascensor
    public Resultado ejecutarSCAN(ListaEnlazada solicitudes) {
        ListaEnlazada secuencia = new ListaEnlazada();
        int desplazamiento = 0;
        int actual = this.posicionCabezal;
        int n = solicitudes.getTamano();

        if (n == 0) return new Resultado(secuencia, 0);

        //Se convierte la lista en una arreglo para ordenarlo facil
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = (int) solicitudes.obtener(i);
        }

        //Ordenar de menor a mayor
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }

        //Se separa en los que están más adelante y los que están atrás
        ListaEnlazada mayores = new ListaEnlazada();
        ListaEnlazada menores = new ListaEnlazada();
        for (int i = 0; i < n; i++) {
            if (arr[i] >= actual) {
                mayores.insertar(arr[i]);
            } else {
                menores.insertar(arr[i]); 
            }
        }

        //Recorrer par arriba
        for (int i = 0; i < mayores.getTamano(); i++) {
            int bloque = (int) mayores.obtener(i);
            secuencia.insertar(bloque);
            desplazamiento += Math.abs(bloque - actual);
            actual = bloque;
        }

        //Si quedaron menores, bajar nuevamente
        if (!menores.estaVacia()) {
            int limiteDisco = this.tamanoDisco - 1; //Ultimo bloque del disco
            
            if (actual != limiteDisco) {
                desplazamiento += Math.abs(limiteDisco - actual);
                actual = limiteDisco;
            }

            //Leer la lista menores de atrás para adelantez
            for (int i = menores.getTamano() - 1; i >= 0; i--) {
                int bloque = (int) menores.obtener(i);
                secuencia.insertar(bloque);
                desplazamiento += Math.abs(bloque - actual);
                actual = bloque;
            }
        }

        return new Resultado(secuencia, desplazamiento);
    }
}
