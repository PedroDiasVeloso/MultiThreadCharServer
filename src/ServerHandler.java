import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class ServerHandler implements Runnable {

    Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private static List<ServerHandler> clientList = Collections.synchronizedList(new ArrayList<>());
    //private  List<ServerHandler> clientList;



    public ServerHandler(Socket clientSocket){
        this.clientSocket = clientSocket;
        clientList.add(this);

    }
    @Override
    public void run() {
        try {
            receiveMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void receiveMessage() throws IOException {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            out = new PrintWriter(clientSocket.getOutputStream(),true);


        } catch (IOException e) {
            e.printStackTrace();
        }

        while(!clientSocket.isClosed()) {

            String message = null;
            String toShutDown = null;


            try {
                message = in.readLine();
                if(message==null){
                    clientSocket.close();
                }
                else {
                    toShutDown = message.split(" ")[2];
                }
            } catch (IOException e) {
                System.out.println("Someone has left the chat!");
            }

            if(message == null){
                clientSocket.close();
            }
            else {
                //se nao for quit manda para todos e verifica o que é
                if (!toShutDown.equals("quit")) {

                    String[] iPrivate = message.split(" ");
                    String ifPrivate = iPrivate[0];
                    sendToAll(message);

                   // se for kick remove o cliente da lista e avisa o server
                    if(message.split(" ")[2].equals("kick")){
                        clientList.remove(this);
                        System.out.println(message.split(" ")[3] + " was kicked.");
                        continue;
                    }

                    // se for private nao imprime no server
                    else if(message.split(" ")[2].equals("private")){
                        continue;
                    }

                    // se for changeName nao imprime
                    else if(message.split(" ")[2].equals("changeName")){
                        continue;
                    }
                    //se for normal imprime
                    else {
                        System.out.println(message);
                    }


                }


                else {
                    try {
                        System.out.println(message.split(" ")[0]+ " has left the chat!");
                        clientSocket.close();
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        }
    }

    public void sendToAll(String message) {
            for (ServerHandler rcv : clientList) {

                rcv.out.println(message);


            }
        }
    }
