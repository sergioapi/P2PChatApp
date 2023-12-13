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
import java.util.ArrayList;

public class UserController {
    // Interfaz del servidor RMI
    private CallbackServerInterface server;

    // Interfaz del cliente para callbacks
    private CallbackClientImpl client;

    // Objeto Usuario actual
    private Usuario user;

    private GUIChat vChat;
    private String contrasena;

    // Constructor que recibe las interfaces del servidor y del cliente
    public UserController(CallbackServerInterface server, CallbackClientImpl client) {
        this.user = null;
        this.server = server;
        this.client = client;
        client.setController(this);
    }

    // Método para registrar un nuevo usuario
    public boolean registrarse(String usuario, String contrasena) {
        this.contrasena = hashPassword(contrasena);
        try {
            user = server.registrarUsuario(client, usuario, this.contrasena);
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
            this.contrasena = hashPassword(contrasena);
            user = server.iniciarSesion(client, usuario, this.contrasena);
            if (user != null) {
                System.out.println("Inicio de sesion exitoso");
                System.out.println("Amigos de " + user.getUsername() + ": " + user.getAmigos());
                for (Usuario conectado : user.getAmigosConectados())
                    System.out.println("Amigos conectados de " + user.getUsername() + ": " + conectado.getUsername());
            } else {
                System.out.println("Error al iniciar sesión");
                return false;
            }
            return true;
        } catch (RemoteException e) {
            System.out.println("Error al inicar sesion: " + e.getMessage());
            return false;
        }
    }

    // Método para cerrar sesión
    public boolean cerrarSesion() {
        try {
            server.cerrarSesion(user, this.contrasena);
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
            server.pedirAmistad(user.getUsername(), usuario, this.contrasena);
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
            nuevoAmigo = server.aceptarAmistad(user, username, this.contrasena);
            System.out.println("Amistad aceptada");
            if (nuevoAmigo != null) {
                user.eliminarSolicitud(username);
                if (nuevoAmigo.isConectado()) {
                    user.anadirAmigo(nuevoAmigo);
                    vChat.actualizarListaNombres(this);
                } else user.anadirAmigo(username);

            }
        } catch (RemoteException e) {
            System.out.println("Error al aceptar peticion de amistad: " + e.getMessage());
        }
        return false;
    }

    // Método para rechazar una solicitud de amistad
    public boolean rechazarAmistad(String usuario) {
        try {
            if (server.rechazarAmistad(user.getUsername(), usuario, this.contrasena)) {
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
            if (server.eliminarAmigo(user, usuario, this.contrasena)) {
                user.eliminarAmigo(usuario);
                vChat.actualizarListaNombres(this);
                System.out.println("se ha eliminado correctamente al amigo");
                return true;
            }
        } catch (RemoteException e) {
            System.out.println("Error al eliminar amigo: " + e.getMessage());
        }
        return false;
    }

    public ArrayList<String> obtenerSolicitudesPendientes() {
        //ArrayList<String> solicitudes = new ArrayList<>();
        try {
            return server.obtenerSolicitudes(user.getUsername(), this.contrasena);
        } catch (RemoteException e) {
            System.out.println("Error al obtener las solicitudes de amistad de " + user.getUsername() + ": " + e.getMessage());
            return null;
        }
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
                    if (!amigo.chatIniciado()) iniciarChat(usuario);
                    System.out.println("Mensaje de " + usuario + ": " + mensaje);
                    amigo.recibirMensaje(user.getUsername(), mensaje);
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
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

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

    public CallbackServerInterface getServer() {
        return server;
    }

    public Usuario getUser() {
        return user;
    }

    public void setvChat(GUIChat vChat) {
        this.vChat = vChat;
    }

    public void actualizarMensajes(String mensaje, String sender) {
        vChat.actualizarMensajes(mensaje, sender);
    }

    public void actualizarAmigosConectados(Usuario amigo, boolean conexion) {
        if (conexion) {
            user.anadirAmigoConectado(amigo);
        } else {
            System.out.println("Quitando de amigos conectados a: " + amigo.getUsername());
            user.amigoDesconectado(amigo);
        }
        vChat.actualizarListaNombres(this);
    }

    public void actualizarAmigos(Usuario amigo, boolean amistad) {
        if (amistad)
            user.anadirAmigo(amigo);
        else user.eliminarAmigo(amigo.getUsername());
        vChat.actualizarListaNombres(this);
    }

    public void setMensajero(MensajeroInterface mensajero) {
        user.setMensajero(mensajero);
    }
}
