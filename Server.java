import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.net.MalformedURLException;

public class Server {
    public static void main(String[] args) {
        try {
            // Start the RMI registry on port 1099
            LocateRegistry.createRegistry(1099);

            // Create the TokenManager and bind it to the RMI registry
            TokenManager tokenManager = new TokenManagerImpl();
            Naming.rebind("rmi://localhost/TokenManager", tokenManager);  // Bind with proper URL format

            System.out.println("Token Manager is ready.");
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
