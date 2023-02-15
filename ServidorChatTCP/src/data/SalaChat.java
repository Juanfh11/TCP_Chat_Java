package data;

import java.io.File;
import java.net.Socket;

public class SalaChat {
    //VARIABLES
    private int conexiones;
    private int actuales;
    private int MAXIMO;
    private Socket tabla[];

    //CONSTRUCTOR
    public SalaChat(int max,int conexiones, int actuales,Socket[] tabla) {
        this.setConexiones(conexiones);
        this.setActuales(actuales);
        MAXIMO = max;
        this.tabla = tabla;
    }

    //METODOS
    public int getConexiones() {
        return conexiones;
    }

    public void setConexiones(int conexiones) {
        this.conexiones = conexiones;
    }

    public int getActuales() {
        return actuales;
    }

    public void setActuales(int actuales) {
        this.actuales = actuales;
    }

    //LE PASAS EL NUMERO DE CONEXIÓN QUE ERES Y AÑADES EL SOCKET AL ARRAY
    public void addTabla (Socket s, int i) {
        tabla[i] = s;
    }

    public Socket[] getTabla() {
        return tabla;
    }
}
