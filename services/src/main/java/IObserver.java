import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IObserver extends Remote {
    void donatorSaved(Donator donator) throws RemoteException;
    void cazUpdated(CazCaritabil caz) throws RemoteException;
}
