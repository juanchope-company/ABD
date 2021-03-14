package com.BaseDeDatos;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author Juanchope
 * @version  1.0
 */

public abstract class Objeto_Tabla implements Serializable{
    
    protected final Tabla TABLA;
    protected Campo ID =  new Campo("id", Campo.TIPO_SERIAL, true);
    
    public final String
            MENSAGE = "mensage" ,
            RESPUESTA_BOOLEANA = "is",
            MENSAGE_VALIDO = "Validado correctamente";

    public Objeto_Tabla(Tabla Consultas) {
        this.TABLA = Consultas;
        this.ID.setValor((Long) 0L);
    }
    
    public Objeto_Tabla(Tabla Consultas, Long id) {
        this.TABLA = Consultas;
        this.ID.setValor(id);
    }
    
    public boolean esValido() {
        return TABLA.ValidarItem(this);
    }
    
    public Long getID() {
        return (Long) ID.getValor();
    }
    
    public void setID(Long ID){
        this.ID.setValor(ID);
    }
    
    public boolean AgregarItem(){
        return TABLA.AgregarItem(this);
    }
    
    public boolean ActualizarItem(){
        return TABLA.ActualizarItem(this);
    }
    
    public boolean BorrarItem(){
        return TABLA.BorrarItem(this);
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
        return TABLA.getItem(ID) != null;
    }
    
//    public abstract HashMap<String, Object> validar();
    
    public abstract String getMiniDescripcion();
}
