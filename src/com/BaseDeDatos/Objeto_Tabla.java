package com.BaseDeDatos;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Juanchope 
 * @version 1.0
 */
public abstract class Objeto_Tabla{
    
    protected static final String 
            ID = "id";
    
    protected final String 
            NOMBRE_TABLA;
    
    public final BasedeDatos 
            BD;
    
    protected Campo[]
            campos;
    
/**
 * @param BD Consulta a la base de datos
 * @param todosloscampos Campos de la tabla con sus tipos 
 * @param nombre_tabla nombre de la tabla
 */
    public Objeto_Tabla(BasedeDatos BD, Campo[] todosloscampos,String nombre_tabla) {
        this.BD = BD;
        campos = todosloscampos;
        NOMBRE_TABLA = nombre_tabla;
    }  

    public String getNOMBRE_TABLA() {
        return NOMBRE_TABLA;
    }
    
    protected abstract boolean ActualizarItem();
    
    protected abstract boolean BorrarItem();
    
    protected abstract boolean AgregarItem();
    
    protected abstract Objeto_Tabla parse(Campo[] campos);
    
//    protected Objeto_Tabla getItem(Campo campo) {
//        Objeto_Tabla res = null;
//
//        campo.setParametro(true);        
//        Consulta consult = new Consulta(
//            NOMBRE_TABLA, 
//            Consulta.SELECIONAR,
//            Campo.TODOS_CAMPOS,
//            campo
//        );
//        
//        LinkedList<Campo[]> aux = BD.recibirConsultaIndexadas(consult);
//        if (aux != null)
//            if (!aux.isEmpty())
//                res = parse(aux.getLast());
//        
//        return  res;
//    }
    
    public LinkedList<Objeto_Tabla> getItems() {
        return gets(BD.recibirConsultaIndexadas(new Consulta(
                NOMBRE_TABLA, 
                Consulta.SELECIONAR, 
                Campo.TODOS_CAMPOS
        )));
    }
    
    protected  boolean CrearTabla(){
        return BD.Consulta(new Consulta(NOMBRE_TABLA, Consulta.TABLA_CREAR, campos));
    }
    
    public boolean BorrarTabla() {
        return BD.Consulta(new Consulta(NOMBRE_TABLA, Consulta.TABLA_ELIMINAR));
    }
    
    public boolean ProbarTabla(){
        //consulta si la tabla existe
        Consulta consult = new Consulta(
                NOMBRE_TABLA, 
                Consulta.SELECIONAR
        );
        boolean res = BD.Consulta(consult);
        if (!res){
            BorrarTabla();
            CrearTabla();
            res = BD.Consulta(consult);
        }                
        System.out.println("\t* verificación de la tabla " + NOMBRE_TABLA + (res ? " exitosa" : " falló"));
        return res;
    }
     
    public static byte[] convertirObjectToByteArray(Object obj) {
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
    
    protected static Object convertFromBytes(byte[] bytes){
        Object res = null;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInput in = new ObjectInputStream(bis)) {
            res = in.readObject();
        } catch (IOException | ClassNotFoundException | NullPointerException ex) {
            System.out.println(ex.getMessage());
        }
        return res;
    }
    
    public boolean ValidarItem(Objeto_Tabla obj){
        return getItem(getCampo(ID)) != null;
    }
    
    protected LinkedList<Objeto_Tabla> getItems(Campo[] items) {
        if (items == null)
            return null;
        
        Campo[] campos = new Campo[items.length + 1];
        System.arraycopy(items, 0, campos, 0, items.length);
        campos[items.length] = new Campo("*");
                
        Consulta consult = new Consulta(NOMBRE_TABLA, Consulta.SELECIONAR, campos);
        LinkedList<Campo[]> aux = BD.recibirConsultaIndexadas(consult);        
        return gets(aux);
    }
    
    public LinkedList<Objeto_Tabla> buscatItem(Campo... campos){
        Campo[] camposs = new Campo[campos.length + 1];
        System.arraycopy(campos, 0, camposs, 0, campos.length);
        camposs[campos.length] = new Campo("*");
                
        Consulta consult = new Consulta(NOMBRE_TABLA,Consulta.SELECIONAR , campos);  
        LinkedList<Campo[]> aux = BD.recibirConsultaIndexadas(consult);
        return gets(aux);
    }
    
    
    protected LinkedList<Objeto_Tabla> getItem(Campo... campos){   
        Campo[] aux = new Campo[campos.length + 1];
        int i = 0;
        
        for (;i < campos.length; i++)
            aux[i] = campos[i];
        aux[i] = Campo.TODOS_CAMPOS;
        return gets(BD.recibirConsultaIndexadas(new Consulta(
                NOMBRE_TABLA,
                Consulta.SELECIONAR,
                aux
        )));
    }
    
    protected Campo getCampo(String nombre){
        for (Campo campo : campos) 
            if (campo.getNombre().equals(nombre))
                return campo;
        return null;
    }
    
//    protected LinkedList<String> ConvertirArrayToArrayListString(String[] val) {
//        LinkedList<String> res = new LinkedList<>();
//        
//        res.addAll(Arrays.asList(val));
//        
//        return res;
//    }

//    protected LinkedList<LocalDate> ConvertirArrayToArrayListLocalDate(String[] val) {
//        LinkedList<LocalDate> res = new LinkedList<>();
//        
//        for (String obj : val)
//            res.add(LocalDate.parse(obj));
//        
//        return res;
//    }    
    
    public String getMiniDescripcion(){
        return "falta por implementar la mini descripcion del objecto tabla en cuestion";
    }
    
    public HashMap<String, Object> Validar(){
        return new HashMap<>();
    }

    private boolean existe() {
        return BD.Consulta(new Consulta(
                NOMBRE_TABLA,
                Consulta.SELECIONAR,
                getCampo(ID)
        ));
    }

    private LinkedList<Objeto_Tabla> gets(LinkedList<Campo[]> datos) {
        LinkedList<Objeto_Tabla> res = new LinkedList<>();
        
        if (datos != null)
            datos.forEach((obj) -> { 
                res.add(parse(obj));
            });
        return res;
    }
}