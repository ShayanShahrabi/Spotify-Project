package Server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    private static Response processRequest(Request request) {
        // TODO: Implement the logic to process the client request and generate the appropriate response
        return null;
    }

    private static void handleClient(Socket clientSocket) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

            // Create Gson object for JSON serialization/deserialization
            Gson gson = new GsonBuilder().create();

            String clientMessage;
            while ((clientMessage = reader.readLine()) != null) {
                // Deserialize the client request from JSON to Request object
                Request request = gson.fromJson(clientMessage, Request.class);

                // Process the request and generate the response
                Response response = processRequest(request);

                // Serialize the response to JSON
                String jsonResponse = gson.toJson(response);

                // Send the JSON response back to the client
                writer.println(jsonResponse);
            }

            reader.close();
            writer.close();
            clientSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        handleClient(clientSocket);
    }
}

public class Server {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                // Create a new thread for the client using the ClientHandler class
                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
