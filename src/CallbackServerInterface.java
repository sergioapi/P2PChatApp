import java.rmi.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface CallbackServerInterface extends Remote {
    // metodo para obtener el objeto remoto de otro cliente
    String sayHello() throws RemoteException;

    void registrarCliente(CallbackClientInterface cliente) throws RemoteException;

    void suprimirCliente(CallbackClientInterface cliente) throws RemoteException;

    void enviarContacto(CallbackClientInterface usuarioEnvia, String usuarioEnviar, String direccionObjeto) throws java.rmi.RemoteException;

    void getCantidadClientes(CallbackClientInterface cliente) throws RemoteException;

    void getListaUsuarios(CallbackClientInterface cliente) throws RemoteException;

    void crearGrupoAmistad(String groupName) throws RemoteException;

    void agregarAmistad(String usuario1, String usuario2) throws RemoteException;

    Set<String> obtenerAmistades(String groupName) throws RemoteException;
}
