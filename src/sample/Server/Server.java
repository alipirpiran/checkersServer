package sample.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {
    boolean running = true;
    public ServerSocket serverSocket;
    ArrayList<Socket> clients = new ArrayList<>();
    public ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    public ArrayList<String> usernames = new ArrayList<>();

    public Server(int port){
        try {
            serverSocket = new ServerSocket(port);

            System.out.println("server running : " + InetAddress.getLocalHost().getHostAddress() + ":" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (running){
            Socket socket = null;
            try {
                System.out.println("waiting for client to connect");
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            clients.add(socket);
            ClientHandler clientHandler = new ClientHandler(socket, this);
            Thread thread = new Thread(clientHandler);
            thread.setDaemon(true);
            thread.start();

            System.out.println("client accepted : " + socket.getRemoteSocketAddress());
        }
    }




    public void sendMessageToClients(ClientHandler clientHandler, Message message){
        for(ClientHandler clientHandler1:clientHandlers){
            if(clientHandler1 != clientHandler) {
                    clientHandler1.sendMessage(message);
            }
        }
    }

    public void sendMessageToAllClinets(Message message){
        for (ClientHandler clientHandler:clientHandlers) {
                clientHandler.sendMessage(message);
        }
    }

    public void sendMessageToClinet(Message message, String username){
        for (ClientHandler clientHandler:clientHandlers){
            if (clientHandler.username.equals(username))
                clientHandler.sendMessage(message);
        }
    }

    public void sendToLastClient(Message message){
        clientHandlers.get(clientHandlers.size() - 1).sendMessage(message);
    }


    public void start(){
        Thread thread = new Thread(this);
        thread.start();
    }

    public void stop(){
        running = false;
    }

    public boolean hasSameName(String username){
        for (String name:usernames)
            if (name.equals(username))
                return true;

        return false;
    }

    public void sendOnlineUsers(){
        Message message = new Message();
        message.messageType = MessageType.onlineUsers;
        message.usersnames = new ArrayList<>(usernames);

        System.out.println(usernames.size());

        sendMessageToAllClinets(message);
    }

    public void startGame(Message message){
        message.messageType = MessageType.startGame;

        sendMessageToClinet(message, message.targetUser);
        sendMessageToClinet(message, message.localUsername);

    }
}
