package com.Cifrado;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author ASUS
 */
public class Cifrado {
    
    private static final int bytesHash = 512;
    
    private static String GenerarHash(String texto){
        try {
            MessageDigest digestordeMensages = MessageDigest.getInstance("SHA-" + bytesHash);
            return  codificar(digestordeMensages.digest(texto.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.getCause().toString());
        }
        return null;
    }
    
    public static String CifradoAES(String texto, String contraseña){
        if (contraseña.getBytes().length < 16)
            return null;
        String res = "";
        try {
            // Generamos una clave de 128 bits adecuada para CifradoAES
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            //SecretKey key = keyGenerator.generateKey
            SecretKey key;
            
            // Alternativamente, una clave que queramos que tenga al menos 16 bytes
            // y nos quedamos con los bytes 0 a 15
            key = new SecretKeySpec(contraseña.getBytes(),  0, 16, "AES");
            
            // Ver como se puede guardar esta clave en un fichero y recuperarla
            // posteriormente en la clase RSAAsymetricCrypto.java
            
            // Texto a encriptar
            //String texto = "Este es el texto que queremos encriptar";
            
            // Se obtiene un cifrador CifradoAES
            Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
            
            // Se inicializa para encriptacion y se encripta el texto,
            // que debemos pasar como bytes.
            aes.init(Cipher.ENCRYPT_MODE, key);
            byte[] encriptado = aes.doFinal(texto.getBytes());
            
            //texto encriptado en base 64
            res = codificar(encriptado);
            System.out.println(res);
            
            /*
            // Se escribe byte a byte en hexadecimal el texto
            // encriptado para ver su pinta.
            for (byte b : encriptado) {
                System.out.print(Integer.toHexString(0xFF & b));
            }
            System.out.println();//*/
            
            // Se iniciliza el cifrador para desencriptar, con la
            // misma clave y se desencripta
            aes.init(Cipher.DECRYPT_MODE, key);
            byte[] desencriptado = aes.doFinal(encriptado);
            
            // Texto obtenido, igual al original.
            System.out.println(new String(desencriptado));
            System.out.println(new String(desencriptado).equals(texto));
            if (new String(desencriptado).equals(texto))
                return res;
            
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(Cifrado.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private static String DecifradoAES(String texto, String contraseña){
        if (contraseña.getBytes().length != 16){
            System.out.println("contraseña tiene un tamaño diferente a 16");
            return null;
        }
        String res = "";
        try {
            // Generamos una clave de 128 bits adecuada para CifradoAES
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            //SecretKey key = keyGenerator.generateKey
            SecretKey key;
            
            // Alternativamente, una clave que queramos que tenga al menos 16 bytes
            // y nos quedamos con los bytes 0 a 15
            key = new SecretKeySpec(contraseña.getBytes(),  0, 16, "AES");
            
            // Ver como se puede guardar esta clave en un fichero y recuperarla
            // posteriormente en la clase RSAAsymetricCrypto.java
            
            // Texto a encriptar
            //String texto = "Este es el texto que queremos encriptar";
            
            // Se obtiene un cifrador CifradoAES
            Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
            
            // Se inicializa para encriptacion y se encripta el texto,
            // que debemos pasar como bytes.
            aes.init(Cipher.ENCRYPT_MODE, key);
            byte[] encriptado = aes.doFinal(texto.getBytes());
            
            //texto encriptado en base 64
            res = codificar(encriptado);
            System.out.println(res);
            
            /*
            // Se escribe byte a byte en hexadecimal el texto
            // encriptado para ver su pinta.
            for (byte b : encriptado) {
                System.out.print(Integer.toHexString(0xFF & b));
            }
            System.out.println();//*/
            
            // Se iniciliza el cifrador para desencriptar, con la
            // misma clave y se desencripta
            aes.init(Cipher.DECRYPT_MODE, key);
            byte[] desencriptado = aes.doFinal(encriptado);
            
            // Texto obtenido, igual al original.
            System.out.println(new String(desencriptado));
            System.out.println(new String(desencriptado).equals(texto));
            if (new String(desencriptado).equals(texto))
                return res;
            
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(Cifrado.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private  static String codificar(byte[] cifrado) {
        return Base64.getEncoder().encodeToString(cifrado);
    }

    private  static byte[] decodificar(String textocifrado) {
        return Base64.getDecoder().decode(textocifrado);
    }
}
