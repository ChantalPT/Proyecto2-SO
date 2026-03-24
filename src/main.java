

import edu.unimetproyecto2.estructuras.Cola;
import edu.unimetproyecto2.modelo.Archivo;
import edu.unimetproyecto2.modelo.Directorio;
import edu.unimetproyecto2.modelo.DiscoVirtual;
import edu.unimetproyecto2.simulation.Estado;
import edu.unimetproyecto2.simulation.Proceso;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author jesus alejandro
 */
public static void main(String[] args) {
    // Probar la Cola
    Cola<String> pruebaCola = new Cola<>();
    pruebaCola.encolar("Proceso 1");
    pruebaCola.encolar("Proceso 2");
    
    System.out.println("Saliendo de la cola: " + pruebaCola.desencolar()); // Debe decir Proceso 1
    System.out.println("Siguiente en fila: " + pruebaCola.verFrente());    // Debe decir Proceso 2
    
    DiscoVirtual miDisco = new DiscoVirtual(10); // Disco pequeño de 10 bloques
    int primerLibre = miDisco.buscarBloqueLibre();
    
    if (primerLibre == 0) {
        System.out.println("✅ Disco inicializado correctamente.");
    } else {
        System.out.println("❌ Error en el disco.");
      }
    
    // 1. Crear el Proceso
    Proceso p = new Proceso(1, "CREAR", 0);
    p.setEstadoActual(Estado.LISTO);

    // 2. Simular asignación encadenada (2 bloques)
    int b1 = miDisco.buscarBloqueLibre();
    miDisco.getBloque(b1).setOcupado(true);
    miDisco.getBloque(b1).setNombreArchivo("foto.jpg");
    
    int b2 = miDisco.buscarBloqueLibre(); // Debería dar el siguiente libre
    miDisco.getBloque(b1).setSiguienteBloque(b2); // ENCADENAMIENTO
    miDisco.getBloque(b2).setOcupado(true);
    miDisco.getBloque(b2).setNombreArchivo("foto.jpg");

    // 3. Crear el objeto Archivo
    Directorio root = new Directorio("root", "admin", null);
    Archivo nuevoArc = new Archivo("foto.jpg", "admin", 2, b1, java.awt.Color.BLUE, root);
    root.agregarHijo(nuevoArc);

    System.out.println("Archivo creado en: " + nuevoArc.getPadre().getNombre());
    System.out.println("Bloque inicial: " + nuevoArc.getBloqueInicial());
    System.out.println("Siguiente bloque del primero: " + miDisco.getBloque(b1).getSiguienteBloque());
}
