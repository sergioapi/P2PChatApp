import java.rmi.Remote;

public interface MensajeroInterface extends Remote {
    public String recibirMsj(String msj) throws java.rmi.RemoteException;

    ;
}
