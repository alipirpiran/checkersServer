package sample.Server;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    public MessageType messageType;

    // game fields
    public int oldX,oldY;
    public int newX,newY;

    // message fields
    public String msgText;
    public String msgTime;
    public String msgSenderName;


    //start game fields
    public boolean startGame = false;
    public String localUsername;
    public String targetUser;

    //online users fields
    public ArrayList<String> usersnames;

    // connect To sample.Server Fields
    public String name;
    public boolean accepted = false;






}
