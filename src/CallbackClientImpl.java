import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Sócrates Agudo Torrado
 * Sergio Álvarez Piñón
 */
// Clase CallbackClientImpl:
// Implementación de la interfaz CallbackClientInterface que define los métodos para recibir notificaciones de conexión y desconexión de amigos.

public class CallbackClientImpl extends UnicastRemoteObject implements CallbackClientInterface {

    private UserController controller;

    public CallbackClientImpl() throws RemoteException {
        super();
    }

    @Override
    public void amigoConectado(Usuario usuario) throws RemoteException {
        // Método llamado cuando un amigo se conecta.
        // Aquí se puede implementar la lógica necesaria para manejar la notificación de conexión de un amigo.
        // Por ejemplo, actualizar la interfaz gráfica para reflejar el estado de conexión del amigo.
        if (controller != null) {
            controller.actualizarAmigosConectados(usuario, true);
        }
    }

    @Override
    public void amigoDesconectado(Usuario usuario) throws RemoteException {
        // Método llamado cuando un amigo se desconecta.
        // Aquí se puede implementar la lógica necesaria para manejar la notificación de desconexión de un amigo.
        // Por ejemplo, actualizar la interfaz gráfica para reflejar el estado de desconexión del amigo.
        if (controller != null) {
            System.out.println(usuario.getUsername() + " se ha desconectado");
            controller.actualizarAmigosConectados(usuario, false);
        }
    }

    @Override
    public void nuevoAmigo(Usuario usuario) throws RemoteException {
        if (controller != null) {
            controller.actualizarAmigos(usuario, true);
        }
    }

    @Override
    public void amigoBorrado(Usuario usuario) throws RemoteException {
        if (controller != null) {
            controller.actualizarAmigos(usuario, false);
        }
    }

    public void setController(UserController controller) {
        if (controller != null)
            this.controller = controller;
    }
}

