import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class sendMessage implements Runnable {

    Socket clientSocket;
    String theChatName;
    String theMessage;
    PrintWriter out;



    public sendMessage(Socket clientSocket, String theChatName, String theMessage, PrintWriter out){
        this.clientSocket = clientSocket;
        this.theChatName = theChatName;
        this.theMessage = theMessage;
        this.out = out;
    }


    public void sendMessageToServer() throws IOException {
        while(!clientSocket.isClosed()) {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(Thread.currentThread().getName() + " : " + theMessage);

            Scanner message2 = new Scanner(System.in);
            theMessage = message2.nextLine();
        }
    }


    @Override
    public void run() {
        try {
            sendMessageToServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
