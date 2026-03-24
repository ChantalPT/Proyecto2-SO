package edu.unimetproyecto2.controlador;

import edu.unimetproyecto2.estructuras.ListaEnlazada;

/**
 * @author pinto
 */
public class PlanificadorDisco {
    private int tamanoDisco;

    public PlanificadorDisco(int posicionInicial, int tamanoDisco) {
        this.tamanoDisco = tamanoDisco;
    }
    
    public class Resultado {
        public ListaEnlazada<Integer> secuenciaAtencion; 
        public int desplazamientoTotal;

        public Resultado(ListaEnlazada<Integer> secuencia, int desplazamiento) {
            this.secuenciaAtencion = secuencia;
            this.desplazamientoTotal = desplazamiento;
        }
    }

    // --- MÉTODOS DE APOYO (ORDENAMIENTO EFICIENTE) ---
    
    private void quickSort(int[] arr, int bajo, int alto) {
        if (bajo < alto) {
            int pi = particion(arr, bajo, alto);
            quickSort(arr, bajo, pi - 1);
            quickSort(arr, pi + 1, alto);
        }
    }

    private int particion(int[] arr, int bajo, int alto) {
        int pivote = arr[alto];
        int i = (bajo - 1);
        for (int j = bajo; j < alto; j++) {
            if (arr[j] < pivote) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[alto];
        arr[alto] = temp;
        return i + 1;
    }

    // --- ALGORITMOS ---

    public Resultado ejecutarFIFO(ListaEnlazada<Integer> solicitudes) {
        ListaEnlazada<Integer> secuencia = new ListaEnlazada<>();
        int desplazamiento = 0;
        int actual = 0; 

        for (int i = 0; i < solicitudes.getTamano(); i++) {
            int solicitud = solicitudes.obtener(i);  
            secuencia.insertar(solicitud);
            desplazamiento += Math.abs(solicitud - actual); 
            actual = solicitud;
        }
        return new Resultado(secuencia, desplazamiento);
    }

    public Resultado ejecutarSSTF(ListaEnlazada<Integer> solicitudes, int posicionActual) {
        ListaEnlazada<Integer> pendientes = new ListaEnlazada<>();
        for(int i = 0; i < solicitudes.getTamano(); i++) {
            pendientes.insertar(solicitudes.obtener(i));
        }

        ListaEnlazada<Integer> secuencia = new ListaEnlazada<>();
        int desplazamiento = 0;
        int actual = posicionActual;

        while (!pendientes.estaVacia()) { 
            int indiceMasCercano = 0;
            int distanciaMinima = Integer.MAX_VALUE;

            for (int i = 0; i < pendientes.getTamano(); i++) {
                int distancia = Math.abs(pendientes.obtener(i) - actual);
                if (distancia < distanciaMinima) {
                    distanciaMinima = distancia;
                    indiceMasCercano = i;
                }
            }

            Integer elegido = pendientes.obtener(indiceMasCercano);
            pendientes.remover(elegido);
            secuencia.insertar(elegido);
            desplazamiento += distanciaMinima;
            actual = elegido;
        }
        return new Resultado(secuencia, desplazamiento);
    }

    public Resultado ejecutarSCAN(ListaEnlazada<Integer> solicitudes, int posicionActual, boolean haciaArriba) {
        ListaEnlazada<Integer> secuencia = new ListaEnlazada<>();
        int desplazamiento = 0;
        int actual = posicionActual;
        int n = solicitudes.getTamano();

        if (n == 0) return new Resultado(secuencia, 0);

        // Pasamos a arreglo para usar QuickSort
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = solicitudes.obtener(i);
        }

        // --- ORDENAMIENTO QUICK SORT (O(n log n)) ---
        quickSort(arr, 0, n - 1);

        ListaEnlazada<Integer> mayores = new ListaEnlazada<>();
        ListaEnlazada<Integer> menores = new ListaEnlazada<>();
        
        for (int valor : arr) {
            if (valor >= actual) mayores.insertar(valor);
            else menores.insertar(valor);
        }

        if (haciaArriba) {
            // Sube atendiendo los mayores
            for (int i = 0; i < mayores.getTamano(); i++) {
                int bloque = mayores.obtener(i);
                secuencia.insertar(bloque);
                desplazamiento += Math.abs(bloque - actual);
                actual = bloque;
            }
            // Al llegar al tope (o al último mayor), baja por los menores (en orden descendente)
            for (int i = menores.getTamano() - 1; i >= 0; i--) {
                int bloque = menores.obtener(i);
                secuencia.insertar(bloque);
                desplazamiento += Math.abs(bloque - actual);
                actual = bloque;
            }
        } else {
            // Lógica inversa si empezara bajando...
            for (int i = menores.getTamano() - 1; i >= 0; i--) {
                int bloque = menores.obtener(i);
                secuencia.insertar(bloque);
                desplazamiento += Math.abs(bloque - actual);
                actual = bloque;
            }
            for (int i = 0; i < mayores.getTamano(); i++) {
                int bloque = mayores.obtener(i);
                secuencia.insertar(bloque);
                desplazamiento += Math.abs(bloque - actual);
                actual = bloque;
            }
        }

        return new Resultado(secuencia, desplazamiento);
    }
}