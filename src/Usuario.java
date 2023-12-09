import java.util.ArrayList;

public class Usuario {

    CallbackClientInterface cliente;
    String username;
    String remoteURL;
    ArrayList<String> amigos;
    ArrayList<Usuario> amigosConectados;
    boolean conectado;

    public Usuario(CallbackClientInterface cliente, String username, String remoteURL, ArrayList<String> amigos, ArrayList<Usuario> amigosConectados) {
        this.cliente = cliente;
        this.username = username;
        this.remoteURL = remoteURL;
        this.amigos = amigos;
        this.amigosConectados = amigosConectados;
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

    public void eliminarAmigo(Usuario usuario) {
        amigos.remove(usuario.getUsername());
        amigosConectados.remove(usuario);
    }
}
