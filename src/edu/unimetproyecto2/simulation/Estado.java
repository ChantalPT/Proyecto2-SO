/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.unimetproyecto2.simulation;

/**
 *
 * @author jesus alejandro
 */
public enum Estado {
    NUEVO,      // El proceso acaba de ser creado
    LISTO,      // Está en la cola esperando al planificador de disco
    EJECUTANDO, // El cabezal del disco está atendiendo su solicitud
    BLOQUEADO,  // Esperando por un Lock (concurrencia)
    TERMINADO   // La operación terminó con éxito
}
