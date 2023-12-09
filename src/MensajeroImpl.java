import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MensajeroImpl extends UnicastRemoteObject
        implements MensajeroInterface {
    public MensajeroImpl() throws RemoteException {
        super();
    }

    public String recibirMsj(String msj) {
        return msj;
    }
}
