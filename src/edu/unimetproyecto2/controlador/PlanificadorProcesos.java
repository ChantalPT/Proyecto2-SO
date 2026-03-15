/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.unimetproyecto2.controlador;

import edu.unimetproyecto2.estructuras.Cola;
import edu.unimetproyecto2.modelo.Archivo;
import edu.unimetproyecto2.modelo.Entrada;
import edu.unimetproyecto2.modelo.PCB;

/**
 *
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
        
        PCB pcb = colaListos.desencolar(); //Se desencola al primero de la fila
        Entrada objetivo = pcb.getObjetivo();
        String resultado = "";

        //Verificar locks (Control de Concurrencia) 
        //Locks estrictos si el objetivo = Archivo
        if (objetivo != null && !objetivo.isEsDirectorio()) {
            Archivo arch = (Archivo) objetivo;
            boolean esOperacionEscritura = !pcb.getOperacion().equals("LEER_ARCHIVO");

            //Si quiero escribir y hay lectores activos entonces bloquear
            if (arch.isBloqueadoEscritura() || (esOperacionEscritura && arch.getLectoresActivos() > 0)) {
                pcb.setEstado("BLOQUEADO");
                colaBloqueados.encolar(pcb); 
                return "Proceso " + pcb.getPid() + " [" + pcb.getOperacion() + "] BLOQUEADO por concurrencia en '" + arch.getNombre() + "'";
            }

            //Aplico Lock temporalmente si pasa el filtro
            if (esOperacionEscritura) {
                arch.setBloqueadoEscritura(true);
            } else {
                arch.agregarLector(); //lock compartido (entra a leer)
            }
        }

        pcb.setEstado("EJECUTANDO");
        Object[] params = pcb.getParametros();
        gestor.cambiarUsuario(pcb.getUsuario());

        switch (pcb.getOperacion()) {
            case "CREAR_ARCHIVO":
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
                resultado = "ERROR:\n Operación del PCB desconocida.";
        }

        //Liberar locks
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
    
    // Mover procesos de Bloqueados a Listos
    private void revisarBloqueados() {
        int tamano = colaBloqueados.getTamano(); 
        for (int i = 0; i < tamano; i++) {
            PCB pcbBloqueado = colaBloqueados.desencolar();
            
            //Se regresa a la cola de Listos para que lo vuelva a intentar en el siguiente ciclo
            pcbBloqueado.setEstado("LISTO");
            colaListos.encolar(pcbBloqueado);
        }
    }
}
