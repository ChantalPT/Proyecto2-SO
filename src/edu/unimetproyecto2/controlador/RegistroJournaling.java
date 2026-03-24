/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.unimetproyecto2.controlador;

/**
 *
 * @author pinto
 */
public class RegistroJournaling {
    private static int contadorGlobal = 1;
    private int idTransaccion;
    private String operacion;
    private String nombreEntrada;
    private int tamano; // Cantidad de bloques (ej. 30)
    private String estado;
    
    // Constructor ACTUALIZADO para recibir 3 parámetros
    public RegistroJournaling(String operacion, String nombreEntrada, int tamano) {
        this.idTransaccion = contadorGlobal++;
        this.operacion = operacion;
        this.nombreEntrada = nombreEntrada;
        this.tamano = tamano; // Ahora sí recibe el valor correctamente
        this.estado = "PENDIENTE"; 
    }
    
    public void confirmar() {
        this.estado = "CONFIRMADA";
    }

    // --- Getters y Setters ---
    
    public int getTamano() { 
        return tamano; 
    }
    
    public void setTamano(int tamano) { 
        this.tamano = tamano; 
    }
    
    public static int getContadorGlobal() {
        return contadorGlobal;
    }

    public static void setContadorGlobal(int contadorGlobal) {
        RegistroJournaling.contadorGlobal = contadorGlobal;
    }

    public int getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(int idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public String getNombreEntrada() {
        return nombreEntrada;
    }

    public void setNombreEntrada(String nombreEntrada) {
        this.nombreEntrada = nombreEntrada;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    @Override
    public String toString() {
        return "TX-" + idTransaccion + " | " + operacion + " '" + nombreEntrada + "' | Tam: " + tamano + " | Estado: " + estado;
    }
}