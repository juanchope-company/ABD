package com.Archivos;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.JOptionPane;

/**
 *
 * @author Juanchope 
 * @version 1.0
 */
public abstract class  Archivo {
    
    public static final String ENTRADA = "entradas\\";
    public static final String SALIDA = "salidas\\";
    protected String nombreArchivo;
    protected byte[] datos;
    
    /**
     * lee el tipo de documento escogido
     * @return devuelve su contenido en el formato especificado
     */
    //public abstract Object Leer();

    public static void main(String[] args) {
        System.out.println(CopiarArchivo("D:\\backUp.sql", "D:\\copia2"));
    }
    /**
     * Escribe lo que necesita en el formato escogido
     * @param Contenido lo que se va a escribir en dicho documento
     * @return si fué correctamente escrito o no
     */
    
    //public abstract boolean Escribir(Object Contenido);
    
    /**
     * clona un archivo
     * @param entrada ruta del archivo de origen
     * @param salida ruta del archivo de salida sin extencion
     * @return nombre de archivo de salida
     */
    public static String CopiarArchivo(String entrada, String salida) {  
        //capturar extension del archivo de entrada para el archivo de salida
        String extension = entrada.substring(entrada.lastIndexOf("."));
        
        //agregando extension al archivo de salida
        File    archivoEntrada = new File(entrada),
                archivoSalida = new File(salida + extension);
        
        //validar existencia
        if (archivoSalida.exists() | !archivoEntrada.exists())
            return null;
        
        //verifica que el archivo sea corectamente escrito
        for (int i=0;archivoSalida.exists();i++) 
            archivoSalida = new File(salida + "_" + i + extension);
        
        //genera el directorio en caso de que no exista
        //archivoSalida.getParentFile().mkdirs();
        
        //intenta crear el archivo 
        try {
            archivoSalida.createNewFile();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        
        try {
            OutputStream flujoSalida;
            try (InputStream flujoEntrada = new FileInputStream(entrada)) {
                flujoSalida = new FileOutputStream(archivoSalida);
                
                //buffer de escritura que soporta hasta archivos de 1024 bytes
                //o sele envia directamente el tamaño del documento
                byte[] buf = new byte[1024];
                int len;
                
                //escritura de bytes
                while ((len = flujoEntrada.read(buf)) > 0) 
                    flujoSalida.write(buf, 0, len);
                
                //intento de escritura ilimitada
                flujoEntrada.close();
            }
            flujoSalida.close();
            
            return archivoSalida.getName();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static boolean AbrirDocumento(String nombreArchivo) {
        //System.out.println("Abrir archivo " + salidas + nombreArchivo);
        Desktop ficheroAEjecutar=Desktop.getDesktop();
        try {
            File archivo = new File(SALIDA + nombreArchivo);
            if(archivo.exists()){
                ficheroAEjecutar.open(archivo);
                return true;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }        
        return false;
    }
    
    protected boolean  probarArchivo(String prefijo){
        File archivo = new File(prefijo + nombreArchivo);
        return archivo.exists();
    }
}
