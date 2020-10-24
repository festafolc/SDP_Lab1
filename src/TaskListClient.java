import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;


public class TaskListClient {
    public static void main(String[] args) throws IOException {
        try{
            Scanner scan = new Scanner(System.in);
            InetAddress ip = InetAddress.getByName("localhost");
            Socket skClient = new Socket(ip, 5000);
            DataOutputStream dataOut = new DataOutputStream(skClient.getOutputStream());
            DataInputStream dataIn = new DataInputStream(skClient.getInputStream());

            while(true){
                System.out.println("You are the client, please send a message to the server");
                String clientMessage = scan.nextLine();

                switch (clientMessage){
                    case "L":
                        dataOut.writeUTF("L");
                        break;
                    case "R":
                        dataOut.writeUTF("R");
                        String newTask = scan.nextLine();
                        dataOut.writeUTF(newTask);
                        break;
                    case "Q":
                        dataOut.writeUTF("Q");
                        dataOut.close();
                        dataIn.close();
                        skClient.close();
                        skClient = null;
                        break;
                    default:
                        dataOut.writeUTF(clientMessage);
                }
                if (skClient != null){
                    System.out.println("Below you can see the message from the server");
                    String serverMessage = dataIn.readUTF();
                    System.out.println(serverMessage);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}