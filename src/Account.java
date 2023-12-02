package ServerSide;

import java.util.*;

public class Account {
    String username ;
    final int authToken ; // authentication number
    List <Message> messageBox ;
    Random random = new Random() ;

    public Account(String username){
        this.username = username ;
        authToken = random.nextInt(5001) + 1000 ; // generate a random number between 1000 and 6000
        messageBox = new List<Message>() ;
    }

     public String getUsername(){
        return this.username;
    }

    public int getAuthToken() {
        return authToken;
    }
}
