package client4;



import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
//import java.util.*;




// A client for our Multithreaded SocketServer.
public class Client4
{
    private static Socket sock;
    private static String fileName;
    private static BufferedReader bufferReader;
    private static PrintStream os;
    public static int chunkcount;

    public static void main(String[] args) throws IOException {
        readNoChunk();
        int i;
        try {
            sock = new Socket("localhost", 4444);
            bufferReader = new BufferedReader(new InputStreamReader(System.in));
        } catch (Exception e) {
            System.err.println("Error - Try again.");
            System.exit(1);
        }

        os = new PrintStream(sock.getOutputStream());

        boolean done = false;

        while (!done) {
            try {

                String s = selectAction();
                if (s.equals("1")) {
                    os.println("get");
                    //os.println(fileName);
                    os.println("Client 4");
                    for(i=4;i<=chunkcount;i+=5)
                        receiveFile(fileName);

                } else if (s.equals("2")) {
                    done = true;
                    os.println("exit");
                    System.out.println("Connection closed");

                }
            } catch (Exception e) {
                System.err.println("Wrong command");
            }
        }

        sock.close();
    }

    public static void readNoChunk()
    {
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

    public static void receiveFile(String fileName) {
        try {
            int bytesRead;
            InputStream in = sock.getInputStream();

            DataInputStream clientData = new DataInputStream(in);

            fileName = clientData.readUTF();
            OutputStream output = new FileOutputStream(
                    ("src/client4/received_from_server_" + fileName));
            long size = clientData.readLong();
            byte[] buffer = new byte[1024];
            while (size > 0
                    && (bytesRead = clientData.read(buffer, 0,
                    (int) Math.min(buffer.length, size))) != -1) {
                output.write(buffer, 0, bytesRead);
                size -= bytesRead;
            }
            output.flush();

            System.out.println("File " + fileName + " received from Server.");
        } catch (IOException ex) {
            //  Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE,
            //   null, ex);
            //sysout
            System.out.println("ERRORRR!");

        }
    }

}
