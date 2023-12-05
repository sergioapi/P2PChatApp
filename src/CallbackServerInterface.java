import java.rmi.*;
public interface CallbackServerInterface extends Remote{
   // metodo para obtener el objeto remoto de otro cliente
    public String sayHello( )
            throws java.rmi.RemoteException;
    public void registerForCallback(
            CallbackClientInterface callbackClientObject
    ) throws java.rmi.RemoteException;

// This remote method allows an object client to
// cancel its registration for callback

    public void unregisterForCallback(
            CallbackClientInterface callbackClientObject)
            throws java.rmi.RemoteException;
}
