public class Message {
    boolean isRead;
    String sender ;
    String receiver;
    String body ; // The actual message
    Message (String sender , String receiver , String body){ // constructor of the class
        isRead = false;
        this.sender = sender ;
        this.receiver = receiver ;
        this.body = body ;

    }

}
