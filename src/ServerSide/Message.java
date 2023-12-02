package ServerSide ;

import java.util.Random;

public class Message {
    private boolean isRead;
    private String sender ;
    private String receiver;
    private String body ; // The actual message
    private final int id ;
    Random random = new Random() ;
    Message (String sender , String receiver , String body){ // constructor of the class
        isRead = false;
        this.sender = sender ;
        this.receiver = receiver ;
        this.body = body ;
        id = random.nextInt(100); // message_id is a random number from 0 to 299
    }

    public int getId(){
        return id;
    }

    public String getSender(){
        return sender;
    }

    public String getBody(){
        return body;
    }

    public boolean getStatus(){
        return isRead ;
    }
    public void read(){
        isRead = true ;
    }
}
