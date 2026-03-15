/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.unimetproyecto2.controlador;

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
}
