package edu.unimetproyecto2.controlador;

import edu.unimetproyecto2.gui.VentanaPrincipal;
import edu.unimetproyecto2.estructuras.ListaEnlazada;
import edu.unimetproyecto2.modelo.PCB;

/**
 * @author jesus alejandro
 */
public class Simulador extends Thread {
    private VentanaPrincipal vista;
    private PlanificadorProcesos planificador;
    private volatile boolean encendido; 
    private PlanificadorDisco planificadorAlgoritmos;
    private String algoritmoActual = "FIFO"; 
    private int posicionCabezal = 0; 
    private ManejadorJournaling journal = new ManejadorJournaling();

    public Simulador(VentanaPrincipal vista, PlanificadorProcesos planificador) {
        this.vista = vista;
        this.planificador = planificador;
        this.planificadorAlgoritmos = new PlanificadorDisco(0, 100);
        this.encendido = true;
    }

    @Override
    public void run() {
        while (encendido) {
            // Buscamos si hay un proceso listo para ejecutar
            PCB proceso = planificador.obtenerSiguiente();

            if (proceso != null) {
                String nombreEntrada = (String) proceso.getParametros()[0];
                
                proceso.setEstado("EJECUTANDO");

                java.awt.EventQueue.invokeLater(() -> {
                    vista.actualizarTodoVisual();
                    vista.escribirLog("> [SIMULADOR] Iniciando movimiento para: " + proceso.getOperacion() + " en " + nombreEntrada);
                });

                // --- MOVIMIENTO FÍSICO DEL CABEZAL ---
                // Aquí procesamos el algoritmo de planificación de disco antes de la ejecución lógica
                procesarConAlgoritmo(algoritmoActual); 

                // --- EJECUCIÓN LÓGICA FINAL ---
                // Una vez que el cabezal "llegó", el planificador ejecuta la operación en el disco virtual
                String logResultado = planificador.atenderSiguienteProceso();

                java.awt.EventQueue.invokeLater(() -> {
                    vista.escribirLog(logResultado);
                    vista.actualizarTodoVisual();
                });
            }

            try { 
                Thread.sleep(800); 
            } catch (InterruptedException e) { 
                break; 
            }
        }
    }
    
    public void detenerInmediatamente() { 
        this.encendido = false; 
        this.interrupt(); 
    }
    
    public void actualizarConfiguracionDisco(int nuevosBloques) {
        this.planificadorAlgoritmos = new PlanificadorDisco(0, nuevosBloques);
        this.posicionCabezal = 0; 
    }
    
    private void moverCabezalHacia(int bloqueDestino) {
        while (posicionCabezal != bloqueDestino && encendido) {
            if (posicionCabezal < bloqueDestino) {
                posicionCabezal++;
            } else {
                posicionCabezal--;
            }

            final int pos = posicionCabezal;
            java.awt.EventQueue.invokeLater(() -> {
                vista.actualizarCabezalVisual(pos);
            });

            try {
                Thread.sleep(100); // Velocidad del movimiento visual
            } catch (InterruptedException e) {
                break;
            }
        }
    }
    
    private void procesarConAlgoritmo(String tipoAlgoritmo) {
        ListaEnlazada<Integer> solicitudes = new ListaEnlazada<>();
        
        // CORRECCIÓN AQUÍ: Llamamos al método correcto del planificador para el primer proceso (índice 0)
        int bloqueObjetivo = planificador.obtenerBloqueDeProceso(0);
        solicitudes.insertar(bloqueObjetivo);

        PlanificadorDisco.Resultado resultado;

        switch (tipoAlgoritmo) {
            case "SSTF":
                resultado = planificadorAlgoritmos.ejecutarSSTF(solicitudes, posicionCabezal);
                break;
            case "SCAN":
                resultado = planificadorAlgoritmos.ejecutarSCAN(solicitudes, posicionCabezal, true);
                break;
            default: // FIFO
                resultado = planificadorAlgoritmos.ejecutarFIFO(solicitudes);
                break;
        }

        // Mover el cabezal según la secuencia calculada
        for (int i = 0; i < resultado.secuenciaAtencion.getTamano(); i++) {
            if (!encendido) break; 
            
            int siguienteDestino = (int) resultado.secuenciaAtencion.obtener(i);
            moverCabezalHacia(siguienteDestino);
        }
    }
    
    public void setAlgoritmo(String alg) {
        this.algoritmoActual = alg;
        if(vista != null) vista.escribirLog("> [SISTEMA] Algoritmo cambiado a: " + alg);
    }

    public int getPosicionCabezal() {
        return posicionCabezal;
    }
}