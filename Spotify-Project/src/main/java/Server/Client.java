package Server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        try {
            // Connect to the server
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            System.out.println("Connected to server: " + SERVER_IP + ":" + SERVER_PORT);

            // Create BufferedReader for reading user input
            BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));

            // Create PrintWriter for sending requests to the server
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // Create BufferedReader for reading responses from the server
            BufferedReader serverResponseReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Create Gson object for JSON serialization/deserialization
            Gson gson = new GsonBuilder().create();

            // Create and send a request to the server
            Request request = new Request("GET", "/songs", "");
            String jsonRequest = gson.toJson(request);
            writer.println(jsonRequest);

            // Read and print the response from the server
            String serverResponse;
            while ((serverResponse = serverResponseReader.readLine()) != null) {
                Response response = gson.fromJson(serverResponse, Response.class);
                System.out.println("Received response from server:");
                System.out.println("Status: " + response.getStatus());
                System.out.println("Message: " + response.getMessage());
                // TODO: Process the response data according to your application's needs
            }

            // Close the resources
            userInputReader.close();
            writer.close();
            serverResponseReader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

