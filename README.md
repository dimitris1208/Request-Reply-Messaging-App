# Request-Reply-Messaging-App
A distributed exchange system messaging that uses a simple request-reply protocol. Clients send to Server a Request and then the Server responds with a Response and their connection terminates.

HOW TO USE IT :
1.  java server < port_number > ,run server at first passing argument port number
2.  java client <ip_number> <port_number> <FN_ID> <args> , run client passing arguments <ip_number> (usually localhost) , port number the same as above so the server can hear it , Function Id which determines what to do , and finally some arguments
3.  There are 6 discrete cases :

    -Create Account (FN_ID = 1) , java client <ip_number> <port_number> 1 <user_name> , it returns an authToken , that is needed
    for every action of the user , after the creation of the account .
    
    -Show Accounts (FN_ID = 2) , java client <ip_number> <port_number> 2 <auth_Token> ,it returns the other users' accounts
    
    -Send Message (FN_ID = 3) , java client <ip>_number <port_number> 3 <auth_Token> <recipient_name> <message_body> , it sends a message to
    the user recipient
     
    -Show Inbox (FN_ID = 4) , java client <ip_number> <port_number> 4 <auth_Token> , it returns the messages sent to the user by other
    users
    
    -Read Message (FN_ID = 5) , java client <ip_number> <port_number> 5 <auth_Token> <message_id> , it returns the message with id
    message_id
    
    -Delete Message (FN_ID = 6) , java client <ip_number> <port_number> 6 <auth_Token> <message_id> , it deletes the message with id
    message_id


