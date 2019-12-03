/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encriptardesencriptar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

public class main {

    public static void main(String[] args) throws Exception {

        RSA rsa = new RSA();
        
        String linea;
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        String texto = "";
        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            archivo = new File("/Users/edissoncamilochavezhuertas/Desktop/mochila.txt");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            // Lectura del fichero
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
                texto = linea;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta 
            // una excepcion.
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        //Definimos un texto a cifrar
        System.out.println("\nTexto a cifrar:");
        System.out.println(texto);

        //Instanciamos la clase
        //Generamos un par de claves
        //Admite claves de 512, 1024, 2048 y 4096 bits
        rsa.genKeyPair(512);

        String file_private = "/tmp/rsa.pri";
        String file_public = "/tmp/rsa.pub";

        //Las guardamos asi podemos usarlas despues
        //a lo largo del tiempo
        rsa.saveToDiskPrivateKey("/tmp/rsa.pri");
        rsa.saveToDiskPublicKey("/tmp/rsa.pub");

        //Ciframos y e imprimimos, el texto cifrado
        //es devuelto en la variable secure
        String secure = rsa.Encrypt(texto);

        System.out.println("\nCifrado:");
        System.out.println(secure);

        //A modo de ejemplo creamos otra clase rsa
        RSA rsa2 = new RSA();

        //A diferencia de la anterior aca no creamos
        //un nuevo par de claves, sino que cargamos
        //el juego de claves que habiamos guadado
        rsa2.openFromDiskPrivateKey("/tmp/rsa.pri");
        rsa2.openFromDiskPublicKey("/tmp/rsa.pub");

        //Le pasamos el texto cifrado (secure) y nos 
        //es devuelto el texto ya descifrado (unsecure) 
        String unsecure = rsa2.Decrypt(secure);

        //Imprimimos
        System.out.println("\nDescifrado:");
        System.out.println(unsecure);

        // hash del archivo  de texto  mochila.txt
        String nombreArchivo = "/Users/edissoncamilochavezhuertas/Desktop/mochila.txt";
        try {
            String checksum = obtenerMD5ComoString(nombreArchivo);
            System.out.println(" ");
            System.out.println("El Hash de  " + texto + " es:" + checksum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] obtenerChecksum(String nombreArchivo) throws Exception {
     InputStream fis = new FileInputStream(nombreArchivo);
     byte[] buffer = new byte[1024];
     MessageDigest complete = MessageDigest.getInstance("MD5");
     int numRead;
    // Leer el archivo pedazo por pedazo
        do {
    // Leer datos y ponerlos dentro del búfer
     numRead = fis.read(buffer);
    // Si se leyó algo, se actualiza el MessageDigest
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);
        fis.close();
     // Devolver el arreglo de bytes
        return complete.digest();
    }

    public static String obtenerMD5ComoString(String nombreArchivo) throws Exception {
    // Convertir el arreglo de bytes a cadena
        byte[] b = obtenerChecksum(nombreArchivo);
        StringBuilder resultado = new StringBuilder();
        for (byte unByte : b) {
            resultado.append(Integer.toString((unByte & 0xff) + 0x100, 16).substring(1));
        }
        return resultado.toString();
    }

}
