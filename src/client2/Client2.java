package client2;



import client1.Client1Connection;

import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.ArrayList;




// A client for our Multithreaded SocketServer.
public class Client2
{
    private static Socket socki;
    private static String fileName;
    private static Socket clidownload;
    private static BufferedReader bufferReader;
    private static PrintStream os;
    public static int chunkcount;
    public static int chunkcheck[];
    public static ServerSocket client1Socket;
    public static Socket clientSocket = null;
    private static ObjectInputStream in;
    public static ArrayList<String> xyz;



    public static void main(String[] args) throws IOException {
        readNoChunk();
        int i;
        try {
            socki = new Socket("localhost", 4444);
            bufferReader = new BufferedReader(new InputStreamReader(System.in));
        } catch (Exception e) {
            System.err.println("Error - Try again.");
            System.exit(1);
        }

        os = new PrintStream(socki.getOutputStream());

        boolean done = false;

        while (!done) {
            try {

                String s = selectAction();
                if (s.equals("1")) {
                    os.println("get");
                    //os.println(fileName);
                    os.println("Client 2");
                    for(i=2;i<=chunkcount;i+=5)
                    {
                        receiveFile(fileName,socki);
                        chunkcheck[i-1]=1;
                    }
                    break;

                } else if (s.equals("2")) {
                    done = true;
                    os.println("exit");
                    System.out.println("Connection closed");

                }
            } catch (Exception e) {
                System.err.println("Wrong command");
            }
        }
        boolean test=false;
        while (!test)
        {
            actdownloader();
            actuploader();
        }
        socki.close();
    }

    public static void actdownloader() throws IOException {
        int i,j;
        //boolean flag=false;
        while(true) {
            try {
                clidownload = new Socket("localhost", 4001);
                bufferReader = new BufferedReader(new InputStreamReader(System.in));
               // Thread.sleep(2000);
                os = new PrintStream(clidownload.getOutputStream());
                String temp;
                in=  new ObjectInputStream(clidownload.getInputStream());

                xyz= (ArrayList<String>)in.readObject();

                for(i=0;i<xyz.size();i++)
                {
                    if(xyz.get(i).equalsIgnoreCase("1") && chunkcheck[i]==0) {
                        j=i+1;
                        temp="chunk."+Integer.toString(j);
                        os.println(temp);
                        System.out.println(temp);
                        receiveFile(temp,clidownload);
                    }
                }
                os.println("exit");
            } catch (Exception e) {
                System.out.println("Requesting Neighbor Client 3 to Connect in 2sec!");
                // System.exit(1);
            }
            finally {
                //  clidownload.close();
            }
            //os = new PrintStream(clidownload.getOutputStream());
        }

    }

    public static void actuploader() throws IOException {
        try {
            client1Socket = new ServerSocket(4002);
            System.out.println("Client 2 uploader started.");
        } catch (Exception e) {
            System.err.println("client 2 Port already in use.");
            System.exit(1);
        }
        long start = System.currentTimeMillis();
        long end = start +1000; // 60 seconds * 1000 ms/sec
        System.out.println(end);
        while(System.currentTimeMillis() < end) {
            try {
                clientSocket = client1Socket.accept();
                System.out.println("Conection Accept : " + clientSocket);

                Thread t = new Thread(new Client2Connection(clientSocket, chunkcount, chunkcheck));
                //  System.out.println("hola");
                t.start();

            } catch (Exception e) {
                System.err.println("Conection Error.");
            }
        }
    }

    public static void readNoChunk()
    {
        int i;
        System.out.println("Reading No. of chunks");
        //Name of the file
        String fileName="chunkcount.txt";
        try{

            FileReader inputFile = new FileReader(fileName);
            BufferedReader bufferReader = new BufferedReader(inputFile);
            String line;

            line = bufferReader.readLine();
            chunkcount=Integer.parseInt(line);
            System.out.println(chunkcount);
            chunkcheck=new int[chunkcount];
            for(i=0;i<chunkcheck.length;i++)
                chunkcheck[i]=0;

            bufferReader.close();
        }catch(Exception e){
            System.out.println("Error while reading file line by line:" + e.getMessage());
        }

    }


    public static String selectAction() throws IOException {
        System.out.println("");
        // System.out.println("send - Send File.");
        System.out.println("1 - Get File.");
        System.out.println("2 - Exit.");
        System.out.print("\nSelect one Option: ");

        return bufferReader.readLine();
    }

    /*public static void sendFile() {
        try {
            System.err.print("File Name: ");
            fileName = bufferReader.readLine();

            File myFile = new File(fileName);
            byte[] mybytearray = new byte[(int) myFile.length()];

            FileInputStream fis = new FileInputStream(myFile);
            BufferedInputStream bis = new BufferedInputStream(fis);

            DataInputStream dis = new DataInputStream(bis);
            dis.readFully(mybytearray, 0, mybytearray.length);

            OutputStream os = sock.getOutputStream();

            DataOutputStream dos = new DataOutputStream(os);
            dos.writeUTF(myFile.getName());
            dos.writeLong(mybytearray.length);
            dos.write(mybytearray, 0, mybytearray.length);
            dos.flush();


            System.out.println("File " + fileName
                    + " send to server.");
        } catch (Exception e) {
            System.err.println("ERROR! " + e);
        }
    }*/

    public static void receiveFile(String fileName,Socket sock) {
        try {
            int bytesRead;
            InputStream in = sock.getInputStream();

            DataInputStream clientData = new DataInputStream(in);

            fileName = clientData.readUTF();
            OutputStream output = new FileOutputStream(
                    ("src/client2/" + fileName));
            long size = clientData.readLong();
            byte[] buffer = new byte[1024];
            while (size > 0
                    && (bytesRead = clientData.read(buffer, 0,
                    (int) Math.min(buffer.length, size))) != -1) {
                output.write(buffer, 0, bytesRead);
                size -= bytesRead;
            }
            output.flush();

            System.out.println("File " + fileName + " received.");
        } catch (IOException ex) {
            //  Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE,
            //   null, ex);
            //sysout
            System.out.println("ERRORRR!");

        }
    }

}
