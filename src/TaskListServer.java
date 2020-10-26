import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;


public class TaskListServer {
    public static void main(String[] args) throws IOException {
        ArrayList<String> tasks = new ArrayList();
        FileWriter txt = new FileWriter("output.txt", true);
        ServerSocket skServer = new ServerSocket(5000);
        Socket client = null;

        while (true) {
            try {
                client = skServer.accept();
                DataOutputStream out = new DataOutputStream(client.getOutputStream());
                DataInputStream in = new DataInputStream(client.getInputStream());
                Thread newThread = new ClientHandler(client, in, out, tasks, txt);
                newThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (client == null) { // A ideia era fechar o servidor após todos os clientes fecharem,
                skServer.close(); // mas o programa está em estado bloqueante na linha 17.
                break;
            }
        }
    }
}

class ClientHandler extends Thread {
    DataInputStream dataIn;
    DataOutputStream dataOut;
    Socket client;
    ArrayList<String> tasks;
    FileWriter txt;

    public ClientHandler(Socket client, DataInputStream dataIn, DataOutputStream dataOut, ArrayList<String> tasks, FileWriter txt) {
        this.client = client;
        this.dataIn = dataIn;
        this.dataOut = dataOut;
        this.tasks = tasks;
        this.txt = txt;
    }

    @Override
    public void run (){
        String clientMessage = null;

        while(true){
            try{
                clientMessage = dataIn.readUTF();
                switch (clientMessage){
                    case "L":
                        String showList = Arrays.toString(this.tasks.toArray());
                        dataOut.writeUTF(showList);
                        break;
                    case "R":
                        String newTask = dataIn.readUTF();
                        if (newTask.length() <= 120){
                            this.tasks.add(newTask);
                            this.txt.write(newTask);
                            dataOut.writeUTF("Task added succesfully");
                        } else {
                            dataOut.writeUTF("Caracters cannot be over than 120");
                        }
                        break;
                    case "Q":
                        this.txt.close();
                        this.dataOut.close();
                        this.dataIn.close();
                        this.client.close();
                        break;
                    default:
                        dataOut.writeUTF("This option is not valid");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (clientMessage.equals("Q")){
                break;
            }
        }
    }
}
