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
    public boolean tienePermiso(String operacion) {
        if (this.usuarioActual.equals("Administrador")) {
            return true;
        }
        return operacion.equals("LEER");
    }

    //CRUD
    public String crearDirectorio(String nombre) {
        if (!tienePermiso("CREAR")) {
            return "ERROR: Acceso denegado. Solo el Administrador puede crear directorios.";
        }
        Directorio nuevoDir = new Directorio(nombre, this.usuarioActual, this.directorioActual);
        this.directorioActual.agregarHijo(nuevoDir);
        return "Directorio '" + nombre + "' creado con éxito.";
    }
    
    //Logica CRUD
    public String crearArchivo(String nombre, int tamanoBloques, Color color) {
        if (!tienePermiso("CREAR")) {
            return "ERROR: Acceso denegado. Solo el Administrador puede crear archivos.";
        }
        
        // Creamos el archivo y le pedimos al disco que intente guardarlo
        Archivo nuevoArchivo = new Archivo(nombre, this.usuarioActual, tamanoBloques, -1, color, this.directorioActual);
        boolean guardadoExitoso = this.disco.asignarArchivo(nuevoArchivo);
        
        if (guardadoExitoso) {
            this.directorioActual.agregarHijo(nuevoArchivo);
            return "Archivo '" + nombre + "' guardado correctamente.";
        } else {
            return "ERROR: No hay espacio suficiente en el disco para " + tamanoBloques + " bloques.";
        }
    }
    
    public String leerArchivo(Entrada entrada) {
        if (entrada.isEsDirectorio()) { //No se lee la carpeta
            return "ERROR:\n '" + entrada.getNombre() + "' es un directorio. Usa la navegación para entrar.";
        }
        
        if (!tienePermiso("LEER")) {
            return "ERROR: Acceso denegado. No tienes permiso para leer.";
        }

        Archivo archivo = (Archivo) entrada;
        return "LEYENDO ARCHIVO \n" +
               "Nombre: " + archivo.getNombre() + "\n" +
               "Dueño: " + archivo.getDueno() + "\n" +
               "Tamaño: " + archivo.getTamanoBloques() + " bloques en el disco.\n" +
               "Lectura exitosa.";
    }
    
    public String modificarNombre(Entrada entrada, String nuevoNombre) {
        if (!tienePermiso("MODIFICAR")) {
            return "ERROR:\n Permiso denegado. Solo el Administrador puede modificar.";
        }
        String nombreViejo = entrada.getNombre();
        entrada.setNombre(nuevoNombre);
        return "Éxito: '" + nombreViejo + "' ha sido renombrado a '" + nuevoNombre + "'.";
    }
    
    public String eliminarEntrada(Entrada entrada) {
        if (entrada == raiz) {
            return "ERROR:\n Sistema protegido. No puedes eliminar la carpeta Raíz.";
        }
        if (!tienePermiso("ELIMINAR")) {
            return "ERROR:\n Permiso denegado. Solo el Administrador puede eliminar archivos o directorios.";
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
    
    //CD
    //Entrar a una carpeta
    public String entrarDirectorio(String nombreCarpeta) { 
        int cantidadHijos = directorioActual.getHijos().getTamano(); 
        for (int i = 0; i < cantidadHijos; i++) {
            Entrada hijo = directorioActual.getHijos().obtener(i);
            
            if (hijo.getNombre().equals(nombreCarpeta)) { //Buscar si es carpeta
                if (hijo.isEsDirectorio()) {
                    this.directorioActual = (Directorio) hijo; 
                    return "Éxito: Entraste a la carpeta '" + nombreCarpeta + "'.";
                } else {
                    return "ERROR: '" + nombreCarpeta + "' es un archivo, no puedes entrar en él.";
                }
            }
        }
        return "ERROR: La carpeta '" + nombreCarpeta + "' no existe en este directorio.";
    }
    
    //Salir de una carpeta
    public String subirDirectorio() {
        if (this.directorioActual == this.raiz) {
            return "ERROR: Ya estás en la Raíz. No puedes retroceder más.";
        }
        
        //Ir a la carpeta padre
        this.directorioActual = (Directorio) this.directorioActual.getPadre();
        return "Éxito: Regresaste a la carpeta '" + this.directorioActual.getNombre() + "'.";
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
