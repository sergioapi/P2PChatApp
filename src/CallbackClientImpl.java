import java.rmi.*;
import java.rmi.server.*;
public class CallbackClientImpl extends UnicastRemoteObject
        implements CallbackClientInterface{
    public CallbackClientImpl() throws RemoteException {
        super( );
    }

    @Override
    public void amigoConectado(Usuario usuario) throws RemoteException {

    }

    @Override
    public void amigoDesconectado(Usuario usuario) throws RemoteException {

    }
}
