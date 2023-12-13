import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Sócrates Agudo Torrado
 * Sergio Álvarez Piñón
 */

// Clase MensajeroImpl:
// Esta clase implementa la interfaz MensajeroInterface y proporciona una
// implementación simple para recibir mensajes. La implementación simplemente
// devuelve el mensaje recibido.

public class MensajeroImpl extends UnicastRemoteObject implements MensajeroInterface {
    private UserController controller;

    // Constructor de la clase
    public MensajeroImpl(UserController controller) throws RemoteException {
        super();
        this.controller = controller;
    }

    // Método para recibir mensajes
    public String recibirMsj(String sender, String msj) {
        System.out.println("Mensaje de " + sender + ": " + msj);
        // Retorna el mensaje recibido tal como está
        controller.actualizarMensajes(sender, msj);
        return msj;
    }
}
