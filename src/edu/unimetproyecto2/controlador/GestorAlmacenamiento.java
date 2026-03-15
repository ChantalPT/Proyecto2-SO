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
    
    //Logica CRUD
    public String crearArchivo(String nombre, int tamanoBloques, Color color) {
        Archivo nuevoArchivo = new Archivo(nombre, this.usuarioActual, tamanoBloques, -1, color, this.directorioActual);  
        boolean guardadoExitoso = this.disco.asignarArchivo(nuevoArchivo);
        
        if (guardadoExitoso) {
            this.directorioActual.agregarHijo(nuevoArchivo);
            return "Archivo '" + nombre + "' guardado correctamente.";
        } else {
            return "ERROR:\n No hay espacio suficiente en el disco para " + tamanoBloques + " bloques.";
        }
    }
    
    public String leerArchivo(Entrada entrada) {
        if (entrada.isEsDirectorio()) { //No se lee la carpeta
            return "ERROR:\n '" + entrada.getNombre() + "' es un directorio. Usa la navegación para entrar.";
        }
        
        if (!tienePermiso(entrada)) {
            return "ERROR:\n Acceso denegado. No tienes permiso para leer el archivo '" + entrada.getNombre() + "'.";
        }

        Archivo archivo = (Archivo) entrada;
        return "LEYENDO ARCHIVO \n" +
               "Nombre: " + archivo.getNombre() + "\n" +
               "Dueño: " + archivo.getDueno() + "\n" +
               "Tamaño: " + archivo.getTamanoBloques() + " bloques en el disco.\n" +
               "Lectura exitosa.";
    }
    
    public String modificarNombre(Entrada entrada, String nuevoNombre) {
        if (!tienePermiso(entrada)) {
            return "ERROR:\n Permiso denegado. Solo el dueño o el Administrador pueden modificar.";
        }
        String nombreViejo = entrada.getNombre();
        entrada.setNombre(nuevoNombre);
        return "Éxito: '" + nombreViejo + "' ha sido renombrado a '" + nuevoNombre + "'.";
    }
    
    public String eliminarEntrada(Entrada entrada) {
        if (entrada == raiz) {
            return "ERROR:\n Sistema protegido. No puedes eliminar la carpeta Raíz.";
        }
        if (!tienePermiso(entrada)) {
            return "ERROR:\n Permiso denegado para eliminar '" + entrada.getNombre() + "'.";
        }

        // Si es archivo se liberan sus bloquees en el disco
        if (!entrada.isEsDirectorio()) {
            disco.liberarArchivo((Archivo) entrada);
        } else {
            // Si es directorio se borra todo su contenido primero 
            vaciarDirectorio((Directorio) entrada);
        }

        //Desconectar de la carpeta padre
        Directorio padre = (Directorio) entrada.getPadre();
        if (padre != null) {
            padre.removerHijo(entrada);
        }

        return "Éxito:\n '" + entrada.getNombre() + "' eliminado del sistema y bloques liberados.";
    }
    
    //Para borrar todo lo interno a la carpeta y libera los bloques
    private void vaciarDirectorio(Directorio dir) {
        while (!dir.getHijos().estaVacia()) {
            Entrada hijo = dir.getHijos().obtener(0);
            
            if (!hijo.isEsDirectorio()) {
                disco.liberarArchivo((Archivo) hijo);
            } else {
                vaciarDirectorio((Directorio) hijo); //Llamada recursiva
            }
            dir.removerHijo(hijo);
        }
    }

    //Getters
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
