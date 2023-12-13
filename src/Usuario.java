import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Usuario implements Serializable {

    // Interfaz de cliente para los callbacks
    CallbackClientInterface cliente;

    // Nombre de usuario
    String username;

    // URL remota del usuario para RMI
    String remoteURL;

    // Lista de amigos del usuario
    ArrayList<String> amigos;

    // Lista de amigos conectados (usuarios)
    ArrayList<Usuario> amigosConectados;

    // Lista de solicitudes pendientes de amistad
    ArrayList<String> solicitudesPendientes;

    // Estado de conexión del usuario
    boolean conectado;

    // URL base para la creación de la URL remota
    private final static String URL = "rmi://localhost:1099/";

    // Interfaz de mensajería del usuario
    MensajeroInterface mensajero;

    // Constructores para diferentes situaciones

    public Usuario(CallbackClientInterface cliente, String username, ArrayList<String> amigos, ArrayList<Usuario> amigosConectados, ArrayList<String> solicitudesPendientes, boolean conectado) {
        this.cliente = cliente;
        this.username = username;
        this.remoteURL = URL + username;
        this.amigos = amigos;
        this.amigosConectados = amigosConectados;
        this.solicitudesPendientes = solicitudesPendientes;
        this.conectado = conectado;
    }

    public Usuario(CallbackClientInterface cliente, String username, ArrayList<String> amigos, ArrayList<Usuario> amigosConectados, ArrayList<String> solicitudesPendientes) {
        this.cliente = cliente;
        this.username = username;
        this.remoteURL = URL + username;
        this.amigos = amigos;
        this.amigosConectados = amigosConectados;
        this.solicitudesPendientes = solicitudesPendientes;
        conectado = true;
    }

    public Usuario(CallbackClientInterface cliente, String username, boolean conectado) {
        this.cliente = cliente;
        this.username = username;
        this.remoteURL = URL + username;
        this.conectado = conectado;
        this.amigos = new ArrayList<>();
        this.amigosConectados = new ArrayList<>();
        this.solicitudesPendientes = new ArrayList<>();
    }

    public Usuario(CallbackClientInterface cliente, String username) {
        this.cliente = cliente;
        this.username = username;
        this.remoteURL = URL + username;
        conectado = true;
        this.amigos = new ArrayList<>();
        this.amigosConectados = new ArrayList<>();
        this.solicitudesPendientes = new ArrayList<>();
    }

    public Usuario(String username, boolean conectado) {
        this.username = username;
        this.conectado = conectado;
        this.amigos = new ArrayList<>();
        this.amigosConectados = new ArrayList<>();
        this.solicitudesPendientes = new ArrayList<>();
    }

    // Métodos de acceso y modificación

    public CallbackClientInterface getCliente() {
        return cliente;
    }

    public void setCliente(CallbackClientInterface cliente) {
        this.cliente = cliente;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRemoteURL() {
        return remoteURL;
    }

    public void setRemoteURL(String remoteURL) {
        this.remoteURL = remoteURL;
    }

    public ArrayList<String> getAmigos() {
        return amigos;
    }

    public void setAmigos(ArrayList<String> amigos) {
        this.amigos = amigos;
    }

    public ArrayList<Usuario> getAmigosConectados() {
        return amigosConectados;
    }

    public void setAmigosConectados(ArrayList<Usuario> amigosConectados) {
        this.amigosConectados = amigosConectados;
    }

    // Métodos para gestionar la lista de amigos

    public void anadirAmigoConectado(Usuario amigo) {
        if (amigo != null) if (amigos.contains(amigo.getUsername())) amigosConectados.add(amigo);
    }

    public void amigoDesconectado(Usuario amigo) {
        if (amigo != null) amigosConectados.remove(amigo);
    }

    public void anadirAmigo(Usuario usuario) {
        amigos.add(usuario.getUsername());
        amigosConectados.add(usuario);
    }

    public void anadirAmigo(String username) {
        amigos.add(username);
    }

    public void eliminarAmigo(String usuario) {
        amigos.remove(usuario);
        for (Usuario amigo : amigosConectados) {
            if (amigo.getUsername().equals(usuario)) amigosConectados.remove(amigo);
        }
    }

    // Métodos para gestionar la lista de solicitudes pendientes

    public void anadirSolicitud(String username) {
        solicitudesPendientes.add(username);
    }

    public void eliminarSolicitud(String username) {
        solicitudesPendientes.remove(username);
    }

    public ArrayList<String> getSolicitudesPendientes() {
        return solicitudesPendientes;
    }

    public void setSolicitudesPendientes(ArrayList<String> solicitudesPendientes) {
        this.solicitudesPendientes = solicitudesPendientes;
    }

    // Obtener un usuario amigo por su nombre de usuario

    public Usuario getAmigo(String username) {
        Usuario usuario = null;
        for (Usuario amigo : amigosConectados) {
            if (amigo.getUsername().equals(username)) {
                usuario = amigo;
                break;
            }
        }
        if (usuario == null) if (amigos.contains(username)) {
            usuario = new Usuario(username, false);
        }
        return usuario;
    }

    // Métodos para verificar y gestionar el estado de conexión

    public boolean isConectado() {
        return conectado;
    }

    public void recibirMensaje(String mensaje, String amigo) throws RemoteException {
        mensajero.recibirMsj(mensaje, amigo);
    }

    public MensajeroInterface getMensajero() {
        return mensajero;
    }

    public void setMensajero(MensajeroInterface mensajero) {
        this.mensajero = mensajero;
    }

    public boolean chatIniciado() {
        return mensajero != null;
    }

    public void desconectar() {
        conectado = false;
    }
}

