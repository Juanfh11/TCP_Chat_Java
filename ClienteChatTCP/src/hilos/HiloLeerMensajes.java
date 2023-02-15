package hilos;

import util.ClienteFrame;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class HiloLeerMensajes extends Thread{
    //Variables
    Socket socket;
    DataInputStream entrada;
    ClienteFrame cf;

    //Constructor
    public HiloLeerMensajes(Socket socket, ClienteFrame cf) {
        this.socket = socket;
        try {
            entrada = new DataInputStream(socket.getInputStream());
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.cf = cf;
    }

    /**
     * El método run consiste en un bucle infinito para que primero lea
     * el historial del chat y luego lea los mensajes que han enviado el resto de clientes
     */
    @Override
    public void run() {
        boolean salir = false;
        String mensaje;
        while (!salir){ //Bucle que se ejecute hasta que se cierre el socket
            try {
                //Leo el mensaje
                mensaje = entrada.readUTF();
                //Si el mensaje es el mensaje codificado del fin del fichero lo ignora
                if (!mensaje.equalsIgnoreCase("b95e08220847")){
                    //Coge los mensajes anteriores del textarea y añade el nuevo el mensaje
                    cf.textarea.setText(cf.textarea.getText() + mensaje + "\n");
                }
            }catch (java.net.SocketException e){
                //Cuando el cliente se desconecta del socket
                // sale del bucle y acaba el hilo
                salir = true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
