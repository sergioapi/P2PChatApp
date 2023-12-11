/**
 * Sócrates Agudo Torrado
 * Sergio Álvarez Piñón
 */

// Clase UserController:
// Esta clase gestiona las operaciones relacionadas con el usuario, como el registro,
// inicio de sesión, cierre de sesión, eliminación de usuario, gestión de amistades,
// inicio de chat y envío de mensajes. Utiliza interfaces RMI para comunicarse con
// el servidor y otros usuarios.

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserController {
    // Interfaz del servidor RMI
    private CallbackServerInterface server;

    // Interfaz del cliente para callbacks
    private CallbackClientInterface client;

    // Objeto Usuario actual
    private Usuario user;

    // Constructor que recibe las interfaces del servidor y del cliente
    public UserController(CallbackServerInterface server, CallbackClientInterface client) {
        this.server = server;
        this.client = client;
    }

    // Método para registrar un nuevo usuario
    public boolean registrarse(String usuario, String contrasena) {
        String passwdHash = hashPassword(contrasena);
        try {
            user = server.registrarUsuario(client, usuario, passwdHash);
            System.out.println("Registro exitoso");
        } catch (RemoteException e) {
            System.out.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
        return user != null;
    }

    // Método para iniciar sesión
    public boolean iniciarSesion(String usuario, String contrasena) {
        try {
            String passwdHash = hashPassword(contrasena);
            user = server.iniciarSesion(client, usuario, passwdHash);
            System.out.println("Inicio de sesion exitoso");
            return true;
        } catch (RemoteException e) {
            System.out.println("Error al inicar sesion: " + e.getMessage());
            return false;
        }
    }

    // Método para cerrar sesión
    public boolean cerrarSesion() {
        try {
            server.cerrarSesion(user);
            System.out.println("Sesion cerrada correctamente");
            return true;
        } catch (RemoteException e) {
            System.out.println("Error cerrando sesion: " + e.getMessage());
            return false;
        }
    }

    // Método para eliminar un usuario
    public boolean borrarUsuario(String contrasena) {
        try {
            String passwdHash = hashPassword(contrasena);
            server.eliminarUsuario(user, passwdHash);
            System.out.println("Usuario eliminado correctamente");
            return true;
        } catch (RemoteException e) {
            System.out.println("Error eliminando usuario: " + e.getMessage());
            return false;
        }
    }

    // Método para enviar una solicitud de amistad
    public boolean pedirAmistad(String usuario) {
        try {
            server.pedirAmistad(user.getUsername(), usuario);
            System.out.println("Petición de amistad enviada correctamente");
            return true;

        } catch (RemoteException e) {
            System.out.println("Error al pedir amistad: " + e.getMessage());
            return false;
        }
    }

    // Método para aceptar una solicitud de amistad
    public boolean aceptarAmistad(String username) {
        Usuario nuevoAmigo = null;
        try {
            nuevoAmigo = server.aceptarAmistad(user, username);
            System.out.println("Amistad aceptada");
            if (nuevoAmigo != null) {
                user.eliminarSolicitud(username);
                if (nuevoAmigo.isConectado())
                    user.anadirAmigo(nuevoAmigo);
                else user.anadirAmigo(username);

            }
        } catch (RemoteException e) {
            System.out.println("Error al aceptar peticion de amistad: " + e.getMessage());
        }
        return false;
    }

    // Método para rechazar una solicitud de amistad
    public boolean rechazarAmistad(String usuario) {
        try {
            if (server.rechazarAmistad(user.getUsername(), usuario)) {
                user.eliminarSolicitud(usuario);
                System.out.println("Peticion de amistad rechazada");
                return true;
            }
        } catch (RemoteException e) {
            System.out.println("Error al rechazar peticion de amistad: " + e.getMessage());

        }
        return false;
    }

    // Método para eliminar a un amigo
    public boolean eliminarAmigo(String usuario) {
        try {
            if (server.eliminarAmigo(user, usuario)) {
                user.eliminarAmigo(usuario);
                System.out.println("se ha eliminado correctamente al amigo");
                return true;
            }
        } catch (RemoteException e) {
            System.out.println("Error al eliminar amigo: " + e.getMessage());
        }
        return false;
    }

    // Método para iniciar un chat con un amigo
    public boolean iniciarChat(String usuario) {
        Usuario amigo = user.getAmigo(usuario);
        if (amigo != null) {
            if (amigo.isConectado()) {
                try {
                    amigo.setMensajero((MensajeroInterface) Naming.lookup(amigo.getRemoteURL()));
                    return true;
                } catch (NotBoundException | RemoteException | MalformedURLException e) {
                    System.out.println("Error al iniciar chat: " + e.getMessage());
                }
            }
        }
        return false;
    }

    // Método para enviar un mensaje a un amigo
    public boolean enviarMensaje(String usuario, String mensaje) {
        Usuario amigo = user.getAmigo(usuario);
        if (amigo != null) {
            if (amigo.isConectado()) {
                try {
                    if (!amigo.chatIniciado())
                        iniciarChat(usuario);
                    amigo.recibirMensaje(mensaje);
                    System.out.println("Mensaje enviado correctamente");
                    return true;
                } catch (RemoteException e) {
                    System.out.println("Error al enviar mensaje: " + e.getMessage());
                }
            }
        }
        return false;
    }

    // Método para obtener la URL del usuario
    public String getURL() {
        return user.getRemoteURL();
    }

    // Método para obtener el nombre de usuario del usuario actual
    public String getNombreUsuario() {
        return user.getUsername();
    }

    // Método para generar el hash de una contraseña usando SHA-256
    private String hashPassword(String password) {
        try {
            // Selecciona el algoritmo de hash (en este caso, SHA-256)
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Convierte la contraseña en bytes y aplica el hash
            byte[] encodedhash = digest.digest(
                    password.getBytes(StandardCharsets.UTF_8));

            // Convierte el hash en una representación hexadecimal
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Manejo de la excepción (puede lanzar NoSuchAlgorithmException)
        }

        return null;
    }
}
