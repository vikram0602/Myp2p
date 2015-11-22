package client5;



import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.ArrayList;





// A client for our Multithreaded SocketServer.
public class Client5
{
    private static Socket socki;
    private static Socket clidownload;
    private static String fileName;
    private static BufferedReader bufferReader;
    private static PrintStream os;
    public static int chunkcount;
    public static int chunkcheck[];
    public static ServerSocket client1Socket;
    public static Socket clientSocket = null;
    public static ArrayList<String> mylist ;
    public static ArrayList<String> xyz;
    private static ObjectInputStream in;

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
                    os.println("Client 5");
                    for(i=5;i<=chunkcount;i+=5)
                    {
                        receiveFile(fileName,socki);
                        chunkcheck[i-1]=1;
                        mylist.set(i-1,"1");
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

            actuploader();
            actdownloader();
        }
        socki.close();
    }


    public static void actdownloader() throws IOException {
        int i, j;
        //boolean flag=false;
        while (true) {
            try {
                clidownload = new Socket("localhost", 4004);
                //clidownload.setSoTimeout(5*1000);
                bufferReader = new BufferedReader(new InputStreamReader(System.in));
                // Thread.sleep(2000);
                os = new PrintStream(clidownload.getOutputStream());
                String temp;
                in = new ObjectInputStream(clidownload.getInputStream());

                xyz = (ArrayList<String>) in.readObject();

                for (i = 0; i < xyz.size(); i++) {
                    if (xyz.get(i).equalsIgnoreCase("1") && chunkcheck[i] == 0) {
                        j = i + 1;
                        temp = "chunk." + Integer.toString(j);
                        os.println(temp);
                        System.out.println(temp);
                        chunkcheck[i] = 1;
                        xyz.set(i, "1");
                        receiveFile(temp, clidownload);
                    }
                }
               os.println("exit");
            } catch (Exception e) {
                System.out.println("Requesting Neighbor Client 4 to Connect in 2sec!");
                // System.exit(1);
            } finally {
                // clidownload.close();
            }
            //os = new PrintStream(clidownload.getOutputStream());
        }
    }



    public static void actuploader() throws IOException {
        try {
            client1Socket = new ServerSocket(4005);
            System.out.println("Client 5 uploader started.");
        } catch (Exception e) {
            System.err.println("client 5 Port already in use.");
            System.exit(1);
        }
        //long start = System.currentTimeMillis();

        try {
            clientSocket = client1Socket.accept();
            System.out.println("Conection Accept : " + clientSocket);

            Thread t = new Thread(new Client5Connection(clientSocket, chunkcount, chunkcheck,mylist));
            //  System.out.println("hola");
            t.start();

        } catch (Exception e) {
            System.err.println("Conection Error.");
        }

    }



    public static void readNoChunk()
    {
        int i;
        System.out.println("Reading No. of chunks");
        //Name of the file
        mylist=new ArrayList<String>();
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
            for(i=0;i<chunkcount;i++)
                mylist.add(i,"0");

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



    public static void receiveFile(String fileName,Socket sock) {
        try {
            int bytesRead;
            InputStream in = sock.getInputStream();

            DataInputStream clientData = new DataInputStream(in);

            fileName = clientData.readUTF();
            OutputStream output = new FileOutputStream(
                    ("src/client5/" + fileName));
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
