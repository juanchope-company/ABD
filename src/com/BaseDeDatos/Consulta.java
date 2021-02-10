package com.BaseDeDatos;

import static com.BaseDeDatos.Tabla.ID;
import java.util.LinkedList;

/**
 *
 * @author Juanchope
 * @version 1.0
 */
public class Consulta {
    
    public static final char
            SELECIONAR = 'S',
            INSERTAR = 'I',
            ELIMINAR = 'E',
            AGREGAR = 'G',
            ACTUALIZAR = 'A',
            TRUNCAR = 'T',
            TABLA_ELIMINAR = 'B',
            TABLA_CREAR = 'C',
            BD_CREAR = 'D',
            BD_ELIMINAR = 'F';
            
    private final char
            accion;
    
    private final String
            nombre;
    
    private final Campo[]
            campos;
    
    private Campo[]
            condiciones;
    
    public Consulta(String tabla,char accion, Campo... campos) {
        this.nombre = tabla;
        this.accion = accion;
        this.campos = campos;
    }

    Consulta(String nombre, Campo[] campos, Campo... condiciones) {
        this.nombre = nombre;
        this.accion = ACTUALIZAR;
        this.campos = campos;
        this.condiciones = condiciones;
    }
    
    private String getSelcionar(){
        String res = "SELECT ";
        for (int i = 0; i < campos.length; i++) {
            if (i != 0)
                res += " , ";
            res += campos[i].getNombre();
        }
        
        res += " FROM public." + nombre;        
        try {
           if (campos != null || campos.length != 0)
               res += getParametrizacion();            
        } catch (Exception e) {
        }
        
        return res;
    }
    
    private String getParametrizacion(){
        String res = "";
        if (this.condiciones != null){
            res = " WHERE ";
            for (Campo obj : campos)
                if (obj.isValor())
                    res += obj.getNombre() + " = ? AND ";
        }else{
            for (Campo campo : campos)
                res += campo.getNombre() + "=?, ";
            res = res.substring(0, res.length()- 2) + " WHERE ";
            
            for (Campo obj : condiciones)
                if (obj.isValor())
                    res += obj.getNombre() + " = ? AND ";
        }
        return res.substring(0, res.length() - 5);
    }
    
    public Campo[] getParametros(){
        LinkedList<Campo> aux = new LinkedList<>();
        
        for (Campo campo : campos)
            if (campo.isValor())
                aux.add(campo);
        
        Campo[] res = new Campo[aux.size()];
        for (int i = 0; i < aux.size(); i++)
            res[i] = aux.get(i);
        
        return res;
    }

    public static LinkedList<Campo[]> getSeleccion(Campo[] campos, BasedeDatos BD) {
//        String Consulta = "Select ";
//        LinkedList<Campo> campss = new LinkedList<>();
//        
//        for (int i = 0; i < camps.length ; i++) {
//            if (i != 0)
//                Consulta += " , ";
//            Consulta += camps[i].getNombre();
//            if (camps[i].getValor() != null)
//                campss.add(camps[i]);
//        }
//        
//        Campo[] campos = new Campo[campss.size()];
//        for (int i = 0; i < campss.size(); i++)
//            campos[i] = campss.get(i);
        
        Consulta consulta = new Consulta(BD.getNOMBRE(),Consulta.SELECIONAR, campos);        
        return BD.recibirConsultaIndexadas(consulta);
    }

    public String getConsulta() {
        String res = "";
        
        switch(accion){
            case ACTUALIZAR:
                return getActualizar();
            case AGREGAR:
                return getAgregar();
            case ELIMINAR:
                return getEliminar();
            case SELECIONAR:
                return getSelcionar();
            case TABLA_CREAR:
                return getCrearTabla();
            case TABLA_ELIMINAR:
                return getEliminarTabla();
            case BD_CREAR:
                return getCrearBD();
            case BD_ELIMINAR:
                return getEliminarBD();
        }
        
        return res;
    }

    private String getCrearBD() {
        String res = "CREATE DATABASE \"" + nombre + "\"";
        return res;
    }

    private String getEliminarBD() {
        String res = "DROP DATABASE " + nombre;
        return res;
    }

    private String getActualizar() {
        String res = "UPDATE public." + nombre + " SET " + getParametrizacion();
        return res;
    }

    private String getEliminar() {
        String res = "DELETE FROM public." + nombre + "\n" + getParametrizacion();
        return res;
    }

    private String getAgregar() {
        String res = "INSERT INTO public." + nombre + "(\n" + getCampos() + ")\n" + " VALUES (";
        for (int i = 0; i < campos.length -1; i++) 
            res += " ? ,";        
        return res + " ? )";
    }

    private String getCampos() {
        String res = " ";
        
        for (Campo campo : campos)
            res += campo.getNombre() + "  , ";
        
        return res.substring(0, res.length()-3);
    }

    private String getCrearTabla() {
        //extrayendo campos
        String 
                res,
                contenido = "(";
        for (Campo obj : campos)
            contenido += obj.getCampoToString() + " , \n";
        
        contenido += ID.getCampoToString();
        String llavesPrimarias = ", \nCONSTRAINT " + nombre + "_pkey PRIMARY KEY ( " + ID.getNombre();
        
        for (Campo campo : campos) 
            if (campo.isLlavePrimaria())
                    llavesPrimarias += " , " + campo.getNombre();
        
        llavesPrimarias += ")";
        contenido += llavesPrimarias;
        
        //generando consulta
        res = "CREATE TABLE public." + nombre + "\n" + contenido +
            "\n)WITH (OIDS = FALSE) TABLESPACE pg_default;" +
            "\nALTER TABLE public." + nombre + 
            "\nOWNER to postgres;";
        return res;
    }

    private String getEliminarTabla() {
        return "DROP TABLE public." + nombre;
    }

    boolean respuesta(int estado) {
        switch(accion){
            case BD_CREAR:
                return estado >= 0;
            default:
                return estado ==1;
        }
    }
    
}
