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
        server.pedirAmistad(user.getUsername(), usuario);
        return true;
    }

    public boolean aceptarAmistad() {
        server.aceptarAmistad(user.getUsername(), );
    }

    public boolean rechazarAmistad(String usuario) {
        server.rechazarAmistad(user.getUsername(), usuario);
        return true;
    }

    public boolean eliminarAmigo() {
        server.eliminarAmigo(user, );

    }

    public String getNombreUsuario() {
        return user.getUsername();
    }
}
