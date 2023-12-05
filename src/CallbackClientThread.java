import java.io.*;
import java.rmi.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class CallbackClientThread extends Thread {

    private CallbackServerInterface h;
    private CallbackClientInterface callbackObj;
    private int RMIPort;
    private String hostName;
    private String registryURL;

    private ArrayList<String> usuariosConectados;
    private String username;
    private ArrayList<Mensaje> mensajesRecibidos;
    private ArrayList<Mensaje> mensajesEnviados;

    // Tener clase usuario (futuro amigo) en la que se guarden los mensajes enviados y recibidos
    // Tener una clase conversación en la que se guarden los mensajes recibidos y enviados (en orden de fecha)

    public CallbackClientThread(String username, ArrayList<String> usuariosConectados) {
        super(username);
        this.username = username;
        this.usuariosConectados = new ArrayList<>();
        this.usuariosConectados.addAll(usuariosConectados);
        this.mensajesRecibidos = new ArrayList<>();
    }

    @Override
    public void run() {
        // Imprimir opciones de menu y recibir entrada usuario
        Scanner scanner = new Scanner(System.in);
        int opcion = 0;

        do {
            System.out.println("1. Mostrar usuarios conectados");
            System.out.println("2. Mostrar mensajes recibidos");
            System.out.println("3. Enviar mensaje");
            System.out.println("0. Desconectarse");
            System.out.println("Seleccione una opción (0-3):");
            opcion = scanner.nextInt();
            scanner.nextLine();

            // Mostrar mensajes no leidos
            // Mostrar conversacion con un usuario en concreto (msjs enviados y recibidos)
            switch (opcion) {
                // Ver conectados
                case 1:
                    System.out.println(usuariosConectados.toString());
                    break;
                // ver mensajes
                case 2:
                    System.out.println(mensajesRecibidos.toString());
                    break;
                // enviar mensaje
                case 3:
                    System.out.println("¿A qué usuario quieres enviarle un mensaje?");
                    System.out.println(usuariosConectados.toString());
                    String destino = scanner.nextLine();
                    if (usuariosConectados.contains(destino)) {
                        System.out.println("Escribe tu mensaje: ");
                        String contenido = scanner.nextLine();
                        Mensaje msj = new Mensaje(destino, username, contenido);
                        mensajesEnviados.add(msj);
                    }else System.out.println("El usuario indicado no se encuentra en la lista de usuarios conectados.");
                    break;
                // desconectarse
                case 0:

                    break;
                default:
                    break;
            }
        } while (opcion != 0);
    }

    private void imprimirMenu() {

    }

    public void setUsuariosConectados(ArrayList<String> usuariosConectados) {
        this.usuariosConectados.clear();
        this.usuariosConectados.addAll(usuariosConectados);
    }
}
