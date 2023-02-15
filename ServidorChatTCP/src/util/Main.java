package util;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import data.SalaChat;
import hilos.HiloServidorChat;

public class Main{

    static final int MAXIMO = 10; //Máximo de conexiones permitidas
    public static void main(String[] args) {
        //Iniciar ServerSocket
        int puerto = 4444;
        ServerSocket servidor = null;
        try {
            servidor = new ServerSocket(puerto);
            System.out.println("Servidor iniciado en puerto " + puerto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Array de Sockets (uno para cada usuario)
        Socket tabla[] = new Socket[MAXIMO];
        //Crea la sala del chat
        SalaChat sala = new SalaChat(MAXIMO,0,0,tabla);
        //Se ejecuta el bucle siempre que ne la sala no haya más del máximo
        while(sala.getConexiones() < MAXIMO) {
            //Declaro e inicializo el socket
            Socket nuevo;
            try {
                nuevo = servidor.accept();
                System.out.println("Conexión nueva");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            //Añado una conexión al array y sumo uno a las conexiones
            sala.addTabla(nuevo, sala.getConexiones());
            sala.setActuales(sala.getActuales()+1);
            sala.setConexiones(sala.getConexiones()+1);

            //Declaro file para pasarle al hilo el fichero del historial
            File file = new File("mensaje.txt");
            //Crea el hilo con el socket, la sala y el fichero y lo inicio
            HiloServidorChat hilo = new HiloServidorChat(nuevo,sala,file);
            hilo.start();
        }
        try {
            //Cierro el servidor
            servidor.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}