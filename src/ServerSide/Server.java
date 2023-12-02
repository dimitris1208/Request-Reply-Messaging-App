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

        ServerSocket serverSocket = null;
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
            while (true) {
                //socket object that receives client requests
                Socket client = serverSocket.accept();
                //create a new thread object of runnable
                ClientHandler clientSock = new ClientHandler(client);
                new Thread(clientSock).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //----------------------------------CLIENT HANDLER--------------------------------------------------------------


    private static class ClientHandler implements Runnable {
        int function;
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {

            BufferedReader in = null;
            PrintWriter out = null;
            try {

                //input stream of client
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                try { // get the function first
                    function = Integer.parseInt(in.readLine());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                int token = 0; //it is used in many cases
                int messageId = 0; // it is used in 2 cases

                switch (function) { // discrete cases based on function number
                    case 1: // create account cas
                        createAccount(in.readLine(), out);
                        break;

                    case 2: // show accounts case
                        try {
                            token = Integer.parseInt(in.readLine());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        showAccounts(token, out);
                        break;

                    case 3: //send message case
                        try {
                            token = Integer.parseInt(in.readLine());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        String recipient = in.readLine();
                        String message = in.readLine();
                        sendMessage(token, out, recipient, message);
                        break;

                    case 4: // show inbox case
                        try {
                            token = Integer.parseInt(in.readLine());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        showInbox( token , out );
                        break;

                    case 5 : //read message case
                        try {
                            token = Integer.parseInt(in.readLine());
                            messageId = Integer.parseInt(in.readLine());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        readMessage(token , out , messageId);
                        break;

                    case 6 : // delete message case
                        try {
                            token = Integer.parseInt(in.readLine());
                            messageId = Integer.parseInt(in.readLine());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        deleteMessage(token , out , messageId);
                        break;

                }


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    // Close the client socket in the 'finally' block to ensure it is closed even if an exception occurs
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //--------------------------------------IS VALID--------------------------------------------------------------


    static boolean isValid(String username) {
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
    static boolean isUnique(String name) {
        for (Account account : Accounts) {
            if (account.getUsername().equals(name)) {
                return false;
            }
        }
        return true;
    }


    //-------------------------------------CREATE ACCOUNT-----------------------------------------------------------


    //checks if username is valid and unique and appends it on the Account List and returns authToken
    static void createAccount(String username, PrintWriter out) {
        if (isValid(username)) {
            if (isUnique(username)) {
                // is valid and unique so appends it on list
                Account account = new Account(username);
                Accounts.add(account);
                //returns authToken to client
                out.println(account.getAuthToken());
                return;
            } else {
                out.println("Sorry, the user already exists");
            }
        } else {
            out.println("Invalid Username");
        }
    }


    //-----------------------------------------SHOW ACCOUNTS----------------------------------------------

    static void showAccounts(int token, PrintWriter out) {
        for (Account account : Accounts) {
            if (token == account.getAuthToken()) {
                int counter = 0;
                for (Account otherPeople : Accounts) {
                    if (!otherPeople.getUsername().equals(account.getUsername())) {// if the otherPeople variable's value is not the same as the account's value
                        counter++;
                        out.println(counter + "." + " " + otherPeople.getUsername());   // print username
                    }
                }
                return;
            }

        }
        out.println("Invalid Auth Token");
        return;
    }

    //--------------------------------------------SEND MESSAGE---------------------------------------------

    static void sendMessage(int token, PrintWriter out, String recipient, String message_body) {
        for (Account account : Accounts) {        //it checks all the accounts
            // if the authToken is correct it will get into the if statement
            if (token == account.getAuthToken()) {
                // it checks if the recipient exists
                for (Account receiver : Accounts) {
                    // if exist gets into if statement
                    if (receiver.getUsername().equals(recipient)) {
                        // Create message instance with the wanted values
                        Message message = new Message(account.getUsername(), receiver.getUsername(), message_body);
                        receiver.addMessage(message);  // add message to messagebox of the recipient
                        out.println("OK");
                        return;
                    }
                }
                out.println("User does not exist");
                return;
            }
        }
        out.println("Invalid Auth Token");
        return;
    }

    //-----------------------------------------SHOW INBOX-------------------------------------------------


    static void showInbox(int token, PrintWriter out) {
        for (Account account : Accounts) { //it checks all the accounts
            // if the authToken is correct it will get into the if statement
            if(token == account.getAuthToken()) {
                for (Message message : account.messageBox) { // reads messagebox from account object
                    if (message.getStatus()){
                        out.println(message.getId()+"."+" from : "+message.getSender());// sends back to client
                    }
                    else {
                        out.println(message.getId()+"."+" from : "+message.getSender()+"*"); // sends back to client as unread , with *
                    }
                }
                return;

            }
        }
        out.println("Invalid Auth Token");
        return;
    }

    //-----------------------------------------READ MESSAGE-------------------------------------------------

    static void readMessage(int token, PrintWriter out, int messageId) {
        for (Account account : Accounts) { //it checks all the accounts
            // if the authToken is correct it will get into the if statement
            if(token == account.getAuthToken()) {
                for (Message message : account.messageBox){ // reads messagebox from account object

                    // if messageId is the same with the one in parameters it will get into if statement
                    if(message.getId() == messageId){
                        out.println( "(" + message.getSender() + ")" + message.getBody()); //returns message
                        message.read(); // change status of message from not read to read
                        return;
                    }
                }
                out.println("Message ID does not exist");
                return;

            }
        }
        out.println("Invalid Auth Token");
        return;
    }

    //-----------------------------------------DELETE MESSAGE-------------------------------------------------

    static void deleteMessage(int token, PrintWriter out, int messageId) {
        for (Account account : Accounts) { //it checks all the accounts
            // if the authToken is correct it will get into the if statement
            if(token == account.getAuthToken()) {
                for (Message message : account.messageBox){ // reads messagebox from account object

                    // if messageId is the same with the one in parameters it will get into if statement
                    if(message.getId() == messageId){
                        account.removeMessage(message); // deletes messages
                        out.println("OK");
                        return;
                    }
                }
                out.println("Message ID does not exist");
                return;

            }
        }
        out.println("Invalid Auth Token");
        return;
    }

}

