import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.PriorityQueue;

public class TokenManagerImpl extends UnicastRemoteObject implements TokenManager {
    private static final long serialVersionUID = 1L;
    private int currentTokenHolder;
    private PriorityQueue<ProcessRequest> requestQueue;

    public TokenManagerImpl() throws RemoteException {
        currentTokenHolder = -1;  // No token holder initially
        requestQueue = new PriorityQueue<>((r1, r2) -> {
            // Compare based on sequence number first, then process ID
            if (r1.sequenceNumber == r2.sequenceNumber) {
                return Integer.compare(r1.processId, r2.processId);
            } else {
                return Integer.compare(r1.sequenceNumber, r2.sequenceNumber);
            }
        });
    }

    @Override
    public void requestEntry(int processId, int sequenceNumber) throws RemoteException {
        ProcessRequest request = new ProcessRequest(processId, sequenceNumber);
        requestQueue.add(request);

        // Check if the current process is eligible for the token
        if (currentTokenHolder == -1 || isHigherPriority(request)) {
            // If no token holder or this process has higher priority
            currentTokenHolder = request.processId;
            System.out.println("Granting token to process " + processId);
        }
    }

    @Override
    public void releaseToken(int processId, int sequenceNumber) throws RemoteException {
        // Process is done, remove it from the request queue
        requestQueue.removeIf(r -> r.processId == processId);

        if (!requestQueue.isEmpty()) {
            // Select the next eligible process based on Suzuki-Kasami's priority rules
            ProcessRequest nextRequest = requestQueue.poll();  // Get and remove the highest priority request
            currentTokenHolder = nextRequest.processId;
            System.out.println("Granting token to process " + nextRequest.processId);
        }
    }

    // Method to get the current token holder
    @Override
    public int getCurrentTokenHolder() throws RemoteException {
        return currentTokenHolder;
    }

    private boolean isHigherPriority(ProcessRequest request) {
        // Check if the request has higher priority based on the sequence number and process ID
        if (requestQueue.isEmpty()) {
            return true;
        }
        ProcessRequest highestPriorityRequest = requestQueue.peek();
        return request.sequenceNumber < highestPriorityRequest.sequenceNumber ||
               (request.sequenceNumber == highestPriorityRequest.sequenceNumber &&
               request.processId < highestPriorityRequest.processId);
    }

    private static class ProcessRequest {
        int processId;
        int sequenceNumber;

        ProcessRequest(int processId, int sequenceNumber) {
            this.processId = processId;
            this.sequenceNumber = sequenceNumber;
        }
    }
}
