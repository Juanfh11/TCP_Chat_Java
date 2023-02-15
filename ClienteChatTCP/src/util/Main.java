package util;

import hilos.HiloLeerMensajes;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Main {
    //Declaro e inicializo el socket
    static Socket socket;
    static {
        try {
            socket = new Socket("localhost",4444);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //Declaro e inicializo el DataOutputStream
    static DataOutputStream salida;
    static{
        try {
            salida = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //Declar variable string
    static String nombre;

    /**
     * Main del programa
     * -Pido el nombre del usuario
     * -Compruebo y registro el nombre si no se repite
     * -Creo la interfaz y la hago visible
     * -Creo e inicio el hilo que lee los mensajes y el historial del chat
     */
    public static void main(String[] args) {
        //Declaro booleano para controlar el bucle
        boolean correcto = false;
        //Pido mediante una ventana el nombre
        nombre = JOptionPane.showInputDialog("Dime tu nombre:");
        while (!correcto){ //Bucle para pedir varias veces el nombre si ya existe
            if (comprobarNombre()){ //Compruebo si existe el nombre
                correcto = true;
                //Registro el nombre
                registrarNombre();

                //Creo la ventana y le paso por parámetro el nombre del usuario
                ClienteFrame clf = new ClienteFrame(nombre);
                //La hago visible
                clf.setVisible(true);

                //Ejecuto el hilo para leer el historial y los mensajes de los demás clientes
                HiloLeerMensajes hilo = new HiloLeerMensajes(socket, clf);
                hilo.start();
            }else{ //Si el nombre ya existe se vuelve a pedir
                nombre = JOptionPane.showInputDialog("ERROR, nombre ya usado\nDime otro nombre");
            }
        }
    }

    /**
     * Escribo el mensaje escrito con la cadena recibida por parámetro
     */
    public static void escribirMensaje(String cadena) {
        try {
            //Escribo el mensaje junto con el nombre del usuario
            salida.writeUTF(nombre + ": " + cadena);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Registro el nombre en el fichero
     */
    private static void registrarNombre(){
        try {
            //Declaro BufferedWriter con el fichero donde se va a escrbir e indico que se no se sobreescriban los datos
            BufferedWriter bw = new BufferedWriter(new FileWriter("nombres.txt", true));
            //Escribo el nombre
            bw.write(nombre + "\n");
            //Cierro el BufferedReader
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Método que comprueba si el nombre introducido está ya registrado en el fichero
     * Si el nombre ya existe devuelve un false y si no existe devuelve un true
     */
    private static boolean comprobarNombre(){
        try {
            //Declaro string y un BufferedReader con el fichero que va a leer
            String linea;
            BufferedReader br = new BufferedReader(new FileReader("nombres.txt"));
            //Leo la primera línea del fichero
            linea = br.readLine();
            while (linea != null){ //Bucle hasta que la línea que lea sea null
                if (linea.equalsIgnoreCase(nombre)){ //Si el nombre leido es igual al introducido
                    //Cierro el BufferedReader y devuelvo false
                    br.close();
                    return false;
                }else{ //Si el nombre leído es diferente al introducido
                    //Leo la siguiente línea
                    linea = br.readLine();
                }
            }
            //Cierro el BufferedReader y devuelvo true
            br.close();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}