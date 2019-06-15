package sample.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{
    boolean running = true;

    public ObjectInputStream objectInputStream;
    public ObjectOutputStream objectOutputStream;
    Socket socket ;
    Server server;
    String username;
    public ClientHandler(Socket socket, Server server){
        this.server = server;
        this.socket = socket;

        server.clientHandlers.add(this);

        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // read messages from server
    @Override
    public void run() {
        while (running){
            try {
                Message message = (Message)objectInputStream.readObject();
                read(message);
                System.out.println("server : received ");

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                stop();
            }
        }
    }

    public void sendMessage(Message message){
        try {
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        running = false;
        try {
            objectInputStream.close();
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read(Message message){
        switch (message.messageType){

            case textMessage:
                this.server.sendMessageToClients(this, message);
                break;

            case gameMessage:
                this.server.sendMessageToClients(this, message);
                break;

            case startGame:
                server.startGame(message);
                break;

            case onlineUsers:
                break;

            case connectToServer:
                Message message1 = new Message();
                message1.messageType = MessageType.connectToServer;

                if (server.hasSameName(message.name)){
                    message1.accepted = false;
                }else {
                    message1.accepted = true;
                    accept(message);
                }

                this.sendMessage(message1);

                break;
        }
    }

    private void accept(Message message){
        server.usernames.add(message.name);
        server.sendOnlineUsers();
        this.username = message.name;
    }




}
