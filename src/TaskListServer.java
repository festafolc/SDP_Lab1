import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskListServer {
    public static void main(String[] args) throws IOException {

        final int port = 5000;
        DataOutputStream out;
        DataInputStream in;

        ArrayList<String> tasks = new ArrayList();

        tasks.add("Caminar mucho");
        tasks.add("Nadar mucho");


        ServerSocket skServer = new ServerSocket(port);
        Socket client = skServer.accept(); //Server accepts the connection with the client

        while (true) {
            out = new DataOutputStream(client.getOutputStream());
            in = new DataInputStream(client.getInputStream());

            String clientMessage = in.readUTF(); //Server reads the message from client
            System.out.println(clientMessage);
            switch (clientMessage){
                case "L":
                    String showList = Arrays.toString(tasks.toArray());
                    out.writeUTF(showList);
                    break;
                case "R":
                    String newTask = in.readUTF();
                    tasks.add(newTask);
                    out.writeUTF("Task added succesfully");
                    break;
                case "Q":
                    out.writeUTF("Adiós cliente, ya te he servido.");
                    out.close();
                    in.close();
                    client.close();
                    break;
                default:
                    out.writeUTF("Cliente, esa opción no es válida.");
            }
        }
    }
}