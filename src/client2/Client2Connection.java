package client2;

import java.net.*;
import java.io.*;
import java.util.ArrayList;


public class Client2Connection implements Runnable {

    private Socket clientSocket;
    private BufferedReader in = null;
    public  int count,chunkcheck[];
    public ArrayList<String> arr;
    private ObjectOutputStream out;

    public Client2Connection(Socket client, int a, int[] b, ArrayList<String> k)
    {
        this.clientSocket = client;
        count=a;
        chunkcheck =new int[a];
        chunkcheck=b;
        arr=k;

    }

    //@Override
    public void run() {
        //int i;
        // boolean done = false;
        try{
            in = new BufferedReader(new InputStreamReader(
                    clientSocket.getInputStream()));
            out=new ObjectOutputStream(clientSocket.getOutputStream());

        }
        catch(Exception e){
            System.out.println("error in my connection");
        }
        int chunk;
        String filename,temp;
        try {
            out.writeObject(arr);
            while(true)
            {
                temp=in.readLine();
                if(temp.equalsIgnoreCase("exit"))
                    break;
                else
                {
                    sendFile(temp,"client 3");
                }
            }
            System.out.println("DONE!");
        }
        catch (IOException ex) {
            System.err.println("Erro--" + ex);
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
