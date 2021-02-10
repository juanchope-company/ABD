package com.cifrado;

import static com.cifrado.Codificacion.codificar;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 *
 * @author Juanchope
 * @version 1.0
 * 
 */
public class Hash {
    
/**
 * 
 * @see Metodo para generar el hash unico de la contraseña 
 * @param texto Texto a generar el hash
 * @return Devuelve el hash del mensaje
 * 2048 bit keys should be secure until 2030 - https://web.archive.org/web/20170417095741/https://www.emc.com/emc-plus/rsa-labs/historical/twirl-and-rsa-key-size.htm
 */
    public static String HashSHA512(String texto){//256, 512
        try {
            //Ingresa el mensaje en un objecto de cifrado de mensajes
            MessageDigest digestordeMensages = MessageDigest.getInstance("SHA-" + 512);
            
            //cifra os bytes del texto en formato utf_8
            return  codificar(digestordeMensages.digest(texto.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
    
    private static final int            SECURE_TOKEN_LENGTH = 32;
    private static final String         KEY          = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom   RANDOM              = new SecureRandom();

/**
 * Generate the next secure random token in the series.
 * @return Devuelve un nuevo token
 */
    public static String GenerarToken() {    
        char[] symbols = KEY.toCharArray(),
        buf = new char[SECURE_TOKEN_LENGTH];
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[RANDOM.nextInt(symbols.length)];
        return new  String(buf);
    }
}

