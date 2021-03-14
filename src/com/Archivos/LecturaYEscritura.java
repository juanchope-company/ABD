package com.Archivos;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juanchope
 */
public class LecturaYEscritura {
    
    private final String 
            NOMBREARCHIVO;

    public LecturaYEscritura(String NOMBREARCHIVO) {
        this.NOMBREARCHIVO = NOMBREARCHIVO;
    }

    // Actualizar a la estructura deseada
    public void almacenarEnArchivoDeObjetos(LinkedList<Object> obj) 
    {
        ObjectOutputStream  salida=null;
        try { 
            // escritura de datos
            salida  =  new  ObjectOutputStream( 
                    new  FileOutputStream( NOMBREARCHIVO ) ) ;
            salida.writeObject(obj);
            System.out.println("Guardado Exitoso.");             
        } catch( IOException  e ) { 
            System.out.println("Error almacenando los datos en archivo de objetos "+e.getMessage()) ;
        } finally
        {
            try{
                salida.close();
            } catch(IOException e){
               System.out.println("Error cerrando archivo de objetos") ;             
            }
        }                      
    }
    
    // Actualizar a la estructura deseada    
    public LinkedList<Object> recuperarDesdeArchivoDeObjetos() 
    {   
        LinkedList<Object> res = null;
        ObjectInputStream  entrada=null;
        
        try{
            // lectura de datos
            entrada  =   new  ObjectInputStream( 
                    new  FileInputStream( NOMBREARCHIVO ) ) ;
            
            LinkedList<Object> aux = (LinkedList<Object>) entrada.readObject();
            res = new LinkedList<>();
            
            for (Object obj : aux)
                res.add(obj);
            
        } catch( IOException | ClassNotFoundException  e ) { 
            System.out.println("Error recuperando los datos desde archivo de objetos"+e.getMessage()) ;
        } finally
        {
            try{
                entrada.close();
            } catch(IOException e){
               System.out.println("Error cerrando archivo de objetos") ;             
            }
        }      
        return res;
    }
    
    public static byte[] filetoByteImage(File archivo){
        FileInputStream fis =null;
        try {
            fis = new FileInputStream(archivo);
            byte[] fileBytes;
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                int b;
                byte[] buffer = new byte[1024];
                while((b=fis.read(buffer))!=-1){
                    bos.write(buffer,0,b);
                }   fileBytes = bos.toByteArray();
                fis.close();
                return fileBytes;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LecturaYEscritura.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LecturaYEscritura.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(LecturaYEscritura.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    public static byte[] archivoDeImagenTobytes(File archivo){
        byte [] data = null;
        try {
//            File archivo = new File("/Users/ingenieria/Desktop/Proyecto Odonto Web/OdontoWeb/OdontoWeb/web/img/header2.png");
                        
            FileInputStream fis =new FileInputStream(archivo);
            ByteArrayOutputStream bos=new ByteArrayOutputStream();
            int b;
            byte[] buffer = new byte[1024];
            while((b=fis.read(buffer))!=-1){
                  bos.write(buffer,0,b);
            }
            data =bos.toByteArray();
            fis.close();
            bos.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return data;
    }
}
