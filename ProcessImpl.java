import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ProcessImpl extends UnicastRemoteObject implements Process {
    private static final long serialVersionUID = 1L;
    private int sequenceNumber;
    private boolean inCriticalSection;
    private TokenManager tokenManager;
    private int processId;

    public ProcessImpl(int processId, TokenManager tokenManager) throws RemoteException {
        this.processId = processId;
        this.tokenManager = tokenManager;
        this.sequenceNumber = 0;
        this.inCriticalSection = false;
    }

    @Override
    public void requestCriticalSection() throws RemoteException {
        sequenceNumber++;  // Increment sequence number for each request
        System.out.println("Process " + processId + " requesting critical section with sequence number " + sequenceNumber);
        tokenManager.requestEntry(processId, sequenceNumber);  // Request entry to the critical section

        // Wait for token to be granted
        while (processId != tokenManager.getCurrentTokenHolder()) {
            try {
                Thread.sleep(100); // Polling delay (simulate waiting)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Once granted, enter critical section
        inCriticalSection = true;
        System.out.println("Process " + processId + " entering critical section.");
    }

    @Override
    public void releaseCriticalSection() throws RemoteException {
        inCriticalSection = false;
        System.out.println("Process " + processId + " leaving critical section.");
        tokenManager.releaseToken(processId, sequenceNumber);
    }

    @Override
    public int getSequenceNumber() throws RemoteException {
        return sequenceNumber;
    }
}
