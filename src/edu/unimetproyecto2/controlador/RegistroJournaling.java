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
    private String estado;
    
    public RegistroJournaling(String operacion, String nombreEntrada) {
        this.idTransaccion = contadorGlobal++;
        this.operacion = operacion;
        this.nombreEntrada = nombreEntrada;
        this.estado = "PENDIENTE"; 
    }
    
    public void confirmar() {
        this.estado = "CONFIRMADA";
    }

    //Getters y Setters
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
        return "TX-" + idTransaccion + " | " + operacion + " '" + nombreEntrada + "' | Estado: " + estado;
    }
    
}
