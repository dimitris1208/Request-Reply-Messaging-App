import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    // initialize socket and input stream
    private ServerSocket serverSocket = null ;

    public Server (int port){
        // start server and wait for connection
        try {
            serverSocket = new ServerSocket(port);
            while(true){
                //socket object that receives client requests
                Socket client = serverSocket.accept();
                //create a new thread object of runnable
                ClientHandler clientSock = new ClientHandler(client);
                new Thread(clientSock).start();
            }
            } catch (IOException e){
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        public ClientHandler(Socket socket){
            this.clientSocket = socket ;
        }
        public void run(){
            BufferedReader in = null;
        }
    }

}
