import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
/**
 * Sócrates Agudo Torrado
 * Sergio Álvarez Piñón
 * */

// Interfaz CallbackServerInterface:
// Esta interfaz define los métodos remotos que pueden ser invocados por los
// clientes para realizar operaciones en el servidor. Cada método representa
// una funcionalidad específica relacionada con la gestión de usuarios y amistades.

public interface CallbackServerInterface extends Remote {

    // Método para iniciar sesión de un usuario
    public Usuario iniciarSesion(CallbackClientInterface cliente, String username, String contrasena) throws RemoteException;

    // Método para registrar un nuevo usuario
    public Usuario registrarUsuario(CallbackClientInterface cliente, String username, String contrasena) throws RemoteException;

    // Método para eliminar un usuario
    public boolean eliminarUsuario(Usuario usuario, String contrasena) throws RemoteException;

    // Método para cerrar sesión de un usuario
    public void cerrarSesion(Usuario usuario, String contrasena) throws RemoteException;

    // Método para aceptar una solicitud de amistad
    public Usuario aceptarAmistad(Usuario usuario1, String username2, String contrasena) throws RemoteException;

    // Método para enviar una solicitud de amistad
    public boolean pedirAmistad(String usuario1, String usuario2, String contrasena) throws RemoteException;

    // Método para rechazar una solicitud de amistad
    public boolean rechazarAmistad(String usuario1, String usuario2, String contrasena) throws RemoteException;

    // Método para eliminar a un amigo
    public boolean eliminarAmigo(Usuario usuario1, String username2, String contrasena) throws RemoteException;

    // Método para obtener la lista de amigos de un usuario
    public ArrayList<String> obtenerAmistades(String usuario, String contrasena) throws RemoteException;

    // Método para obtener la lista de solicitudes pendientes de un usuario
    public ArrayList<String> obtenerSolicitudes(String usuario, String contrasena) throws RemoteException;

    // Método para obtener la dirección de un usuario (no implementado)
    public String obtenerDireccion(String usuario, String contrasena) throws RemoteException;
}
