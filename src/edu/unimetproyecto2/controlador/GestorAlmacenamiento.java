package edu.unimetproyecto2.controlador;

import edu.unimetproyecto2.modelo.Archivo;
import edu.unimetproyecto2.modelo.Directorio;
import edu.unimetproyecto2.modelo.DiscoVirtual;
import edu.unimetproyecto2.modelo.Entrada;
import java.awt.Color;

/**
 * @author pinto
 */
public class GestorAlmacenamiento {
    private DiscoVirtual disco;
    private Directorio raiz;
    private Directorio directorioActual;
    private String usuarioActual;
    private ManejadorJournaling journal;
    private boolean simularFallo;

    public GestorAlmacenamiento(int tamanoDisco) {
        this.disco = new DiscoVirtual(tamanoDisco);
        this.raiz = new Directorio("Raiz", "Administrador", null); 
        this.directorioActual = this.raiz;
        this.usuarioActual = "Administrador"; 
        this.journal = new ManejadorJournaling();
        this.simularFallo = false;
    }

    // --- MÉTODOS DE CONFIGURACIÓN Y ACCESO ---

    public String configurarNuevoDisco(int nuevoTamano) {
        if (!usuarioActual.equals("Administrador")) {
            return "ERROR: Solo el Administrador puede reconfigurar el hardware.";
        }
        this.disco = new DiscoVirtual(nuevoTamano);
        this.raiz = new Directorio("Raiz", "Administrador", null);
        this.directorioActual = this.raiz;
        return "ÉXITO: Disco reconfigurado a " + nuevoTamano + " bloques. Sistema reiniciado.";
    }

    public void cambiarUsuario(String nuevoUsuario) {
        this.usuarioActual = nuevoUsuario;
    }

    public boolean tienePermiso(String operacion) {
        if (this.usuarioActual.equals("Administrador")) {
            return true;
        }
        return operacion.equals("LEER");
    }

    // --- OPERACIONES DE ARCHIVOS Y DIRECTORIOS ---

    public String crearArchivo(String nombre, int tamanoBloques, Color color) {
        if (!tienePermiso("CREAR")) 
            return "ERROR: Acceso denegado. Modo Usuario solo permite LEER.";
        
        RegistroJournaling tx = this.journal.registrar("CREAR_ARCHIVO", nombre, tamanoBloques);
        
        Archivo nuevoArchivo = new Archivo(nombre, this.usuarioActual, tamanoBloques, -1, color, this.directorioActual);
        boolean guardadoExitoso = this.disco.asignarArchivo(nuevoArchivo);
        
        if (guardadoExitoso) {
            this.directorioActual.agregarHijo(nuevoArchivo);
            
            if (this.simularFallo) {
                return "CRASH: El sistema falló. '" + nombre + "' quedó PENDIENTE en el Journal.";
            }
            
            tx.confirmar();
            this.journal.actualizarArchivo();
            return "Archivo '" + nombre + "' creado (" + tamanoBloques + " bloques).";
        } else {
            return "ERROR: No hay espacio para " + tamanoBloques + " bloques.";
        }
    }

    /**
     * MÉTODO CORREGIDO: Soluciona el error 'Cannot find symbol' en PlanificadorProcesos
     */
    public String modificarNombre(Entrada objetivo, String nuevoNombre) {
        if (objetivo == null) return "ERROR: No se seleccionó ningún archivo o carpeta.";
        
        if (!tienePermiso("MODIFICAR")) {
            return "ERROR: Permiso denegado. Solo Admin puede renombrar.";
        }

        String nombreViejo = objetivo.getNombre();
        objetivo.setNombre(nuevoNombre);
        
        return "ÉXITO: Se renombró '" + nombreViejo + "' a '" + nuevoNombre + "'.";
    }

    public String eliminarEntrada(Entrada entrada) {
        if (entrada == null) return "ERROR: Entrada nula.";
        if (entrada == raiz) return "ERROR: No se puede eliminar la Raiz.";
        
        if (!tienePermiso("ELIMINAR")) {
            return "ERROR: Permiso denegado. Solo Admin puede eliminar.";
        }
        
        String nombreObjeto = entrada.getNombre();
        RegistroJournaling tx = this.journal.registrar("ELIMINAR", nombreObjeto, 0);

        if (!entrada.isEsDirectorio()) {
            disco.liberarArchivo((Archivo) entrada);
        } else {
            vaciarDirectorio((Directorio) entrada);
        }

        Directorio padre = (Directorio) entrada.getPadre();
        if (padre != null) {
            padre.removerHijo(entrada);
        }

        if (this.simularFallo) {
            return "CRASH: Fallo durante eliminación. '" + nombreObjeto + "' quedó PENDIENTE.";
        }

        tx.confirmar();
        this.journal.actualizarArchivo();
        return "Éxito: '" + nombreObjeto + "' eliminado y bloques liberados.";
    }

    public String leerArchivo(Entrada entrada) {
        if (entrada == null) return "ERROR: No hay archivo seleccionado.";
        if (entrada.isEsDirectorio()) return "ERROR: Es un directorio.";
        
        Archivo archivo = (Archivo) entrada;
        return "CONTENIDO DE " + archivo.getNombre() + ":\nPropietario: " + archivo.getDueno() + "\nBloques: " + archivo.getTamanoBloques();
    }

    public String crearDirectorio(String nombre) {
        if (!tienePermiso("CREAR")) return "ERROR: Solo Admin crea carpetas.";
        Directorio nuevoDir = new Directorio(nombre, this.usuarioActual, this.directorioActual);
        this.directorioActual.agregarHijo(nuevoDir);
        return "Carpeta '" + nombre + "' creada.";
    }

    // --- NAVEGACIÓN Y AUXILIARES ---

    private void vaciarDirectorio(Directorio dir) {
        while (!dir.getHijos().estaVacia()) {
            Entrada hijo = dir.getHijos().obtener(0);
            if (!hijo.isEsDirectorio()) {
                disco.liberarArchivo((Archivo) hijo);
            } else {
                vaciarDirectorio((Directorio) hijo);
            }
            dir.removerHijo(hijo);
        }
    }

    public String entrarDirectorio(String nombreCarpeta) { 
        for (int i = 0; i < directorioActual.getHijos().getTamano(); i++) {
            Entrada hijo = directorioActual.getHijos().obtener(i);
            if (hijo.getNombre().equals(nombreCarpeta) && hijo.isEsDirectorio()) {
                this.directorioActual = (Directorio) hijo; 
                return "Navegando en: " + nombreCarpeta;
            }
        }
        return "ERROR: Directorio no encontrado.";
    }

    public String subirDirectorio() {
        if (this.directorioActual == this.raiz) return "Ya estás en Raiz.";
        this.directorioActual = (Directorio) this.directorioActual.getPadre();
        return "Subiendo a: " + this.directorioActual.getNombre();
    }

    // --- LÓGICA DE RECUPERACIÓN ---

    public String recuperarSistema() {
        edu.unimetproyecto2.estructuras.ListaEnlazada pendientes = journal.obtenerPendientes();
        if (pendientes.estaVacia()) return "Sistema limpio.";

        int fallos = pendientes.getTamano();
        for (int i = 0; i < fallos; i++) {
            RegistroJournaling tx = (RegistroJournaling) pendientes.obtener(i);
            for (int j = 0; j < directorioActual.getHijos().getTamano(); j++) {
                Entrada hijo = directorioActual.getHijos().obtener(j);
                if (hijo.getNombre().equals(tx.getNombreEntrada())) {
                    if (!hijo.isEsDirectorio()) disco.liberarArchivo((Archivo) hijo);
                    directorioActual.removerHijo(hijo);
                    break;
                }
            }
            tx.confirmar(); 
        }
        journal.actualizarArchivo();
        return "Recuperación exitosa: " + fallos + " transacciones limpiadas.";
    }

    // Getters
    public DiscoVirtual getDisco() { return disco; }
    public Directorio getRaiz() { return raiz; }
    public Directorio getDirectorioActual() { return directorioActual; }
    public String getUsuarioActual() { return usuarioActual; }
    public ManejadorJournaling getJournal() { return journal; }
    public void setSimularFallo(boolean simularFallo) { this.simularFallo = simularFallo; }
}