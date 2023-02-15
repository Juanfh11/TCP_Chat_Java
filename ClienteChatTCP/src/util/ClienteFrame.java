package util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClienteFrame extends JFrame implements ActionListener {
    //Declaro variables
    String nombre;
    JTextField mensaje = new JTextField();
    JScrollPane scrollpanel;
    public JTextArea textarea;
    JButton botonEnviar = new JButton("Enviar");
    JButton botonSalir = new JButton("Salir");

    //Constructor
    public ClienteFrame(String nombre) {
        //Pongo titulo a la ventana
        super("CHAT PROCESOS: " + nombre);
        this.nombre = nombre;
        //Pongo tamaño a la ventana
        setSize(530,420);
        setLayout(null);
        //Pongo tamaño al JtextField
        mensaje.setBounds(10,10,400,30);
        //Lo añado a la ventana
        this.add(mensaje);
        textarea = new JTextArea();
        scrollpanel = new JScrollPane();
        //Pongo tamaño al scrollpanel
        scrollpanel.setBounds(10,50,400,300);
        //Lo añado a la ventana
        this.add(scrollpanel);
        //Pongo tamaño al boton enviar
        botonEnviar.setBounds(420,10,100,30);
        //Cuando le des al boton que realize una accion sobre la interfaz
        botonEnviar.addActionListener(this);
        //Lo añado a la ventana
        this.add(botonEnviar);
        //Pongo tamaño al boton salir
        botonSalir.setBounds(420,50,100,30);
        //Cuando le des al boton que realize una accion sobre la interfaz
        botonSalir.addActionListener(this);
        //Lo añado a la ventana
        this.add(botonSalir);
        //Para que no puedas escribir en el textarea
        textarea.setEditable(false);
        //Le doy un tamaño al textarea
        textarea.setBounds(0, 0, 400, 300);
        //Se lo añado al scrollpanel
        scrollpanel.add(textarea);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()== botonEnviar) { //Si se pulsa el boton enviar
            //Envio el mensaje al servidor y borro el texto escrito del campo mensaje
            Main.escribirMensaje(mensaje.getText());
            mensaje.setText("");
        }
        if (e.getSource()== botonSalir) { //Si se pulsa el boton salir
            try {
                //Mando al servidor un * para que sepa que se va a desconectar
                Main.salida.writeUTF("*");
                //Muestro panel de que se ha desconectado del servidor
                JOptionPane.showMessageDialog(null,"Te has desconectado del servidor");
                //Cierro la salida y el socket
                Main.salida.close();
                Main.socket.close();
                System.exit(0);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
