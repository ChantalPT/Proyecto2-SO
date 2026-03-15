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
public class ManejadorJournaling {
    private ListaEnlazada transacciones;
    
    public ManejadorJournaling() {
        this.transacciones = new ListaEnlazada();
    }
    
    public RegistroJournaling registrar(String operacion, String nombreEntrada) {
        RegistroJournaling nuevaTx = new RegistroJournaling(operacion, nombreEntrada);
        transacciones.insertar(nuevaTx);
        return nuevaTx;
    }
    
    public ListaEnlazada getHistorial() {
        return transacciones;
    }
    
    public ListaEnlazada obtenerPendientes() {
        ListaEnlazada fallidas = new ListaEnlazada();
        int total = transacciones.getTamano();
        
        for (int i = 0; i < total; i++) {
            RegistroJournaling tx = (RegistroJournaling) transacciones.obtener(i);
            if (tx.getEstado().equals("PENDIENTE")) {
                fallidas.insertar(tx);
            }
        }
        return fallidas;
    }
    
}
