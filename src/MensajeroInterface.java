import java.rmi.Remote;

/**
 * Sócrates Agudo Torrado
 * Sergio Álvarez Piñón
 */

// Interfaz MensajeroInterface:
// Esta interfaz define un servicio remoto para enviar y recibir mensajes.
// Extiende la interfaz Remote para indicar que sus métodos pueden ser
// invocados de forma remota.

public interface MensajeroInterface extends Remote {

    // Método remoto para recibir mensajes.
    // Puede lanzar una excepción RemoteException para manejar errores
    // relacionados con la comunicación remota.
    public String recibirMsj(String msj, String sender) throws java.rmi.RemoteException;

}

