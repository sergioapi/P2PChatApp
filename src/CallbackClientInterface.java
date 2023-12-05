import java.rmi.*;
public interface CallbackClientInterface extends java.rmi.Remote{
   // metodo para enviar o recibir mensajes

   // metodo para recibir clientes conectados
    public String notifyMe(String message)
            throws java.rmi.RemoteException;
}
