import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TokenManager extends Remote {
    void requestEntry(int processId, int sequenceNumber) throws RemoteException;
    void releaseToken(int processId, int sequenceNumber) throws RemoteException;
    int getCurrentTokenHolder() throws RemoteException;  // Method to get current token holder
}
