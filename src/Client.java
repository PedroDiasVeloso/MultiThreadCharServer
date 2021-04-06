import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {

        chatUser();

    }

    private static void chatUser() throws IOException {
        Scanner port = new Scanner(System.in);
        System.out.print("Port: ");
        String thePort = port.nextLine();

        System.out.print("Host: ");
        String theHost = port.nextLine();

        System.out.print("ChatName: ");
        String theChatName = port.nextLine();


        Socket clientSocket = new Socket(theHost, Integer.parseInt(thePort));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String theMessage = port.nextLine();


        Thread thread = new Thread(new sendMessage(clientSocket,theChatName,theMessage,out));
        thread.setName(theChatName);
        thread.start();


        while(!clientSocket.isClosed()){
            if(clientSocket.isClosed()){
                System.out.println("");
                break;
            }
            String response = in.readLine();

            //if null fecha logo
            if(response == null){
                clientSocket.close();
                break;
            }

            //se for kick avisa que foi kickado e fecha
            else if(response.split(" ")[2].equals("kick") && response.split(" ")[3].equals(theChatName)){
                System.out.println("You have been kicked!");
                clientSocket.close();
                break;

            }



            //se for private mas nao pra ele nao imprime
            else if(response.split(" ")[2].equals("private") && !response.split(" ")[3].equals(theChatName)){
                continue;
            }


            // se for private e pra ele imprime
            else if(response.split(" ")[2].equals("private") && response.split(" ")[3].equals(theChatName)){
                String[] responseArray = response.split(" ");
                String result = "";

                for ( int i = 0; i < responseArray.length; i++){
                    if(i==2){
                        continue;
                    }
                    else if(i==3){
                        continue;
                    }
                    else{
                        result+=responseArray[i]+" ";
                    }

                }
                System.out.println(result);
            }

            //se for para mudar o nome altera-o
            else if(response.split(" ")[2].equals("changeName") && response.split(" ")[0].equals(theChatName)){
                thread.setName(response.split(" ")[3]);
                theChatName = response.split(" ")[3];
            }

            else if(response.split(" ")[2].equals("changeName") && !response.split(" ")[0].equals(theChatName)){
                continue;
            }


            // se for simplesmente normal imprime
            else {
                String[] responseArray = response.split(" ");
                if (!responseArray[0].equals(theChatName)) {
                    System.out.println(response);
                }
            }
        }




    }



}