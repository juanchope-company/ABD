package com.BaseDeDatos;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author Juanchope
 * @version  1.0
 */

public abstract class ObjectoTabla implements Serializable{
    
    protected final Tabla CONSULTAS;
    protected Campo ID = Tabla.ID;
    
    public final String
            MENSAGE = "mensage" ,
            RESPUESTA_BOOLEANA = "is",
            MENSAGE_VALIDO = "Validado correctamente";
    
    public  ObjectoTabla(){
        this.CONSULTAS = null;
    }

    public ObjectoTabla(Tabla Consultas) {
        this.CONSULTAS = Consultas;
        this.ID.setValor((Long) 0L);
    }
    
    public ObjectoTabla(Tabla Consultas, Long id) {
        this.CONSULTAS = Consultas;
        this.ID.setValor(id);
    }
    
    public boolean esValido() {
        return CONSULTAS.ValidarItem(this);
    }
    
    public Long getID() {
        return (Long) ID.getValor();
    }
    
    public void setID(Long ID){
        this.ID.setValor(ID);
    }
    
    public boolean AgregarItem(){
        return CONSULTAS.AgregarItem(this);
    }
    
    public boolean ActualizarItem(){
        return CONSULTAS.ActualizarItem(this);
    }
    
    public boolean BorrarItem(){
        return CONSULTAS.BorrarItem(this);
    }
    
    protected byte[] convertirObjectToByteArray(Object obj) {
        byte[] res = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            res = bos.toByteArray();
        }   catch(IOException ex){
            System.out.println(ex.getMessage());   
        }
        return res;
    }
    
    public boolean existe() {
        return CONSULTAS.getItem(ID) != null;
    }
    
    public abstract HashMap<String, Object> validar();
    
    public abstract String getMiniDescripcion();
}
