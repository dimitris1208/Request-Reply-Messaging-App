package ServerSide;

import java.util.*;


public class Account {
    private final String username ;
    private final int authToken ; // authentication number
    protected  List <ServerSide.Message> messageBox ;
    Random random = new Random() ;

    public Account(String username){
        this.username = username ;
        authToken = random.nextInt(5001) + 1000 ; // generate a random number between 1000 and 6000
        messageBox = new ArrayList<Message>() ;


    }

     public String getUsername(){
        return this.username;
    }

    public int getAuthToken() {
        return authToken;
    }

    public void addMessage(Message message){
        messageBox.add(message);
    }

    public void removeMessage(Message message){
        messageBox.remove(message);
    }
}
