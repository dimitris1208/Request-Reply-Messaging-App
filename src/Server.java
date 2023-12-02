package ServerSide;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server {

    public static List<Account> Accounts = Collections.synchronizedList(new ArrayList<>());


    public static void main(String[] args) {
        // create a list of accounts

        ServerSocket serverSocket = null ;
        // we get the port from argument
        int port ;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
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
        int function ;
        private Socket clientSocket;
        public ClientHandler(Socket socket){
            this.clientSocket = socket ;
        }
        public void run(){

            BufferedReader in = null;
            try {

                // get the input stream of client
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                try { // get the function first
                   function  = Integer.parseInt(in.readLine());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                switch (function){
                    case 1 :
                        createAccount(in.readLine());
                        break;

                }



            }
        }
    }

    static boolean isValid(String username){
        // Check if the input string is not null
        if (username == null) {
            return false;
        }

        // Check if each character in the string is a letter, underscore, or number
        for (char c : username.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && c != '_') {
                return false;
            }
        }

        // If all characters are letters, digits, or underscores, the string is valid
        return true;
    }
    // looks in the List of Accounts to see if another user has the same name
    boolean isUnique(String name){
        for (Account account : Accounts){
            if (account.getUsername().equals(name)){
                return false ;
            }
        }
        return true ;
    }

    //checks if username is valid and unique and appends it on the Account List
    static void createAccount (String username){
        if (isValid(username)){
            if (isUnique(username)){
               Account account = new Account (username);
               Accounts.add(account);
               return;
            }else {
                System.out.println("Sorry, the user already exists");
            }
        } else {
        System.out.println("Invalid Username");
        }
    }

}
