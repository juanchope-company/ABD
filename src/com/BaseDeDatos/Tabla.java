package com.BaseDeDatos;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;

/**
 *
 * @author Juanchope 
 * @version 1.0
 */
public abstract class Tabla implements Serializable{
    
    protected static final Campo 
            ID = new Campo("id", Campo.TIPO_SERIAL, true);
    
    private final String 
            NOMBRE_TABLA;
    
    public final BasedeDatos 
            BD;
    
    protected final Campo[]
            CAMPOS;
    
/**
 * bytea    -> byte[]
 * text     -> String
 * bigint   -> Long
 * text[]   -> String[]
 * NOT NULL -> no nullo
 * @param BD Consulta a la base de datos
 * @param todosloscampos Campos de la tabla con sus tipos 
 * @param tabla nombre de la tabla
 */
    public Tabla(BasedeDatos BD, Campo[] todosloscampos,String tabla) {
        this.BD = BD;
        CAMPOS = todosloscampos;
        NOMBRE_TABLA = tabla;
    }  

    public String getNOMBRE_TABLA() {
        return NOMBRE_TABLA;
    }
    
    protected boolean ActualizarItem(ObjectoTabla obj){
        if (!obj.existe()){
            System.out.println("ID invalido porfavor intente nuevamente mas tarde.");
            return false;
        }
            
        Campo[] campos = getCampos(obj);
        
        Consulta consult = new Consulta(NOMBRE_TABLA, campos, new Campo(ID.getNombre(),obj.getID()));
        
        return BD.Consulta(consult);
    }
    
    protected boolean BorrarItem(ObjectoTabla obj) {
        boolean res;
        ID.setValor(obj.getID());
        Consulta consult = new Consulta(NOMBRE_TABLA, Consulta.ELIMINAR, ID);
        
        if ((res =BD.Consulta(consult)))
            System.out.println("item con el ID " + obj.getID() + " borrado de la tabla " + NOMBRE_TABLA);
        else 
            System.out.println("borrar item con el ID " + obj.getID() + " de la tabla " + NOMBRE_TABLA + " falló porfavor intenta nueva mente");
        return res;
    }
    
    protected boolean AgregarItem(ObjectoTabla obj){
        Campo[] campos = getCampos(obj);
        if (campos == null)
            return false;
        Consulta consult = new Consulta(NOMBRE_TABLA, Consulta.AGREGAR, campos);
        return BD.Consulta(consult);
    }
    
    public ObjectoTabla getItem(Long id) {
        ObjectoTabla res = null;
//        String consulta = "SELECT " + getCampos()  +
//            " FROM public." + NOMBRE_TABLA +
//            " where " + ID.getNombre() + " = ? ";
        
        Consulta consult = new Consulta(
            NOMBRE_TABLA,
            Consulta.SELECIONAR, 
            new Campo[]{
                new Campo(ID.getNombre(), id),
                new Campo("*")
            }
        );
        
        LinkedList<Campo[]> aux = BD.recibirConsultaIndexadas(consult);
        if (aux != null)
            if (aux.size() == 1)
                res = parse(aux.get(0));
        return  res;
    }
    
    protected ObjectoTabla getItem(Campo campo) {
        ObjectoTabla res = null;
//        String consulta = "SELECT " + getCampos() +
//            " FROM public." + NOMBRE_TABLA +
//            " where " + columna + " = ? ";

        Consulta consult = new Consulta(
            NOMBRE_TABLA, 
            Consulta.SELECIONAR,
            new Campo[]{
                campo,
                new Campo("*")
            }
        );
        LinkedList<Campo[]> aux = BD.recibirConsultaIndexadas(consult);
        if (aux != null)
            res = parse(aux.getLast());
        return  res;
    }
    
    
    
    public LinkedList<ObjectoTabla> getItems() {
        Consulta consult = new Consulta(NOMBRE_TABLA, Consulta.SELECIONAR, new Campo("*"));
        
        //recuper los datos en la variable datos
        LinkedList<Campo[]> datos = BD.recibirConsultaIndexadas(consult);
        
        if (datos == null)
            return null;
        
        //crea una variable res que sera el arreglo de usuarios a regresar
        LinkedList<ObjectoTabla> res = new LinkedList<>();
        
        datos.forEach((obj) -> { 
            res.add(parse(obj));
        });
        
        return res;
    }
    
    
    protected  boolean CrearTabla(){
        System.out.println("Generando tabla " + NOMBRE_TABLA);
        return BD.Consulta(new Consulta(NOMBRE_TABLA, Consulta.TABLA_CREAR, CAMPOS));
    }
    
    public boolean BorrarTabla() {
        System.out.println("Borrar Tabla " + NOMBRE_TABLA);
        return BD.Consulta(new Consulta(NOMBRE_TABLA, Consulta.TABLA_ELIMINAR));
    }
    
    public boolean ProbarTabla(){
        //consulta si la tabla existe
        System.out.println("\t* verificando tabla " + NOMBRE_TABLA);
        Consulta consult = new Consulta(NOMBRE_TABLA, Consulta.SELECIONAR, CAMPOS);
        boolean res = BD.recibirConsultaIndexadas(consult) != null;
        if (!res){
            BorrarTabla();
            CrearTabla();
        }        
        res = BD.recibirConsultaIndexadas(consult) != null;
        System.out.println("\t* verificación de la tabla " + NOMBRE_TABLA + (res ? " exitosa" : " falló"));
        return res;
    }
    

//    private String getCampos(String sufijo) {
//        String res = "";
//        
//        for (Campo obj : CAMPOS)
//            res += sufijo + obj.getNombre();
//        
//        return res.substring(sufijo.length());
//    }
    
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
    
    public boolean ValidarItem(ObjectoTabla obj){
        return getItem(obj.getID()) != null;
    }
    
    protected LinkedList<ObjectoTabla> getItems(Campo[] items) {
        if (items == null)
            return null;
        
        Campo[] campos = new Campo[items.length + 1];
        System.arraycopy(items, 0, campos, 0, items.length);
        campos[items.length] = new Campo("*");
                
        Consulta consult = new Consulta(NOMBRE_TABLA, Consulta.SELECIONAR, campos);
        LinkedList<Campo[]> aux = BD.recibirConsultaIndexadas(consult);        
        return camposToObjectTabla(aux);
    }
    
    public LinkedList<ObjectoTabla> buscatItem(Campo... campos){
        Campo[] camposs = new Campo[campos.length + 1];
        System.arraycopy(campos, 0, camposs, 0, campos.length);
        camposs[campos.length] = new Campo("*");
                
        Consulta consult = new Consulta(NOMBRE_TABLA,Consulta.SELECIONAR , campos);  
        LinkedList<Campo[]> aux = BD.recibirConsultaIndexadas(consult);
        return camposToObjectTabla(aux);
    }
    
    abstract public ObjectoTabla parse(Campo[] obj);
    
    protected LinkedList<ObjectoTabla> getItem(Campo... campos){        
        return camposToObjectTabla(Consulta.getSeleccion(campos, BD));
    }

    protected Campo getCampo(String nombre){
        return getCampo(CAMPOS,nombre);
    }
    
    protected Campo getCampo(Campo[] campos, String nombre){
        for (Campo campo : campos) 
            if (campo.getNombre().equals(nombre))
                return campo;
        return null;
    }
//    
//    private String getCampos() {
//        return  getCampos(", ") + ", " + ID.getNombre();
//    }

    private LinkedList<ObjectoTabla> camposToObjectTabla(LinkedList<Campo[]> aux) {
        if (aux == null)
            return new LinkedList<>();
        
        LinkedList<ObjectoTabla> res = new LinkedList<>();
        aux.forEach((obj) -> {
            res.add(parse(obj));
        });
        
        return res;
    }
    
    
    protected LinkedList<String> ConvertirArrayToArrayListString(String[] val) {
        LinkedList<String> res = new LinkedList<>();
        
        res.addAll(Arrays.asList(val));
        
        return res;
    }

    protected LinkedList<LocalDate> ConvertirArrayToArrayListLocalDate(String[] val) {
        LinkedList<LocalDate> res = new LinkedList<>();
        
        for (String obj : val)
            res.add(LocalDate.parse(obj));
        
        return res;
    }    
//    
//    protected LinkedList<LocalDateTime> ConvertirArrayToArrayListLocalDateTime(String[] val) {
//        LinkedList<LocalDateTime> res = new LinkedList<>();
//        
//        for (String obj : val)
//            res.add(LocalDateTime.parse(obj));
//        
//        return res;
//    }
    
    public abstract Campo[] getCampos(ObjectoTabla obj);
}
