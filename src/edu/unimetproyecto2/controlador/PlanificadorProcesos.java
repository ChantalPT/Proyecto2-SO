package edu.unimetproyecto2.controlador;

import edu.unimetproyecto2.estructuras.Cola;
import edu.unimetproyecto2.modelo.Archivo;
import edu.unimetproyecto2.modelo.Entrada;
import edu.unimetproyecto2.modelo.PCB;

/**
 * @author pinto
 */
public class PlanificadorProcesos {
    private Cola<PCB> colaListos;
    private Cola<PCB> colaBloqueados;
    private GestorAlmacenamiento gestor;
    
    public PlanificadorProcesos(GestorAlmacenamiento gestor) {
        this.colaListos = new Cola<>();
        this.colaBloqueados = new Cola<>();
        this.gestor = gestor;
    }
    
    public void recibirProceso(PCB nuevoProceso) {
        colaListos.encolar(nuevoProceso);
        System.out.println("Proceso encolado: " + nuevoProceso.getPid() + " - " + nuevoProceso.getOperacion());
    }
    
    public String atenderSiguienteProceso() {
        if (colaListos.estaVacia()) {
            return "No hay procesos en la cola de listos";
        }
        
        PCB pcb = colaListos.desencolar(); 
        Entrada objetivo = pcb.getObjetivo();
        String resultado = "";

        // --- CONTROL DE CONCURRENCIA ---
        if (objetivo != null && !objetivo.isEsDirectorio()) {
            Archivo arch = (Archivo) objetivo;
            boolean esOperacionEscritura = !pcb.getOperacion().equals("LEER_ARCHIVO");

            if (arch.isBloqueadoEscritura() || (esOperacionEscritura && arch.getLectoresActivos() > 0)) {
                pcb.setEstado("BLOQUEADO");
                colaBloqueados.encolar(pcb); 
                return "Proceso " + pcb.getPid() + " [" + pcb.getOperacion() + "] BLOQUEADO por concurrencia en '" + arch.getNombre() + "'";
            }

            if (esOperacionEscritura) {
                arch.setBloqueadoEscritura(true);
            } else {
                arch.agregarLector(); 
            }
        }

        pcb.setEstado("EJECUTANDO");
        Object[] params = pcb.getParametros();
        gestor.cambiarUsuario(pcb.getUsuario());

        // --- EJECUCIÓN DE OPERACIÓN ---
        switch (pcb.getOperacion()) {
            case "CREAR_ARCHIVO":
                // Los índices deben coincidir con VentanaPrincipal
                resultado = gestor.crearArchivo((String)params[0], (Integer)params[1], (java.awt.Color)params[2]);
                break;
            case "CREAR_DIRECTORIO":
                resultado = gestor.crearDirectorio((String)params[0]);
                break;
            case "LEER_ARCHIVO":
                resultado = gestor.leerArchivo(objetivo);
                break;
            case "MODIFICAR_NOMBRE":
                resultado = gestor.modificarNombre(objetivo, (String)params[0]);
                break;
            case "ELIMINAR":
                resultado = gestor.eliminarEntrada(objetivo);
                break;
            default:
                resultado = "ERROR: Operación desconocida.";
        }

        // --- LIBERAR LOCKS ---
        if (objetivo != null && !objetivo.isEsDirectorio()) {
            Archivo arch = (Archivo) objetivo;
            if (!pcb.getOperacion().equals("LEER_ARCHIVO")) {
                arch.setBloqueadoEscritura(false); 
            } else {
                arch.removerLector(); 
            }
        }

        pcb.setEstado("TERMINADO");
        revisarBloqueados();
        return "Proceso " + pcb.getPid() + " finalizado.\n> " + resultado;
    }
    
    private void revisarBloqueados() {
        int tamano = colaBloqueados.getTamano(); 
        for (int i = 0; i < tamano; i++) {
            PCB pcbBloqueado = colaBloqueados.desencolar();
            pcbBloqueado.setEstado("LISTO");
            colaListos.encolar(pcbBloqueado);
        }
    }
    
    public boolean tieneProcesosEsperando() {
        return !colaListos.estaVacia();
    }

    public int getCantidadProcesos() {
        return colaListos.getTamano();
    }

    // --- CORRECCIÓN CLAVE PARA LA SIMULACIÓN VISUAL ---
    public int obtenerBloqueDeProceso(int indice) {
        if (indice < 0 || indice >= colaListos.getTamano()) return 0;

        PCB p = colaListos.obtener(indice); 

        if (p.getOperacion().equals("CREAR_ARCHIVO")) {
            // El bloque destino es el índice 3 según tu VentanaPrincipal
            return (int) p.getParametros()[3]; 
        } else if (p.getObjetivo() != null && !p.getObjetivo().isEsDirectorio()) {
            // Si es LEER, MODIFICAR o ELIMINAR, usamos el bloque donde ya existe el archivo
            return ((edu.unimetproyecto2.modelo.Archivo) p.getObjetivo()).getBloqueInicial();
        }

        return 0; 
    }

    public Cola<PCB> getColaListos() {
        return colaListos;
    }

    public PCB obtenerSiguiente() {
        for (int i = 0; i < colaListos.getTamano(); i++) {
            PCB p = colaListos.obtener(i);
            if (p.getEstado().equals("LISTO")) {
                return p;
            }
        }
        return null; 
    }
}