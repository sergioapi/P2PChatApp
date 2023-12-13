
/**
 * Sócrates Agudo Torrado
 * Sergio Álvarez Piñón
 */

// Interfaz CallbackClientInterface:
// Esta interfaz define los métodos que pueden ser invocados remotamente
// por el servidor para notificar a los clientes sobre eventos relacionados
// con la conexión de amigos.

public interface CallbackClientInterface extends java.rmi.Remote {

    // Método para notificar que un amigo se ha conectado
    void amigoConectado(Usuario usuario) throws java.rmi.RemoteException;

    // Método para notificar que un amigo se ha desconectado
    void amigoDesconectado(Usuario usuario) throws java.rmi.RemoteException;

    void nuevoAmigo(Usuario usuario) throws java.rmi.RemoteException;

    void amigoBorrado(Usuario usuario) throws java.rmi.RemoteException;

} // end interface
