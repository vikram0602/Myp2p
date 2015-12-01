import java.net.*;
import java.io.*;

public class ClientConnection implements Runnable {

    private Socket clientSocket;
    private BufferedReader in = null;
    private PrintStream out;
    public  int count;
    public static String recievefilename;

    public ClientConnection(Socket client,int a,String k)
    {
        this.clientSocket = client;
        count=a;
        recievefilename=k;

    }

    //@Override
    public void run() {
        boolean done = false;
        try{
            in = new BufferedReader(new InputStreamReader(
                    clientSocket.getInputStream()));
            out = new PrintStream(clientSocket.getOutputStream());

        }
        catch(Exception e){
            System.out.println("errooooooorrrr");
        }

        String clientSelection;

        while (!done) {

            try {


                clientSelection = "";

                while ((clientSelection = in.readLine()) != null) {
                    if (clientSelection.equals("get")) {
                        String  clientName;
                        while ((clientName=in.readLine()) != null) {
                           out.println(count);
                            out.println(recievefilename);
                            sendClient(clientName);
                        }

                    } else {
                        System.out.println("Wrong Command...");

                    }
                }

            } catch (IOException ex) {
                System.err.println("Erro--" + ex);
            }
        }
    }


    public void sendClient(String clientName)
    {
        int i;
        String temp;
        if(clientName.equalsIgnoreCase("Client 1"))
        {
            for(i=1;i<=count;i+=5)
            {
                temp="chunk."+i;
                sendFile(temp,clientName);
            }
        }
        else if(clientName.equalsIgnoreCase("Client 2"))
        {
            for(i=2;i<=count;i+=5)
            {
                temp="chunk."+i;
                sendFile(temp,clientName);
            }
        }
        else if(clientName.equalsIgnoreCase("Client 3"))
        {
            for(i=3;i<=count;i+=5)
            {
                temp="chunk."+i;
                sendFile(temp,clientName);
            }
        }
        else if(clientName.equalsIgnoreCase("Client 4"))
        {
            for(i=4;i<=count;i+=5)
            {
                temp="chunk."+i;
                sendFile(temp,clientName);
            }
        }
        else if(clientName.equalsIgnoreCase("Client 5"))
        {
            for(i=5;i<=count;i+=5)
            {
                temp="chunk."+i;
                sendFile(temp,clientName);
            }
        }
    }
    public void sendFile(String fileName, String clientName) {
        try {

            File myFile = new File(fileName);
            byte[] mybytearray = new byte[(int) myFile.length()];

            FileInputStream fis = new FileInputStream(myFile);
            BufferedInputStream bis = new BufferedInputStream(fis);

            DataInputStream dis = new DataInputStream(bis);
            dis.readFully(mybytearray, 0, mybytearray.length);

            OutputStream os = clientSocket.getOutputStream();

            DataOutputStream dos = new DataOutputStream(os);
            dos.writeUTF(myFile.getName());
            dos.writeLong(mybytearray.length);
            dos.write(mybytearray, 0, mybytearray.length);
            dos.flush();

            System.out.println("File " + fileName + " send to "+clientName);

        } catch (Exception e) {
            System.err.println("Error! " + e);
        }
    }
}
