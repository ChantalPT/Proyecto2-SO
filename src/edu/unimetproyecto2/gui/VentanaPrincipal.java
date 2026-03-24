package edu.unimetproyecto2.gui;

import edu.unimetproyecto2.controlador.*;
import edu.unimetproyecto2.modelo.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.Color;

/**
 * @author jesus alejandro
 * 
 */
public class VentanaPrincipal extends javax.swing.JFrame {
    
    private DiscoVirtual disco; 
    private JLabel[] visualBloques; 
    private Directorio rootLogico;       
    private DefaultTreeModel modeloArbol; 
    private DefaultTableModel modeloTablaArchivos;
    private GestorAlmacenamiento gestor;
    private PlanificadorProcesos planificadorProcesos;
    private Simulador hiloSimulador;
    
    private int bloqueCabezalActual = 0; 
    private java.awt.Color colorCabezal = java.awt.Color.YELLOW; 
    private boolean isAdmin = true; // Variable para el control de modo

    public VentanaPrincipal() {
        initComponents(); 
        
        // 1. Iniciamos la LÓGICA
        gestor = new GestorAlmacenamiento(100); 
        this.disco = gestor.getDisco(); 
        this.rootLogico = gestor.getRaiz(); 

        // 2. Inicializamos lo VISUAL
        visualBloques = new JLabel[100];
        inicializarPanelDisco();

        // 3. Configuramos componentes Swing
        modeloArbol = (DefaultTreeModel) treeArchivos.getModel();
        modeloTablaArchivos = (DefaultTableModel) tablaArchivos.getModel();
        modeloTablaArchivos.setRowCount(0);

        // 4. Iniciamos el SIMULADOR
        planificadorProcesos = new PlanificadorProcesos(gestor);
        hiloSimulador = new Simulador(this, planificadorProcesos);
        hiloSimulador.start();
        
        // 5. RECUPERACIÓN AUTOMÁTICA
        ManejadorJournaling recoveryJournal = new ManejadorJournaling();
        if (recoveryJournal.obtenerPendientes().getTamano() > 0) {
            escribirLog("> [SISTEMA] Se detectaron fallos. Ejecutando Rollback...");
            escribirLog(gestor.recuperarSistema());
        }

        actualizarTodoVisual();
    }
    
    // --- MÉTODOS DE APOYO ---

    private void inicializarPanelDisco() {
        panelDisco.removeAll(); 
        int total = disco.getTamanoTotal();

        // CÁLCULO DINÁMICO: Crea una cuadrícula lo más cuadrada posible
        int lado = (int) Math.sqrt(total);
        if (lado * lado < total) lado++;

        // Seteamos el layout con el nuevo número de filas y columnas
        panelDisco.setLayout(new java.awt.GridLayout(lado, lado, 2, 2));

        for (int i = 0; i < total; i++) {
            JLabel cuadro = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            cuadro.setOpaque(true);
            cuadro.setBackground(Color.WHITE);
            cuadro.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            cuadro.setFont(new java.awt.Font("Segoe UI", 0, 9));
            visualBloques[i] = cuadro;
            panelDisco.add(cuadro);
        }
        panelDisco.revalidate();
        panelDisco.repaint();
    }

    public void actualizarCabezalVisual(int destino) {
        if (destino < 0 || destino >= visualBloques.length) return;
        Bloque bAnterior = disco.getBloque(bloqueCabezalActual);
        visualBloques[bloqueCabezalActual].setBackground(bAnterior.isOcupado() ? bAnterior.getColorArchivo() : Color.WHITE);
        
        bloqueCabezalActual = destino;
        visualBloques[destino].setBackground(colorCabezal);
        visualBloques[destino].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    }

    public void actualizarTodoVisual() {
        this.rootLogico = gestor.getRaiz(); 
        actualizarArbol(); 
        actualizarTabla(); 
        actualizarTablaProcesos();

        for (int i = 0; i < disco.getTamanoTotal(); i++) {
            Bloque bLogico = disco.getBloque(i);
            if (i == bloqueCabezalActual) continue;
            visualBloques[i].setBackground(bLogico.isOcupado() ? bLogico.getColorArchivo() : Color.WHITE);
        }
    }

    private void actualizarArbol() {
        DefaultMutableTreeNode visualRoot = new DefaultMutableTreeNode(rootLogico.getNombre());
        llenarNodos(visualRoot, rootLogico);
        modeloArbol.setRoot(visualRoot);
    }

    private void llenarNodos(DefaultMutableTreeNode nodoVisual, Directorio directorioLogico) {
        for (int i = 0; i < directorioLogico.getHijos().getTamano(); i++) {
            Entrada hijo = directorioLogico.getHijos().obtener(i);
            DefaultMutableTreeNode nuevoNodoVisual = new DefaultMutableTreeNode(hijo.getNombre());
            nodoVisual.add(nuevoNodoVisual);
            if (hijo.isEsDirectorio()) llenarNodos(nuevoNodoVisual, (Directorio) hijo);
        }
    }

    private void actualizarTabla() {
        modeloTablaArchivos.setRowCount(0);
        recorrerArchivosParaTabla(gestor.getRaiz());
    }

    private void recorrerArchivosParaTabla(Directorio dir) {
        for (int i = 0; i < dir.getHijos().getTamano(); i++) {
            Entrada e = dir.getHijos().obtener(i);
            if (!e.isEsDirectorio()) {
                Archivo arc = (Archivo) e;
                modeloTablaArchivos.addRow(new Object[]{arc.getNombre(), arc.getTamanoBloques(), arc.getBloqueInicial(), "■"});
            } else {
                recorrerArchivosParaTabla((Directorio) e);
            }
        }
    }

    public void actualizarTablaProcesos() {
        DefaultTableModel modelo = (DefaultTableModel) tablaProcesos.getModel();
        modelo.setRowCount(0);
        for (int i = 0; i < planificadorProcesos.getCantidadProcesos(); i++) {
            PCB p = planificadorProcesos.getColaListos().obtener(i);
            if (p != null) {
                modelo.addRow(new Object[]{p.getPid(), p.getOperacion(), p.getEstado(), "Bloque..."});
            }
        }
    }

    private Entrada buscarEnDirectorio(Directorio dir, String nombre) {
        for (int i = 0; i < dir.getHijos().getTamano(); i++) {
            Entrada e = dir.getHijos().obtener(i);
            if (e.getNombre().equals(nombre)) return e;
            if (e.isEsDirectorio()) {
                Entrada encontrada = buscarEnDirectorio((Directorio) e, nombre);
                if (encontrada != null) return encontrada;
            }
        }
        return null;
    }

    public void escribirLog(String texto) {
        txtLog.append(texto + "\n");
        txtLog.setCaretPosition(txtLog.getDocument().getLength());
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        treeArchivos = new javax.swing.JTree();
        panelDisco = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaArchivos = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaProcesos = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        btnCrear = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnFallo = new javax.swing.JButton();
        btnModo = new javax.swing.JButton();
        btnConfig = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setViewportView(treeArchivos);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.WEST);

        panelDisco.setBorder(javax.swing.BorderFactory.createTitledBorder("Simulación de Disco Virtual (SD)"));
        panelDisco.setLayout(new java.awt.GridLayout(10, 10, 2, 2));
        getContentPane().add(panelDisco, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        jLabel1.setText("TABLA DE ASIGNACIÓN");
        jPanel1.add(jLabel1);

        tablaArchivos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Archivo", "Cant. Bloques", "Bloque Inicial", "Color"
            }
        ));
        jScrollPane2.setViewportView(tablaArchivos);

        jPanel1.add(jScrollPane2);

        jLabel2.setText("COLA DE PROCESOS");
        jPanel1.add(jLabel2);

        tablaProcesos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Operación", "Estado", "Bloque Obj."
            }
        ));
        jScrollPane3.setViewportView(tablaProcesos);

        jPanel1.add(jScrollPane3);

        getContentPane().add(jPanel1, java.awt.BorderLayout.EAST);

        jPanel2.setLayout(new java.awt.BorderLayout());

        btnCrear.setText("Crear Archivo");
        btnCrear.addActionListener(this::btnCrearActionPerformed);
        jPanel3.add(btnCrear);

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(this::btnEliminarActionPerformed);
        jPanel3.add(btnEliminar);

        btnFallo.setText("Simular Fallo");
        btnFallo.addActionListener(this::btnFalloActionPerformed);
        jPanel3.add(btnFallo);

        btnModo.setText("Cambiar Modo (Admin/User)");
        btnModo.addActionListener(this::btnModoActionPerformed);
        jPanel3.add(btnModo);

        btnConfig.setText("Configurar Disco");
        btnConfig.addActionListener(this::btnConfigActionPerformed);
        jPanel3.add(btnConfig);

        jPanel2.add(jPanel3, java.awt.BorderLayout.NORTH);

        txtLog.setEditable(false);
        txtLog.setColumns(20);
        txtLog.setLineWrap(true);
        txtLog.setRows(5);
        jScrollPane4.setViewportView(txtLog);

        jPanel2.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearActionPerformed
try {
            String nombre = JOptionPane.showInputDialog(this, "Nombre del Archivo:");
            if (nombre == null || nombre.isEmpty()) return;
            
            String strTamano = JOptionPane.showInputDialog(this, "Cantidad de bloques:");
            if (strTamano == null) return;
            int tamano = Integer.parseInt(strTamano);

            int bloqueDestino = gestor.getDisco().buscarBloqueLibre(); 
            if (bloqueDestino == -1) {
                escribirLog("ERROR: Disco lleno.");
                return;
            }

            // Creamos el proceso para el planificador
            PCB nuevoProceso = new PCB(gestor.getUsuarioActual(), "CREAR_ARCHIVO", null);
            Object[] params = { nombre, tamano, Color.CYAN, bloqueDestino };
            nuevoProceso.setParametros(params);

            planificadorProcesos.recibirProceso(nuevoProceso);
            actualizarTablaProcesos();
            escribirLog("> [USER] Solicitud de creación: " + nombre);
        } catch (Exception e) {
            escribirLog("ERROR: Datos de creación inválidos.");
        }
    }//GEN-LAST:event_btnCrearActionPerformed

    private void btnFalloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFalloActionPerformed
        escribirLog("!!! CRITICAL FAILURE SIMULATED !!!");
                // Simulamos corrupción en un bloque aleatorio antes de cerrar
                int blockFallo = (int)(Math.random() * disco.getTamanoTotal());
                disco.getBloque(blockFallo).setOcupado(true); 

                if (hiloSimulador != null) {
                    hiloSimulador.detenerInmediatamente(); 
                }
                JOptionPane.showMessageDialog(this, "Fallo crítico en bloque " + blockFallo + ". El sistema se cerrará.");
                System.exit(0); 
    }//GEN-LAST:event_btnFalloActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeArchivos.getLastSelectedPathComponent();
                if (selectedNode == null || selectedNode.isRoot()) {
                    escribirLog("ERROR: Selecciona un archivo válido.");
                    return;
                }

                String nombreEliminar = selectedNode.toString();
                Entrada objetivo = buscarEnDirectorio(gestor.getRaiz(), nombreEliminar);

                if (objetivo != null) {
                    PCB procesoDel = new PCB(gestor.getUsuarioActual(), "ELIMINAR", objetivo);
                    Object[] params = { nombreEliminar, 0, Color.WHITE, 0 };
                    procesoDel.setParametros(params);

                    planificadorProcesos.recibirProceso(procesoDel);
                    actualizarTablaProcesos();
                    escribirLog("> [USER] Solicitud de eliminación: " + nombreEliminar);
                }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnModoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModoActionPerformed
        isAdmin = !isAdmin;
                String nuevo = isAdmin ? "Administrador" : "Usuario";
                gestor.cambiarUsuario(nuevo);

                btnModo.setText("Modo: " + nuevo);
                escribirLog("> [SISTEMA] Cambio de privilegios a: " + nuevo);

                // Bloqueo de funciones según privilegio
                btnConfig.setEnabled(isAdmin);
                btnFallo.setEnabled(isAdmin);
    }//GEN-LAST:event_btnModoActionPerformed

    private void btnConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfigActionPerformed
        String res = JOptionPane.showInputDialog(this, "Nuevo tamaño del disco (bloques):", "100");
        if (res != null) {
            try {
                int nuevoTam = Integer.parseInt(res);

                // 1. Aplicamos el cambio en la lógica del gestor
                escribirLog(gestor.configurarNuevoDisco(nuevoTam));

                // 2. IMPORTANTE: Actualizamos la referencia del disco y el tamaño del array visual
                this.disco = gestor.getDisco();
                this.visualBloques = new JLabel[nuevoTam]; 

                // 3. Redibujamos el panel y refrescamos todo
                inicializarPanelDisco();
                actualizarTodoVisual();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Por favor, ingresa un número válido.");
            }
        }
    }//GEN-LAST:event_btnConfigActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
        /* Create and display the form */
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConfig;
    private javax.swing.JButton btnCrear;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnFallo;
    private javax.swing.JButton btnModo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPanel panelDisco;
    private javax.swing.JTable tablaArchivos;
    private javax.swing.JTable tablaProcesos;
    private javax.swing.JTree treeArchivos;
    private javax.swing.JTextArea txtLog;
    // End of variables declaration//GEN-END:variables
}
