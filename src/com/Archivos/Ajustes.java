package com.Archivos;

import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author Juanchope
 */
public class Ajustes implements Serializable{
    
    private static final String 
            NOMBREDEARCHIVO = "Ajustes.obj";
    
    private static String
//            URLARCHIVO = "/Users/ingenieria/Desktop/Proyecto Odonto Web/OdontoWeb/web/imgsPrueba/BASE/base.png",
            URLBASE = "img/odontograma/base/base.png",
            URLPATH = "img/odontograma";
    

    
    private final Datos
            antecedentesFamiliares;

    public Ajustes(Datos antecedentesFamiliares) {
        this.antecedentesFamiliares = antecedentesFamiliares;
    }

    public Datos getAntecedentesFamiliares() {
        return antecedentesFamiliares;
    }
    
    public static void main(String[] args) {
        Ajustes ajus= new Ajustes(new Datos(null));
        setAjustes(ajus);
        System.out.println(getAjustes());
    }
    
    public static Ajustes getAjustes(){
        LecturaYEscritura ajustes = new LecturaYEscritura(NOMBREDEARCHIVO);
        LinkedList<Object> obj = ajustes.recuperarDesdeArchivoDeObjetos();
        if (obj != null)
            if (obj.size() == 1 && obj.get(0) instanceof Ajustes)
                return  (Ajustes) obj.get(0);
        return null;
    }
    
    public static void setAjustes(Ajustes obj){
        LecturaYEscritura ajustes = new LecturaYEscritura(NOMBREDEARCHIVO);
        LinkedList<Object> aux = new LinkedList<>();
        aux.add(obj);
        ajustes.almacenarEnArchivoDeObjetos(aux);
    } 

    public static String getURLBASE() {
        return URLBASE;
    }

    public static void setURLBASE(String URLBASE) {
        Ajustes.URLBASE = URLBASE;
    }

    public static String getURLPATH() {
        return URLPATH;
    }

    public static void setURLPATH(String URLPATH) {
        Ajustes.URLPATH = URLPATH;
    }
    
    
}
