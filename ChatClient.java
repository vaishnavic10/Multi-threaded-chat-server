import java.io.*;
import java.net.*;

public class ChatClient {
    public static void main(String[] args) {
        final String SERVER = "localhost";
        final int PORT = 5000;

        try (Socket socket = new Socket(SERVER, PORT);
             BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            Thread readerThread = new Thread(() -> {
                String serverMsg;
                try {
                    while ((serverMsg = reader.readLine()) != null) {
                        System.out.println(serverMsg);
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed.");
                }
            });
            readerThread.start();

            String userMsg;
            while ((userMsg = input.readLine()) != null) {
                writer.println(userMsg);
            }

        } catch (IOException e) {
            System.out.println("Client Error: " + e.getMessage());
        }
    }
}
