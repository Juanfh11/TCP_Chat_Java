package hilos;

import java.io.*;
import java.net.Socket;

import data.SalaChat;

public class HiloServidorChat extends Thread{ //Extiendo la clase Thread
    //Variables
    Socket socket;
    SalaChat sala;
    DataInputStream entrada;
    DataOutputStream salida;
    File file;

    //Constructor
    public HiloServidorChat(Socket s, SalaChat sal, File fichero) {
        this.socket = s;
        this.sala = sal;
        try {
            entrada = new DataInputStream(socket.getInputStream());
            salida = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.file = fichero;
    }

    /**
     * Metodo run, se encarga del proceso de envio y recibo de mensajes
     */
    public void run() {
        System.out.println("Lanzado hilo" + this.getName());
        //Llamo al metodo mandarFichero y le paso por parámetro el fichero
        mandarFichero(file);
        boolean salir = false;
        while (!salir) { //Bucle que se ejecuta hasta que se reciba un *
            String cadena;
            try {
                //Almaceno en la variable el mensaje recibido
                cadena = entrada.readUTF();
                //Si el mensaje es un *
                if (cadena.trim().equals("*")){
                    //Imprimo por pantalla que se desconecta el hilo y salgo del bucle
                    System.out.println("Se ha desconectado " + this.getName());
                    salir = true;
                }else{ //Si el mensaje no es un *
                    //Se manda el mensaje al resto de clientes del chat
                    mandarMensajes(cadena);
                    //Escribo en el fichero del historial el mensaje
                    escribirFichero(cadena);
                }
            } catch (EOFException e){
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Escribe en el fichero los mensajes recibidos para generar el historial del chat
     */
    public void escribirFichero(String cadena){
        try{
            //Declaro BufferedWriter y escribo en el fichero el mensaje
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.write(cadena + "\n");
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Métod que se encarga de mandar el historial, que se almacena
     * en el fichero, al los clientes que se van conectando
     */
    public void mandarFichero(File file){
        try{
            //Declaro String y BufferedReader
            String linea;
            BufferedReader br = new BufferedReader(new FileReader(file));
            //Leo la primera línea del historial
            linea = br.readLine();
            while (linea!=null){ //Bucle hasta que se lea null
                //envío la línea
                salida.writeUTF(linea);
                //Leo la siguiente línea
                linea = br.readLine();
            }
            //Cuando se haya leido el historial al completo se
            // envía un mensaje especifico para indicar fin de fichero
            salida.writeUTF("---FIN DEL HISTORIAL---");
            salida.writeUTF("b95e08220847");
            //Cierro el BufferedReader
            br.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Método que se encarga de mandar el mensaje envíado
     * por el cliente al resto de clientes conectados al servidor
     */
    public void mandarMensajes(String cadena){
        try {
            //Bucle for para recorrer todos los clientes de la sala
            for (int i = 0; i < sala.getTabla().length; i++) {
                //Envío al cliente el mensaje
                salida = new DataOutputStream(sala.getTabla()[i].getOutputStream());
                salida.writeUTF(cadena);
            }
        } catch (NullPointerException e){
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}