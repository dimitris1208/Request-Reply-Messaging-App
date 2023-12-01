import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    public static void main(String[] args) {
        String ip ;
        int port ;
        int function ;

        ip = args[0];
        // transmit string inputs port and function to integers
        try {
            port = Integer.parseInt(args[1]);
            function = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        //establish a connection
        try {
            Socket socket = new Socket(ip,port);
            // writing to server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            // reading from server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // sending the function id to server
            out.println(function);
            switch (function)
            {

                case 1 :
                case 2 :
                case 4 :
                    //gets username ( or authToken if cases 2,4 ) from arguments and sends it to the server
                    out.println(args[3]);
                    break;
                case 3 :
                    //gets authToken from arguments and sends it to server
                    out.println(args[3]);
                    //gets recipient from arguments and sends it to server
                    out.println(args[4]);
                    //gets message from arguments and sends it to server
                    out.println(args[5]);
                    break;
                case 5 :
                case 6 :
                    //gets authToken from arguments and sends it to server
                    out.println(args[3]);
                    //gets message_id from arguments and sends it to server
                    out.println(args[4]);
                    break;

            }

       // displaying server reply
            String serverResponse;
            while ((serverResponse = in.readLine()) != null) {
                System.out.println(serverResponse);
            }

            // Close resources
            out.close();
            in.close();
            socket.close();

        }catch (IOException e ){
            e.printStackTrace();
        }



    }


}
