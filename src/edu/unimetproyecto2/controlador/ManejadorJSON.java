/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.unimetproyecto2.controlador;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.unimetproyecto2.modelo.Archivo;
import edu.unimetproyecto2.modelo.Directorio;
import edu.unimetproyecto2.modelo.DiscoVirtual;
import edu.unimetproyecto2.modelo.Entrada;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 *
 * @author pinto
 */
public class ManejadorJSON {
    //Exportar
    public static String exportarSistema(Directorio raiz, String rutaArchivo) {
        try {
            if (!rutaArchivo.endsWith(".json")) rutaArchivo += ".json";

            //Convertir Raiz a un Objeto JSON seguro
            JsonObject jsonRaiz = convertirAJson(raiz);

            //Configurar Gson para que el archivo se vea bien
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String textoJson = gson.toJson(jsonRaiz);

            // Guardamos el archivo
            FileWriter escritor = new FileWriter(rutaArchivo);
            escritor.write(textoJson);
            escritor.close();

            return "Éxito: Sistema exportado a " + rutaArchivo;
        } catch (Exception e) {
            return "ERROR al exportar: " + e.getMessage();
        }
    }

    //transformar árbol N-ario a JSON
    private static JsonObject convertirAJson(Entrada entrada) {
        JsonObject obj = new JsonObject();
        obj.addProperty("nombre", entrada.getNombre());

        if (entrada.isEsDirectorio()) {
            Directorio dir = (Directorio) entrada;
            obj.addProperty("tipo", "directorio");
            obj.addProperty("dueno", dir.getDueno());

            JsonArray hijosArray = new JsonArray();
            int cantidad = dir.getHijos().getTamano();
            for (int i = 0; i < cantidad; i++) {
                Entrada hijo = (Entrada) dir.getHijos().obtener(i);
                hijosArray.add(convertirAJson(hijo)); // Recursividad
            }
            obj.add("hijos", hijosArray);
        } else {
            Archivo arch = (Archivo) entrada;
            obj.addProperty("tipo", "archivo");
            obj.addProperty("dueno", arch.getDueno());
            obj.addProperty("tamanoBloques", arch.getTamanoBloques());
        }
        return obj;
    }

    //Importar
    public static Directorio importarSistema(String rutaArchivo, DiscoVirtual disco) {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) return null;

        try {
            //Leer el archivo JSON 
            FileReader lector = new FileReader(archivo);
            JsonObject jsonRaiz = JsonParser.parseReader(lector).getAsJsonObject();
            lector.close();

            //Reconstruir el árbol
            return (Directorio) reconstruirDesdeJson(jsonRaiz, null, disco);

        } catch (Exception e) {
            System.out.println("Error al leer JSON: " + e.getMessage());
            return null;
        }
    }

    private static Entrada reconstruirDesdeJson(JsonObject obj, Directorio padre, DiscoVirtual disco) {
        String nombre = obj.get("nombre").getAsString();
        String tipo = obj.get("tipo").getAsString();
        String dueno = obj.get("dueno").getAsString();

        if (tipo.equals("directorio")) {
            Directorio dir = new Directorio(nombre, dueno, padre);
            
            if (obj.has("hijos")) {
                JsonArray hijosArray = obj.getAsJsonArray("hijos");
                for (JsonElement elemento : hijosArray) {
                    Entrada hijoReconstruido = reconstruirDesdeJson(elemento.getAsJsonObject(), dir, disco);
                    dir.agregarHijo(hijoReconstruido);
                }
            }
            return dir;
        } else {
            int tamano = obj.get("tamanoBloques").getAsInt();
            Archivo arch = new Archivo(nombre, dueno, tamano, -1, java.awt.Color.GRAY, padre);
            
            //Pedir al disco que reserve el espacio para el archivo cargado
            disco.asignarArchivo(arch);
            return arch;
        }
    }
}