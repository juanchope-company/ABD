package com.BaseDeDatos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;

/**
 *
 * @author Juanchope 
 * @version 1.0
 */
public abstract class Tabla{
    protected static final Campo
            ID = new Campo("id", Campo.TIPO_SERIAL, true);
    
    protected final Campo 
            id = new Campo("id", Campo.TIPO_SERIAL, true),
            TODOS_CAMPOS = new Campo("*");
    
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
 * @param nombre_tabla nombre de la tabla
 */
    public Tabla(BasedeDatos BD, Campo[] todosloscampos,String nombre_tabla) {
        this.BD = BD;
        CAMPOS = todosloscampos;
        NOMBRE_TABLA = nombre_tabla;
    }  

    public String getNOMBRE_TABLA() {
        return NOMBRE_TABLA;
    }
    
    protected boolean ActualizarItem(Objeto_Tabla obj){
        if (!obj.existe()){
            System.out.println("ID invalido porfavor intente nuevamente mas tarde.");
            return false;
        }
            
        Campo[] aux = new Campo[getCampos(obj).length + 1];
        int i=0;
        
        for (Campo campo : getCampos(obj)) {
            campo.setParametro(true);
            aux[i] = campo;
            i++;
        }
        
        aux[i] = ID.clone();
        aux[i].setValor(obj.getID());
        Consulta consult = new Consulta(NOMBRE_TABLA, Consulta.ACTUALIZAR, aux);
        
        return BD.Consulta(consult);
    }
    
    protected boolean BorrarItem(Objeto_Tabla obj) {
        boolean res;
        id.setValor(obj.getID());
        Consulta consult = new Consulta(NOMBRE_TABLA, Consulta.ELIMINAR, id);
        
        if ((res =BD.Consulta(consult)))
            System.out.println("item con el ID " + obj.getID() + " borrado de la tabla " + NOMBRE_TABLA);
        else 
            System.out.println("borrar item con el ID " + obj.getID() + " de la tabla " + NOMBRE_TABLA + " falló porfavor intenta nueva mente");
        return res;
    }
    
    protected boolean AgregarItem(Objeto_Tabla obj){
        Campo[] campos = getCampos(obj);
        if (campos == null)
            return false;
        Consulta consult = new Consulta(NOMBRE_TABLA, Consulta.AGREGAR, campos);
        return BD.Consulta(consult);
    }
    
    public Objeto_Tabla getItem(Long id) {
        Objeto_Tabla res = null;
//        String consulta = "SELECT " + getCampos()  +
//            " FROM public." + NOMBRE_TABLA +
//            " where " + ID.getNombre() + " = ? ";
        
        Consulta consult = new Consulta(
            NOMBRE_TABLA,
            Consulta.SELECIONAR, 
            new Campo[]{
                new Campo(this.id.getNombre(), id),
                new Campo("*")
            }
        );
        
        LinkedList<Campo[]> aux = BD.recibirConsultaIndexadas(consult);
        if (aux != null)
            if (aux.size() == 1)
                res = parse(aux.get(0));
        return  res;
    }
    
    protected Objeto_Tabla getItem(Campo campo) {
        Objeto_Tabla res = null;

        campo.setParametro(false);        
        Consulta consult = new Consulta(
            NOMBRE_TABLA, 
            Consulta.SELECIONAR,
            new Campo[]{
                TODOS_CAMPOS,
                campo
            }
        );
        
        LinkedList<Campo[]> aux = BD.recibirConsultaIndexadas(consult);
        if (aux != null)
            if (aux.size() != 0)
                res = parse(aux.getLast());
        
        return  res;
    }
    
    
    
    public LinkedList<Objeto_Tabla> getItems() {
        Consulta consult = new Consulta(NOMBRE_TABLA, Consulta.SELECIONAR, new Campo("*"));
        
        //recuper los datos en la variable datos
        LinkedList<Campo[]> datos = BD.recibirConsultaIndexadas(consult);
        
        //crea una variable res que sera el arreglo de usuarios a regresar
        LinkedList<Objeto_Tabla> res = new LinkedList<>();
        
        if (datos != null)
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
     
    protected static byte[] convertirObjectToByteArray(Object obj) {
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
        return getItem(obj.getID()) != null;
    }
    
    protected LinkedList<Objeto_Tabla> getItems(Campo[] items) {
        if (items == null)
            return null;
        
        Campo[] campos = new Campo[items.length + 1];
        System.arraycopy(items, 0, campos, 0, items.length);
        campos[items.length] = new Campo("*");
                
        Consulta consult = new Consulta(NOMBRE_TABLA, Consulta.SELECIONAR, campos);
        LinkedList<Campo[]> aux = BD.recibirConsultaIndexadas(consult);        
        return camposToObjectTabla(aux);
    }
    
    public LinkedList<Objeto_Tabla> buscatItem(Campo... campos){
        Campo[] camposs = new Campo[campos.length + 1];
        System.arraycopy(campos, 0, camposs, 0, campos.length);
        camposs[campos.length] = new Campo("*");
                
        Consulta consult = new Consulta(NOMBRE_TABLA,Consulta.SELECIONAR , campos);  
        LinkedList<Campo[]> aux = BD.recibirConsultaIndexadas(consult);
        return camposToObjectTabla(aux);
    }
    
    abstract public Objeto_Tabla parse(Campo[] obj);
    
    protected LinkedList<Objeto_Tabla> getItem(Campo... campos){   
        Campo[] aux = new Campo[campos.length + 1];
        int i = 0;
        
        for (;i < campos.length; i++)
            aux[i] = campos[i];
        aux[i] = new Campo("*");
        return camposToObjectTabla(Consulta.getSeleccion(aux, BD, NOMBRE_TABLA));
    }

    protected Campo getCampo(String nombre){
        return getCampo(CAMPOS,nombre);
    }
    
    protected Campo getCampo(Campo campo){
        return getCampo(CAMPOS,campo.getNombre());
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

    private LinkedList<Objeto_Tabla> camposToObjectTabla(LinkedList<Campo[]> aux) {
        if (aux == null)
            return new LinkedList<>();
        
        LinkedList<Objeto_Tabla> res = new LinkedList<>();
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
    
    public abstract Campo[] getCampos(Objeto_Tabla obj);
}
