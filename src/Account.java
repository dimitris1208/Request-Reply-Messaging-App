import java.util.List;
import java.util.Random;

public class Account {
    String username ;
    private final int authToken ; // authentication number
    List <Message> messageBox ;
    Random random = new Random() ;

    Account(String username){
        this.username = username ;
        authToken = random.nextInt(5001) + 1000 ; // generate a random number between 1000 and 6000

    }
}
