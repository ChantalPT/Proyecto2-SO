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
    
    
    //Usuarios
    public void cambiarUsuario(String nuevoUsuario) {
        this.usuarioActual = nuevoUsuario;
    }

    //Validador de permisos
    public boolean tienePermiso(Entrada entrada) {
        if (this.usuarioActual.equals("Administrador")) {
            return true;
        }
        return entrada.getDueno().equals(this.usuarioActual);
    }

    //CRUD
    public String crearDirectorio(String nombre) {
        Directorio nuevoDir = new Directorio(nombre, this.usuarioActual, this.directorioActual);
        this.directorioActual.agregarHijo(nuevoDir);
        return "Directorio '" + nombre + "' creado con éxito.";
    }

    //GETTERS
    public DiscoVirtual getDisco() { 
        return disco; 
    }
    
    public Directorio getRaiz() { 
        return raiz; 
    }
    
    public Directorio getDirectorioActual() { 
        return directorioActual; 
    }
    
    public String getUsuarioActual() { 
        return usuarioActual; 
    }

    public void setDisco(DiscoVirtual disco) {
        this.disco = disco;
    }

    public void setRaiz(Directorio raiz) {
        this.raiz = raiz;
    }

    public void setDirectorioActual(Directorio directorioActual) {
        this.directorioActual = directorioActual;
    }

    public void setUsuarioActual(String usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

}
