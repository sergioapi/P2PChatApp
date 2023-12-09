import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MainApp {

    private static final int RMIPORT = 1099;
    private static final String HOSTNAME = "localhost";
    public static String REGISTRY_URL = "rmi://" + RMIPORT + ":" + HOSTNAME + "/callback";

    private static MensajeroImpl exportedObj;


    // Tener dos interfaces: una del server para recibir cuando se conecta alguien y poder enviar solicitudes de amistad
    // y obtener el objeto remoto de un cliente
    // otra interfaz para enviar y recibir mensajes entre clientes (P2P) => hacer de esta clase otro servidor, colgando
    // su propio objeto remoto

    public static void main(String[] args) {
        CallbackServerInterface server = null;
        CallbackClientInterface remoto = null;

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

// Creo el cliente para recibir Callbacks
        try {
            remoto = new CallbackClientImpl();
        } catch (RemoteException ex) {
            System.out.println("Error al crear cliente: " + ex.getMessage());
            System.exit(0);
        }

        UserController controller = new UserController(server, remoto);
        GUILogin login = new GUILogin(controller);
        String urlRegistro = "rmi://localhost:1099/" + controller.getNombreUsuario();

        try {
            registrarObjRemoto();
            exportedObj = new MensajeroImpl();
            Naming.rebind(urlRegistro, exportedObj);
            //listRegistry(urlRegistro);
            GUIChat chat = new GUIChat(controller);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void registrarObjRemoto() throws RemoteException {
        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(RMIPORT);
            registry.list();
        } catch (RemoteException e) {
            System.out.println("RMI registry cannot be located at port " + RMIPORT);
            registry = LocateRegistry.createRegistry(RMIPORT);
            System.out.println("RMI registry created at port " + RMIPORT);
        }
    }
}
