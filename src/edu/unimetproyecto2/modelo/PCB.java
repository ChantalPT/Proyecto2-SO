package edu.unimetproyecto2.modelo;

/**
 * @author pinto
 * 
 */
public class PCB {
    private static int contadorPID = 1;
    private int pid; // Process ID único
    private String usuario; // Administrador o Usuario
    private String operacion; // CREAR_ARCHIVO, ELIMINAR, LEER, etc.
    private Entrada objetivo; // El archivo o carpeta si ya existe
    private String estado; // LISTO, EJECUTANDO, TERMINADO, ERROR
    private Object[] parametros; // [0]: Nombre, [1]: Tamaño, [2]: Color, [3]: BloqueInicial
    
    // Constructor principal para procesos nuevos (como CREAR)
    public PCB(String usuario, String operacion, Entrada objetivo) {
        this.pid = contadorPID++;
        this.usuario = usuario;
        this.operacion = operacion;
        this.objetivo = objetivo;
        this.parametros = new Object[4]; // Inicializamos el espacio para los 5 botones
        this.estado = "LISTO"; 
    }

    // --- GETTERS Y SETTERS ---

    public static int getContadorPID() {
        return contadorPID;
    }

    public static void setContadorPID(int contadorPID) {
        PCB.contadorPID = contadorPID;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public Entrada getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(Entrada objetivo) {
        this.objetivo = objetivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Object[] getParametros() {
        return parametros;
    }

    public void setParametros(Object[] parametros) {
        this.parametros = parametros;
    }
    
    @Override
    public String toString() {
        // Mejoramos el toString para que el Log de la VentanaPrincipal sea más claro
        String detalle = (parametros != null && parametros.length > 0) ? (String) parametros[0] : "";
        return "PID: " + pid + " | " + operacion + " | " + detalle + " | [" + estado + "]";
    }
}