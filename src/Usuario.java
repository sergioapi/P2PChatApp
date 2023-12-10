import java.util.ArrayList;

public class Usuario {

    CallbackClientInterface cliente;
    String username;
    String remoteURL;
    ArrayList<String> amigos;
    ArrayList<Usuario> amigosConectados;
    ArrayList<String> solicitudesPendientes;
    boolean conectado;

    public Usuario(CallbackClientInterface cliente, String username, String remoteURL, ArrayList<String> amigos, ArrayList<Usuario> amigosConectados, ArrayList<String> solicitudesPendientes) {
        this.cliente = cliente;
        this.username = username;
        this.remoteURL = remoteURL;
        this.amigos = amigos;
        this.amigosConectados = amigosConectados;
        this.solicitudesPendientes = solicitudesPendientes;
    }

    public Usuario(CallbackClientInterface cliente, String username, String remoteURL) {
        this.cliente = cliente;
        this.username = username;
        this.remoteURL = remoteURL;
    }

    public Usuario(String username, boolean conectado) {
        this.username = username;
        this.conectado = conectado;
    }

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
            if (amigo.getUsername().equals(usuario))
                amigosConectados.remove(amigo);
        }
    }

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

    public boolean isConectado() {
        return conectado;
    }
}
