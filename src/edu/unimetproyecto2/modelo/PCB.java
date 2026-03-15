/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.unimetproyecto2.modelo;

/**
 *
 * @author pinto
 */
public class PCB {
    private static int contadorPID = 1;
    private int pid; //process id
    private String usuario;
    private String operacion; //CRUD
    private Entrada objetivo; //el archivo o carpeta
    private String estado;
    private Object[] parametros; //Guardar nombres nuevos, tamaños, colores, etc
    
    public PCB(String usuario, String operacion, Entrada objetivo) {
        this.pid = contadorPID++;
        this.usuario = usuario;
        this.operacion = operacion;
        this.objetivo = objetivo;
        this.parametros = parametros;
        this.estado = "LISTO"; //Siempre inicia en listo.
    }

    public static int getContadorPID() {
        return contadorPID;
    }

    public static void setContadorPID(int contadorPID) {
        PCB.contadorPID = contadorPID;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public Entrada getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(Entrada objetivo) {
        this.objetivo = objetivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Object[] getParametros() {
        return parametros;
    }

    public void setParametros(Object[] parametros) {
        this.parametros = parametros;
    }
    
    
    
    @Override
    public String toString() {
        return "PCB [PID=" + pid + ", Usuario=" + usuario + ", Op=" + operacion + ", Estado=" + estado + "]";
    }
    
}
