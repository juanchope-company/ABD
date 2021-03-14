package com.BaseDeDatos;

import java.time.LocalDate;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import org.postgresql.jdbc.PgArray;

/**
 *
 * @author Juanchope
 * @code Esta clase se encarga de manejar la conexion a la base de datos
 *  
 *  nombreBasedeDatos: nombre de la base de datos que desea acceder
 *  URI: Localizacion de la base de datos formateada con el controlador de la base de datos
 *  ejemplo de conrtroladores como 
 *  jdbc:postgresql para postgres
 *  jdbc:mysql para mysql
 *  segido de su direccion de enlace
 *  normalmete en el caso de xampp con el uso de mysql es de la siguiente manera
 *  jdbc:mysql://localhost:3306/
 *  donde localhost es la ip de la base de datos publica o en su defecto localhost si es privada
 *  y :3306 el puerto es el que genera la base dedatos y es el unico camino de conexion a dicha 
 *  base de datos
 *  y por ejempo en postgres seria de la siguiente maner
 *  jdbc:postgresql://localhost:5432/
 *  con ip 190.60.4.247 la cual es de una ip publica
 *  al final la URI se une con el nombreBasedeDatos la base de datos para generar su direccion de conexion
 *  jdbc:mysql://localhost:3306/tienda1
 *  Controlador Indica que tipo de base de datos es y como debe ser su comunicacon con dicha base de datos
 *  en el caso de mysql es: com.mysql.jdbc.Driver
 *  en el caso de postgres es: org.postgresql.Driver
 *  usuario y contraseña son las de la base de datos y con las que va a 
 *  conecion con dicha base de datos
 *  contraseña ip publica: Coonube140819
 * 
 * 
 * uri = "jdbc:postgresql://localhost:5432/",
            controlador = "org.postgresql.Driver",
            usuario = "postgres",
            clave = "2711"; 
 * 
 * Controlador
 * "com.mysql.jdbc.Driver", "org.apache.derby.jdbc.EmbeddedDriver"
 * USUARIO
 * "root", "APP"
 * CLAVE
 * "", "1234"
 * URL jdbc:derby:D:\\Mis Documentos\\Juan David G V\\NetBeansProjects\\Prueba1\\BD" 
 * "jdbc:mysql://localhost:3306/tienda1"
 * "jdbc:derby:memory:myDB;create=true" private String CONTROLADOR, USUARIO, CLAVE, URL;
 */

public abstract class BasedeDatos implements Serializable{
    
    protected Connection 
            con;
    
    protected String
            nombre, URI, 
            CONTROLADOR, CLAVE,
            USUARIO;     
    
    protected static final String
            /*POSTGRES*/
            POSGREST_CONTROLADOR = "org.postgresql.Driver",
            POSGREST_URI = "jdbc:postgresql:";
    
    public BasedeDatos(String nombre) {
        this.nombre = nombre;
    }
    
    public String getNOMBRE() {
        return nombre;
    }

    public abstract Tabla[] getTablas();

    /**
     *
     * @param controlador
     * @param url
     * @param usuario
     * @param clave
     * @see Eestablece la conexion a la base de datos
     * @return Devuelve el estado de la conección
     *
     */
    protected boolean conectar(String controlador, String url, String usuario, String clave) {
        try{    
            //Ingresa el controlador de base de datos
            Class.forName(controlador);
            
            //Ingresa los datos de coneccion
            con = DriverManager.getConnection(url,usuario,clave);
            if (con == null){
                System.out.println("Ha ocurrido un error al conectar");
                return false;
            }
//            System.out.println("Base de datos conectada correctamente");
            return true;
        }catch (ClassNotFoundException | SQLException e){
            System.out.println("error. "+ e + e.getMessage());
        }
        return false;
    }
    
    /**
     *
     * @see Rompe la conexion a la base de datos 
     * @return Devulve si se desconectó correctamente
     * 
    */
    protected boolean desconectar(){
        //try: captura los errores en tiempo de ejecucion
        try {
            //desconecta la conecion
            con.close();
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }
    
    
    /**
     * @param consult
     * @see Ejecuta una consulta
     * @return Devuelve si fué finalizada correctamente
     */    
    public boolean Consulta(Consulta consult){
        conectar(nombre);
        
        Campo[] parametros = consult.getParametros();
        int estado = 0;
        try{
            //Prepara la consulta SQL
            PreparedStatement consulta = con.prepareStatement(consult.getConsulta());
            
            //Agrega los parametros a la consulta si los hay
            if (parametros != null)
                for (int i = 0; i < parametros.length; i++){
                    if (parametros[i].getValor() instanceof byte[])
                        consulta.setBytes(i+1, (byte[]) parametros[i].getValor());
                    else
                        consulta.setObject(i+1, parametros[i].getValor());
                }
            
            //Ejecuta la Consulta SQL Devolviendo un numero de estado
            estado = consulta.executeUpdate();
            desconectar();
            return consult.respuesta(estado);
        }catch(SQLException ex){            
            if (ex.hashCode() == 89387388)
                System.out.println("Filas repetidas.");
            else
                System.out.println("Consulta falló.\n" + ex.getMessage());
        }
        System.out.println("Estado: " + estado);
        desconectar();
        return false;
    }
      
/**
 * 
 * @see Realiza consultas Select
 * @param consult La consulta SQL
 * @return Una tabla de objecto que contiene filas y columnas
 * 
 */
    
    public LinkedList<Campo[]> recibirConsultaIndexadas(Consulta consult) {
        conectar(nombre);

        //Construccion de la tabla de salida
        LinkedList<Campo[]> res = new LinkedList<>();
        Campo[] parametros = consult.getParametros();
        
        try{
            //Prepara la consulta SQL
            PreparedStatement consulta = con.prepareStatement(consult.getConsulta());
            
            //Si hay parametros los agrega
            if (parametros != null)
                for (int i = 0; i < parametros.length; i++){
                    /**
                     * dependiendo del tipo de dato los agrega directamente estableciendolo
                     * no es necesario hacerlo con cada uno por que el 
                     * setObject valida cualquier tipo de dato 
                     * pero yo lo incluí a modo de prueba de errores
                     */
                    if (parametros[i].getValor() instanceof LocalDate)
                        consulta.setDate(i+1, Date.valueOf(parametros[i].getValor().toString()));
                    else if (parametros[i].getValor() instanceof Integer)
                        consulta.setInt(i+1, (int) parametros[i].getValor());
                    else if (parametros[i].getValor() instanceof  Long)
                        consulta.setLong(i+1, (Long) parametros[i].getValor());
                    else if (parametros[i].getValor() instanceof String)
                        consulta.setString(i+1, (String) parametros[i].getValor());
                    else if (parametros[i].getValor() instanceof byte[])
                        consulta.setBlob(i+1, (Blob) parametros[i].getValor());
                    else
                        consulta.setObject(i, parametros[i].getValor());
                }
            
            //Ejecuta la sentencia SQL y devuelve un ResulSet
            ResultSet r = consulta.executeQuery();
            
            //Pasamos por todas las filas del resulset para capturar sus datos
            while(r.next()){
                //Capturamos la fila completa en un LinkedLis
                Campo[] campos = new Campo[r.getMetaData().getColumnCount()];
                
                for (int i = 1;i <= r.getMetaData().getColumnCount();i++){
                    if (r.getObject(i) instanceof PgArray)
                        campos[i-1] = new Campo(r.getMetaData().getColumnName(i),(Object[]) r.getArray(i).getArray());
                    else
                        campos[i-1] = new Campo(r.getMetaData().getColumnName(i),r.getObject(i));
                }
                
                //Agregamos el anterior arreglo a la tabla que devolveremos
                res.add(campos);
            }
            desconectar();
            return res;
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        desconectar();
        return null;
    }
    
    protected abstract boolean conectar(String bd);
    
    public boolean  conectar(){
        return conectar(nombre);
    }
    
    public boolean crearBasedeDatos() {
        System.out.println("\t* Creando base de datos: " + nombre );
        String temp = nombre;
        
        System.out.print("\t* ");
        conectar("");
        
        System.out.print("\t* ");
        Consulta consult = new Consulta(temp, Consulta.BD_CREAR);
        boolean res = Consulta(consult);
        
        System.out.print("\t* ");
        conectar(temp);
        System.out.println("\t* "+(res ? "Base de datos creada correctamente." : "Base de datos no creada intente nuevamente."));
        return res;
    }
    
    protected boolean probarBasedeDatos(){
        Tabla[] tablas = getTablas();
        System.out.println("*** inicio de la prueba de coexion ***");
        System.out.println("\t* Conectando con base de datos");
        boolean operacion = true;
        
        System.out.print("\t* ");
        if (!conectar())
            if (!crearBasedeDatos())
                return false;
        else
            System.out.println("\t* Conectado correctamente");
        
        System.out.println("\t* Verificando tablas");
        for (int i = 0; i < tablas.length ; i++){
            System.out.println("\t* verificando tablas [" + (i+1) + "/" + tablas.length + "]");
            if (!tablas[i].ProbarTabla())
                operacion = false;
        }
        System.out.println("\t* tablas verificadas");
        
        if (!operacion)
            System.out.println("\t* Verificación de tablas falló");
        
        System.out.print("\t* ");
        if (conectar() && operacion){
            System.out.println("\t**************************");
            System.out.println("\t* Prueba sactisfactoria. *");
            System.out.println("\t**************************");
            System.out.println("*** fin de la prueba de conexion ***");
            return true;
        }
        System.out.println("\t*******************");
        System.out.println("\t* Prueba fallida. *");
        System.out.println("\t*******************");
        System.out.println("*** fin de la prueba de conexion ***");
        return false;
    }
    
    protected boolean borrarBasedeDatos() {
        Consulta consult = new Consulta(nombre, Consulta.BD_ELIMINAR);
        return Consulta(consult);
    }
}
