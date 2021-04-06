import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {

    public static void main(String[] args){
        Server server = new Server();

        server.nextText();

    }

    private void nextText() {
        Scanner port = new Scanner(System.in);

        System.out.print("Port: ");

        String thePort = port.nextLine();

        ServerSocket socket = null;
        try {
            socket = new ServerSocket(Integer.parseInt(thePort));
        } catch (IOException e) {
            e.printStackTrace();
        }



        while (!socket.isClosed()) {

            Socket clientSocket = null;

            try {
                clientSocket = socket.accept();
                ServerHandler serverHandler = new ServerHandler(clientSocket);
                System.out.println("added one");

                Thread thread = new Thread(serverHandler);

                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }



    }
}
