/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.unimetproyecto2.controlador;

import edu.unimetproyecto2.modelo.Archivo;
import edu.unimetproyecto2.modelo.Directorio;
import edu.unimetproyecto2.modelo.DiscoVirtual;
import edu.unimetproyecto2.modelo.Entrada;
import java.awt.Color;

/**
 *
 * @author pinto
 */
public class GestorAlmacenamiento {
    private DiscoVirtual disco;
    private Directorio raiz;
    private Directorio directorioActual;
    private String usuarioActual; 

    public GestorAlmacenamiento(int tamanoDisco) {
        this.disco = new DiscoVirtual(tamanoDisco);
        // La raíz siempre le pertenece al Admin y no tiene padre (null)
        this.raiz = new Directorio("Raiz", "Administrador", null); 
        this.directorioActual = this.raiz;
        this.usuarioActual = "Administrador"; //Admin por defecto
    }

    //GETTERS
    public DiscoVirtual getDisco() { return disco; }
    public Directorio getRaiz() { return raiz; }
    public Directorio getDirectorioActual() { return directorioActual; }
    public String getUsuarioActual() { return usuarioActual; }

}
