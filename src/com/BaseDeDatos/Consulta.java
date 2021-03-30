package com.BaseDeDatos;

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
            tabla;
    
    private final Campo[]
            campos;
    
    public Consulta(String tabla,char accion, Campo... campos) {
        this.tabla = tabla;
        this.accion = accion;
        this.campos = campos;
    }
    
    private String getSelcionar(){
        return "SELECT " + getCondiciones() + " FROM public." + tabla + getParametrizacion();            
    }
    
    private String getCondiciones(){
        String res = "";
        int n = 0;
        if (campos != null)
            for (int i = 0; i < campos.length; i++)
                if (campos[i].isParametro()){
                    if (n != 0)
                        res += " , ";
                    res += campos[i].getNombre();
                    n++;
                }
        return res;
    }
    private String getParametrizacion(){
        String res = "", aux = "";
        if (campos != null || campos.length != 0){
            for (Campo obj : campos){
                if (accion == ACTUALIZAR){
                    if (obj.isValor() && obj.isLlavePrimaria())
                        aux += obj.getbusqueda() + " AND ";
                    else if (obj.isValor())
                        res += obj.getbusqueda() + " ,   ";                      
                }else
                    if (obj.isValor() && obj.isbusqueda())
                        aux += obj.getbusqueda()+ " OR ";
                    else if (obj.isValor())
                        aux += obj.getbusqueda()+ " AND ";
            }       
            
            if (accion == ACTUALIZAR){
                if (res.length() != 0)
                    res = res.substring(0, res.length() - 5); 
                if (aux.length() != 0)
                    res += " WHERE " + aux.substring(0, aux.length() - 5);
            }else
                if (aux.length() != 0)
                    res = " WHERE " + aux.substring(0, aux.length() - 5);
        }
        return res;
    }
    
    public Campo[] getParametros(){
        LinkedList<Campo> aux = new LinkedList<>();
        
        for (Campo campo : campos)
            if (campo.isValor() && !campo.isbusqueda())
                aux.add(campo);
        
        Campo[] res = new Campo[aux.size()];
        for (int i = 0; i < aux.size(); i++)
            res[i] = aux.get(i);
        
        return res;
    }

    public static LinkedList<Campo[]> getSeleccion(Campo[] campos, BasedeDatos BD, String nomb_tabla) {        
        Consulta consulta = new Consulta(nomb_tabla,Consulta.SELECIONAR, campos);        
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
        String res = "CREATE DATABASE \"" + tabla + "\"";
        return res;
    }

    private String getEliminarBD() {
        String res = "DROP DATABASE " + tabla;
        return res;
    }

    private String getActualizar() {
        String res = "UPDATE public." + tabla + " SET " + getParametrizacion();
        return res;
    }

    private String getEliminar() {
        String res = "DELETE FROM public." + tabla + "\n" + getParametrizacion();
        return res;
    }

    private String getAgregar() {
        String res = "INSERT INTO public." + tabla + "(\n" + getCampos() + ")\n" + " VALUES (";
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
        String res, contenido = "(";        
        
        for (Campo obj : campos)
            contenido += obj.getCampoToString() + " , \n";
        
        String llavesPrimarias = "CONSTRAINT " + tabla + "_pkey PRIMARY KEY ( ";
        
        for (Campo campo : campos) 
            if (campo.isLlavePrimaria())
                    llavesPrimarias += campo.getNombre() + " , ";
        
        contenido += llavesPrimarias.substring(0, llavesPrimarias.length() - 3) + " ) ";
        
        //generando consulta
        res = "CREATE TABLE public." + tabla + "\n" + contenido +
            "\n)WITH (OIDS = FALSE) TABLESPACE pg_default;" +
            "\nALTER TABLE public." + tabla + 
            "\nOWNER to postgres;";
        return res;
    }

    private String getEliminarTabla() {
        return "DROP TABLE public." + tabla;
    }

//    boolean respuesta(int estado) {
//        switch(accion){
//            case BD_CREAR:
//                return estado >= 0;
//            default:
//                return estado ==1;
//        }
//    }
    
}
