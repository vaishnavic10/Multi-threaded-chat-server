import java.io.*;
import java.net.*;
import java.util.Set;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private Set<ClientHandler> clients;

    public ClientHandler(Socket socket, Set<ClientHandler> clients) {
        this.socket = socket;
        this.clients = clients;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("Welcome to the chat server!");
        } catch (IOException e) {
            System.out.println("ClientHandler Error: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        String message;
        try {
            while ((message = reader.readLine()) != null) {
                broadcastMessage("Client[" + socket.getPort() + "]: " + message);
            }
        } catch (IOException e) {
            System.out.println("Client disconnected: " + socket.getPort());
        } finally {
            try {
                socket.close();
                clients.remove(this);
            } catch (IOException e) {
                System.out.println("Cleanup Error: " + e.getMessage());
            }
        }
    }

    private void broadcastMessage(String message) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != this) {
                    client.writer.println(message);
                }
            }
        }
    }
}


