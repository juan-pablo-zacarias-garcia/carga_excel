/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carga_excel.clases;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public class Alg_enc {
    
    public byte [] encript(String texto, String llave16b) throws Exception {
        // Generamos una clave de 128 bits adecuada para AES
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        Key key = keyGenerator.generateKey();

        // Alternativamente, una clave que queramos que tenga al menos 16 bytes
        // y nos quedamos con los bytes 0 a 15
        key = new SecretKeySpec(llave16b.getBytes(), 0, 16, "AES");

        // Ver como se puede guardar esta clave en un fichero y recuperarla
        // posteriormente en la clase RSAAsymetricCrypto.java
        // Se obtiene un cifrador AES
        Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");

        // Se inicializa para encriptacion y se encripta el texto,
        // que debemos pasar como bytes.
        aes.init(Cipher.ENCRYPT_MODE, key);
        byte[] encriptado = aes.doFinal(texto.getBytes());
        //devuelve un arreglo de bytes que es el texto encriptado
        return encriptado;
    }
    
    public String decript(byte[] text_encript, String llave16b)throws Exception {
        // Generamos una clave de 128 bits adecuada para AES
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        Key key = keyGenerator.generateKey();

        // Alternativamente, una clave que queramos que tenga al menos 16 bytes
        // y nos quedamos con los bytes 0 a 15
        key = new SecretKeySpec(llave16b.getBytes(), 0, 16, "AES");

        // Ver como se puede guardar esta clave en un fichero y recuperarla
        // posteriormente en la clase RSAAsymetricCrypto.java
        // Se obtiene un cifrador AES
        Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");

       // Se iniciliza el cifrador para desencriptar, con la
        // misma clave y se desencripta
        aes.init(Cipher.DECRYPT_MODE, key);
        byte[] desencriptado = aes.doFinal(text_encript);

        //devuelve un string con el texto desencriptado
        return new String(desencriptado);
    }
}
