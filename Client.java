import java.rmi.Naming;
import java.rmi.RemoteException;

public class Client {
    public static void main(String[] args) {
        try {
            // Get a reference to the TokenManager object from the RMI registry
            TokenManager tokenManager = (TokenManager) Naming.lookup("rmi://localhost/TokenManager");

            // Create Process objects for each client, passing the shared TokenManager
            Process process1 = new ProcessImpl(1, tokenManager);
            Process process2 = new ProcessImpl(2, tokenManager);
            Process process3 = new ProcessImpl(3, tokenManager);

            // Each process requests and releases the critical section
            new Thread(() -> {
                try {
                    process1.requestCriticalSection();
                    process1.releaseCriticalSection();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }).start();

            new Thread(() -> {
                try {
                    process2.requestCriticalSection();
                    process2.releaseCriticalSection();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }).start();

            new Thread(() -> {
                try {
                    process3.requestCriticalSection();
                    process3.releaseCriticalSection();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
