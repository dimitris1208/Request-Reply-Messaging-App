package ServerSide;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


public class Server {

    public static List<Account> Accounts = Collections.synchronizedList(new ArrayList<>());



    //-------------------------------------MAIN-------------------------------------------------------------------


    public static void main(String[] args) {
        // create a list of accounts

        ServerSocket serverSocket = null ;
        // we get the port from argument
        int port = 0;
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


    //----------------------------------CLIENT HANDLER--------------------------------------------------------------



    private static class ClientHandler implements Runnable {
        int function ;
        private Socket clientSocket;
        public ClientHandler(Socket socket){
            this.clientSocket = socket ;
        }
        public void run(){

            BufferedReader in = null;
            PrintWriter out = null ;
            try {

                //input stream of client
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                try { // get the function first
                   function  = Integer.parseInt(in.readLine());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                switch (function){
                    case 1 :
                        createAccount(in.readLine(),out);
                        break;
                    case 2 :
                        int token=0;
                        try {
                            token = Integer.parseInt(in.readLine());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        showAccounts(token,out);
                        break;

                }



            } catch (IOException e){
                e.printStackTrace();
            }finally {
                try {
                    // Close the client socket in the finally block to ensure it is closed even if an exception occurs
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //--------------------------------------IS VALID--------------------------------------------------------------



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

    //-------------------------------------------IS UNIQUE-----------------------------------------------------

    // looks in the List of Accounts to see if another user has the same name
    static boolean isUnique(String name){
        for (Account account : Accounts){
            if (account.getUsername().equals(name)){
                return false ;
            }
        }
        return true ;
    }





    //-------------------------------------CREATE ACCOUNT-----------------------------------------------------------




    //checks if username is valid and unique and appends it on the Account List and returns authToken
    static void createAccount (String username,PrintWriter out){
        if (isValid(username)){
            if (isUnique(username)){
                // is valid and unique so appends it on list
               Account account = new Account (username);
               Accounts.add(account);
               //returns authToken to client
               out.println(account.getAuthToken());
               return;
            }else {
                out.println("Sorry, the user already exists");
            }
        } else {
        out.println("Invalid Username");
        }
    }


    //-----------------------------------------SHOW ACCOUNTS----------------------------------------------

    static void showAccounts(int token , PrintWriter out){
        for(Account account : Accounts){
            if(token == account.getAuthToken()){
                int counter = 0;
                for (Account otherPeople : Accounts){
                    if (!otherPeople.getUsername().equals(account.getUsername())){// if the otherPeople variable's value is not the same as the account's value
                        counter++;
                        out.println(counter+"."+" "+otherPeople.getUsername());   // print username
                    }
                }
                return;
            }

        }
        out.println("Invalid Auth Token");
        return;
    }


}
