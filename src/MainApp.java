import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.Objects;

public class MainApp {

    public static final int RMIPORT = 1099;
    public static final String HOSTNAME = "localhost";
    public static final String REGISTRY_URL = "rmi://" + RMIPORT + ":" + HOSTNAME + "/callback";
    private CallbackServerInterface h;
    private CallbackClientInterface callbackObj;

    private static ArrayList<String> usuariosConectados;
    private static String username;

    private static CallbackClientThread hilo;

    // Tener dos interfaces: una del server para recibir cuando se conecta alguien y poder enviar solicitudes de amistad
    // y obtener el objeto remoto de un cliente
    // otra interfaz para enviar y recibir mensajes entre clientes (P2P) => hacer de esta clase otro servidor, colgando
    // su propio objeto remoto

    public static void main(String[] args) {
        CallbackServerInterface server = null;
        CallbackClientInterface remoto = null;
        usuariosConectados = new ArrayList<>();
        username = "cliente1";

        // Obtengo el objeto del Servidor
        try {
            server = (CallbackServerInterface) Naming.lookup(REGISTRY_URL);
            System.out.println("Lookup completed ");

        } catch (MalformedURLException ex) {
            System.out.println("Error al formar URL: " + ex.getMessage());
            System.exit(0);
        } catch (NotBoundException e) {
            System.out.println("Error en el registro: " + e.getMessage());
            System.exit(0);
        } catch (RemoteException re) {
            System.out.println("Excepcion remota: " + re.getMessage());
            System.exit(0);
        }

// Registro al cliente para recibir Callbacks
        try {
            remoto = new CallbackClientImpl();
            server.registrarCliente(remoto);
            System.out.println("Registered for callback.");


        } catch (RemoteException ex) {
            System.out.println("Error al sucribirse: " + ex.getMessage());
            System.exit(0);
        }
        // creo el hilo
        hilo = new CallbackClientThread(username, usuariosConectados);
        System.out.println("************** CHAT P2P **************");
        hilo.start();
        try {
            hilo.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

// Me desuscribo
        try {
            server.suprimirCliente(remoto);
            System.out.println("Unregistered for callback.");

        } catch (RemoteException ex) {
            System.out.println("Error al desuscribirse: " + ex.getMessage());
        }
    }

    public static void actualizarListaUsuarios(String usernames) {
        // Utilizar addAll y luego quitar el del cliente actual
        // Recorrer el mensaje y añadir solo los nuevos
        String[] arrUsers = usernames.split(",");
        usuariosConectados.clear();
        for (String user : arrUsers) {
            if (!Objects.equals(user, username))
                usuariosConectados.add(user);
        }
        hilo.setUsuariosConectados(usuariosConectados);
    }
}
