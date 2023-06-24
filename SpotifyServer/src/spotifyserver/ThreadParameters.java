package spotifyserver;

import java.net.Socket;
import java.util.Scanner;

public class ThreadParameters {
    Scanner scannerSocket;
    Socket socket;
    //------------------------------------------------------------
    public ThreadParameters(Scanner scannerSocket, Socket socket) {
        this.scannerSocket = scannerSocket;
        this.socket = socket;
    }

    // Getters and setters 
    //----------------------------------
    public Scanner getScannerSocket() {
        return scannerSocket;
    }

    //----------------------------------
    public void setScannerSocket(Scanner scannerSocket) {
        this.scannerSocket = scannerSocket;
    }
    
    //----------------------------------
    public Socket getSocket() {
        return socket;
    }

    //----------------------------------
    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
