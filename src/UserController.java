import java.rmi.RemoteException;

public class UserController {
    private CallbackServerInterface server;
    private CallbackClientInterface client;
    private Usuario user;

    public UserController(CallbackServerInterface server, CallbackClientInterface client) {
        this.server = server;
        this.client = client;

    }

    public boolean registrarse(String usuario, String contrasena) {
        user = server.registrarUsuario(client, usuario, contrasena);
        return user != null;
    }

    public boolean iniciarSesion(String usuario, String contrasena) {
        try {
            user = server.iniciarSesion(client, usuario, contrasena);
        } catch (RemoteException e) {
            System.out.println();
        }
        return user != null;
    }

    public boolean cerrarSesion() {
        try {
            server.cerrarSesion(user);
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean borrarUsuario(String contrasena) {
        try {
            server.eliminarUsuario(user, contrasena);
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean pedirAmistad(String usuario) {
        return server.pedirAmistad(user.getUsername(), usuario);
    }

    public boolean aceptarAmistad(String username) {
        Usuario nuevoAmigo = null;
        try {
            nuevoAmigo = server.aceptarAmistad(user, username);
            if (nuevoAmigo != null) {
                user.eliminarSolicitud(username);
                if (nuevoAmigo.isConectado())
                    user.anadirAmigo(nuevoAmigo);
                else user.anadirAmigo(username);
            }
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean rechazarAmistad(String usuario) {
        if (server.rechazarAmistad(user.getUsername(), usuario)) {
            user.eliminarSolicitud(usuario);
        }
        return false;
    }

    public boolean eliminarAmigo(String usuario) {
        try {
            if (server.eliminarAmigo(user, usuario)) {
                user.eliminarAmigo(usuario);
            }
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
        return false;

    }

    public String getNombreUsuario() {
        return user.getUsername();
    }
}
