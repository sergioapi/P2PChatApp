import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Sócrates Agudo Torrado
 * Sergio Álvarez Piñón
 */
// Clase MainApp:
// Esta clase representa la aplicación principal del sistema. Se encarga de iniciar el cliente, el servidor y las interfaces
// gráficas para el inicio de sesión y la conversación.

public class MainApp {

    private static final int RMIPORT = 1099;
    private static final String HOSTNAME = "localhost";
    public static String REGISTRY_URL = "rmi://" + HOSTNAME + ":" + RMIPORT + "/callback";
    private static MensajeroImpl exportedObj;

    private static UserController controller;

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
            System.out.println("Excepción remota: " + re.getMessage());
            System.exit(0);
        }

        // Creo el cliente para recibir Callbacks
        try {
            remoto = new CallbackClientImpl();
            System.out.println("Cliente de callback creado");
        } catch (RemoteException ex) {
            System.out.println("Error al crear cliente: " + ex.getMessage());
            System.exit(0);
        }

        UserController controller = new UserController(server, (CallbackClientImpl) remoto);
        GUILogin login = new GUILogin(controller);

        // Esperar a que el diálogo de inicio de sesión se cierre
        while (login.isDialogVisible()) {
            try {
                Thread.sleep(100); // Espera activa, ajustar según sea necesario
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Continuar con el programa después de que el diálogo de inicio de sesión se cierre
        System.out.println("Cambio de ventana");
        try {
            String urlRegistro = controller.getURL();
            System.out.println(urlRegistro);
            registrarObjRemoto();
            exportedObj = new MensajeroImpl(controller);
            Naming.rebind(urlRegistro, exportedObj);
            System.out.println("Objeto remoto del cliente exportado");
            controller.setMensajero(exportedObj);
            GUIChat chat = new GUIChat(controller);
            //controller.setvChat(chat);
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
            System.out.println("Objeto remoto registrado");
        } catch (RemoteException e) {
            System.out.println("RMI registry cannot be located at port " + RMIPORT);
            registry = LocateRegistry.createRegistry(RMIPORT);
            System.out.println("RMI registry created at port " + RMIPORT);
        }
    }
}
