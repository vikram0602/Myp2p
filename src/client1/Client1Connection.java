package client1;

/**
 * Created by dell 2 on 18-Nov-15.
 */
import java.net.*;
import java.io.*;


public class Client1Connection implements Runnable {

    private Socket clientSocket;
    private BufferedReader in = null;
    public  int count;

    public Client1Connection(Socket client,int a)
    {
        this.clientSocket = client;
        count=a;

    }

    //@Override
    public void run() {
        boolean done = false;
        try{
            in = new BufferedReader(new InputStreamReader(
                    clientSocket.getInputStream()));

        }
        catch(Exception e){}

        String clientSelection;

        while (!done) {

            try {


                clientSelection = "";

                while ((clientSelection = in.readLine()) != null) {
                    if (clientSelection.equals("send")) {
                        receiveFile();

                    } else if (clientSelection.equals("get")) {
                        String outGoingFileName, clientName;
                        while ((clientName=in.readLine()) != null) {
                           // sendClient(clientName);
                            //  sendFile(outGoingFileName,clientName);
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

    public void receiveFile() {
  /*      try {
            int bytesRead;

            DataInputStream clientData = new DataInputStream(
                    clientSocket.getInputStream());

            String fileName = clientData.readUTF();
            OutputStream output = new FileOutputStream(
                    ("received_from_client_" + fileName));
            long size = clientData.readLong();
            byte[] buffer = new byte[1024];
            while (size > 0
                    && (bytesRead = clientData.read(buffer, 0,
                            (int) Math.min(buffer.length, size))) != -1) {
                output.write(buffer, 0, bytesRead);
                size -= bytesRead;
            }
            output.flush();
            output.close();

            System.out.println("File " + fileName + " received from client.");

        } catch (IOException ex) {
            System.err.println("Error." + ex);
        }*/
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
