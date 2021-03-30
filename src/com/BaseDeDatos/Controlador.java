package com.BaseDeDatos;

import java.util.LinkedList;
import java.util.Scanner;


/**
 *
 * @author Juanchope
 */
public abstract class Controlador {
    
    protected final Tabla tabla;
    protected final BasedeDatos bd;
    
    public Controlador(Tabla tabla){
        this.tabla = tabla;
        this.bd = null;
    }
    
    public Controlador(BasedeDatos bd){
        this.tabla = null;
        this.bd = bd;
    }
    
    protected abstract void crearObjecto();
    
    public abstract void eliminarObject();
    
    public abstract void verObjecto();
    
    public abstract void modificarObjecto();
    
    public boolean ListarObjectos() {
        if (tabla != null) {
            LinkedList<Objeto_Tabla> lu = tabla.getItems();

            for (int i = 0; i < lu.size(); i++)
                System.out.println((i + 1) + ". " + lu.get(i).getMiniDescripcion());

            if (lu.isEmpty())
                System.out.println("No hay filas para listar. ");

            return !lu.isEmpty();
        }else{
            Tabla[] lt = bd.getTablas();
            
            for (int i = 0; i < lt.length; i++)
                System.out.println((i+1) + ". " + lt[i].getNOMBRE_TABLA());
            
            if (lt.length == 0)
                System.out.println("No hay tablas para listar. ");
            
            return lt.length != 0;
        }
    }
    
    public boolean menu(){
        if (tabla != null) {
            String nombre = tabla.getNOMBRE_TABLA();
            System.out.println("*** MENU DE " + nombre + " ***");
            System.out.println("1. Agregar " + nombre);
            System.out.println("2. Borrar " + nombre);
            System.out.println("3. Modificar " + nombre);
            System.out.println("4. Ver " + nombre);
            System.out.println("5. Listar " + nombre);
            System.out.println("6. Salir");

            Scanner lectorInt = new Scanner(System.in);
            System.out.print("Elija una opción del [1 al 6]: ");
            int eleccion = lectorInt.nextInt();

            switch (eleccion) {
                case 1:
                    crearObjecto();
                    break;
                case 2:
                    eliminarObject();
                    break;
                case 3:
                    modificarObjecto();
                    break;
                case 4:
                    verObjecto();
                    break;
                case 5:
                    ListarObjectos();
                    break;
                case 6:
                    System.out.println("gracias por usar este programa");
                    return false;
                default:
                    System.out.println("Elección invalida, porfavor intentelo nuevamente");
                    break;
            }
        }else{
            String nombre = bd.getNOMBRE();
            System.out.println("*** MENU DE BASE DE DATOS " + nombre + " ***");
            System.out.println("1. Agregar tabla");
            System.out.println("2. Eliminar tabla");
            System.out.println("3. Ver tabla");
            System.out.println("4. Listar tabla");
            System.out.println("5. Probar Base de datos");
            System.out.println("6. Salir");

            Scanner lectorInt = new Scanner(System.in);
            System.out.print("Elija una opción del [1 al 6]: ");
            int eleccion = lectorInt.nextInt();

            switch (eleccion) {
                case 1:
                    crearObjecto();
                    break;
                case 2:
                    eliminarObject();
                    break;
                case 3:
                    verObjecto();
                    break;
                case 4:
                    ListarObjectos();
                    break;
                case 5:
                    probarBaseDeDatos();
                    break;
                case 6:
                    System.out.println("gracias por usar este programa");
                    return false;
                default:
                    System.out.println("Elección invalida, porfavor intentelo nuevamente");
                    break;
            }
        }
        Scanner lector = new Scanner(System.in);
        System.out.println("Preciona enter para continuar.");
        lector.nextLine();
        return true;
    }

    private void probarBaseDeDatos() {
        if (bd != null){
            if (bd.probarBasedeDatos())
                System.out.println("Prueba exitosa");
            else
                System.out.println("Prueba fallida");
        }
        else
            System.out.println("Error en base de datos");
    }
}
