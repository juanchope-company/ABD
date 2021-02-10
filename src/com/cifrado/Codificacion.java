package com.cifrado;

import java.util.Base64;

/**
 *
 * @author Juanchope
 */
public class Codificacion {
        
/**
 * 
 * @see  Convierte un arreglo de Bytes unicode en un texto plano con formato Base64
 * @param cifrado Arreglo de bytes a convertir a texto plano legible
 * @return Devuelve el texto plano legible en Base64
 * 
 */
    public static String codificar(byte[] cifrado) {
        return Base64.getEncoder().encodeToString(cifrado);
    }
/**
 * 
 * @see Convierte un texto plano con formato Base64 en un arreglo de Bytes unicode
 * @param textocifrado Texto plano con formato Base64
 * @return Arreglo de Bytes unicode
 * 
 */

    public static byte[] decodificar(String textocifrado) {
        return Base64.getDecoder().decode(textocifrado);
    }
}
