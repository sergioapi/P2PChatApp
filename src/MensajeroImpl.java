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

    // Constructor de la clase
    public MensajeroImpl() throws RemoteException {
        super();
    }

    // Método para recibir mensajes
    public String recibirMsj(String msj) {
        // Retorna el mensaje recibido tal como está
        return msj;
    }
}
