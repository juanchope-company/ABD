package com.BaseDeDatos;

import java.util.LinkedList;
import java.util.Scanner;


/**
 *
 * @author Juanchope
 */
public abstract class Controlador {
    
    private static Tabla tabla;
    private static BasedeDatos bd;
    
    public Controlador(Tabla tabla, BasedeDatos bd){
        this.tabla = tabla;
        this.bd = bd;
    }
    
    public abstract void crearObjecto();
    
    public abstract void eliminarObject();
    
    public abstract void verObjecto();
    
    public abstract void modificarObjecto();
    
    public boolean ListarObjectos() {
        LinkedList<ObjectoTabla> lu = tabla.getItems();
        
        for (int i = 0; i < lu.size(); i++) {
            System.out.println((i + 1) + ". " + lu.get(i).getMiniDescripcion());
        }
        
        if (lu.isEmpty())
            System.out.println("No hay filas para listar. ");
        
        return !lu.isEmpty();
    }
    
    public boolean menu(){
        System.out.println("*** MENÚ DE USUARIO ***");
        System.out.println("1. Agregar Usuario");
        System.out.println("2. Borrar Usuario");
        System.out.println("3. Modificar Usuario");
        System.out.println("4. Ver Usuario");
        System.out.println("5. Listar Usuarios");
        System.out.println("6. Probar Base de datos");
        System.out.println("7. Salir");

        Scanner lectorInt = new Scanner(System.in);
        System.out.print("Elija una opción del [1 al 7]:");
        int eleccion = lectorInt.nextInt();

        if (eleccion == 1)
            crearObjecto();
        else if (eleccion == 2)
            eliminarObject();
        else if (eleccion == 3)
            modificarObjecto();
        else if (eleccion == 4)
            verObjecto();
        else if (eleccion == 5)
            ListarObjectos();
        else if (eleccion == 6){
            System.out.println("Probando base de datos");
            System.out.println("Prueba exitsa?: " + bd.probarBasedeDatos());
        }else if (eleccion == 7){
            System.out.println("gracias por usar este programa");
            return false;
        }else
            System.out.println("Elección invalida, porfavor intentelo nuevamente");  
        return true;
    }
}
