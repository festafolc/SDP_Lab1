import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;


public class TaskListServer {
    public static void main(String[] args) throws IOException {
        ArrayList<String> tasks = new ArrayList();
        File doc = new File("/home/carlos/Documents/output.txt");
        FileWriter txt = new FileWriter(doc, true);
        ServerSocket skServer = new ServerSocket(5000);

        while (true) {
            Socket client;
            try {
                client = skServer.accept();
                DataOutputStream out = new DataOutputStream(client.getOutputStream());
                DataInputStream in = new DataInputStream(client.getInputStream());
                Thread newThread = new ClientHandler(client, in, out, tasks, doc, txt);
                newThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class ClientHandler extends Thread {
    DataInputStream dataIn;
    DataOutputStream dataOut;
    Socket client;
    ArrayList<String> tasks;
    File doc;
    FileWriter txt;

    public ClientHandler(Socket client, DataInputStream dataIn, DataOutputStream dataOut, ArrayList<String> tasks, File doc, FileWriter txt) {
        this.client = client;
        this.dataIn = dataIn;
        this.dataOut = dataOut;
        this.tasks = tasks;
        this.doc = doc;
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
