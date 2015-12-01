package client4;



import merge.MergerFiles;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

// A client 4 for our Multithreaded SocketServer.
public class Client4
{
    private static Socket socki;
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
    private static Socket clidownload;
    public static String recievefilename;
    public static int downloadedchunkcount=0;


    public static void main(String[] args) throws IOException {
        BufferedReader br;
        int i;
        try {
            socki = new Socket("localhost", 4444);
            bufferReader = new BufferedReader(new InputStreamReader(System.in));
        } catch (Exception e) {
            System.err.println("Error - Try again.");
            System.exit(1);
        }

        os = new PrintStream(socki.getOutputStream());
        br = new BufferedReader(new InputStreamReader(socki.getInputStream()));

        boolean done = false;

        while (!done) {
            try {

                String s = selectAction();
                if (s.equals("1")) {
                    os.println("get");

                    os.println("Client 4");
                    chunkcount= Integer.parseInt(br.readLine());
                    recievefilename=br.readLine();
                    System.out.println("Received File Name:"+recievefilename);
                    readNoChunk();
                    for(i=4;i<=chunkcount;i+=5)
                    {
                        receiveFile(fileName, socki);
                        chunkcheck[i-1]=1;
                        mylist.set(i-1,"1");
                        downloadedchunkcount++;
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
            if(downloadedchunkcount==chunkcount)
                merge_all();


        }
        socki.close();
    }

    public static void merge_all() throws IOException {
        String obc="src/client4/"+recievefilename;
        MergerFiles mer=new MergerFiles();
        mer.file_merge(chunkcount,obc);

    }
    public static void actdownloader() throws IOException {
        int i, j;
        long start = System.currentTimeMillis();
        long end = start + 5*1000; // 60 seconds * 1000 ms/sec
        while (System.currentTimeMillis() < end) {
            try {
                clidownload = new Socket("localhost", 4003);
                bufferReader = new BufferedReader(new InputStreamReader(System.in));
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
                        mylist.set(i, "1");
                        receiveFile(temp, clidownload);
                        downloadedchunkcount++;
                    }
                }
                os.println("exit");
            } catch (Exception e) {
                System.out.println("Requesting Neighbor Client 3 to Connect in 1sec!");
                // System.exit(1);
            }
        }
    }






    public static void actuploader() throws IOException {
        try {
            client1Socket = new ServerSocket(4004);
            System.out.println("Client 4 uploader started.");
        } catch (Exception e) {
            System.err.println("client 4 Port already in use.");
            System.exit(1);
        }
        try {
            clientSocket = client1Socket.accept();
            System.out.println("Conection Accept : " + clientSocket);

            Thread t = new Thread(new Client4Connection(clientSocket, chunkcount, chunkcheck,mylist));
            //  System.out.println("hola");
            t.start();
            client1Socket.close();
        } catch (Exception e) {
            System.err.println("Conection Error.");
        }

    }





    public static void readNoChunk()
    {
        System.out.println("Reading No. of chunks");
        int i;
        mylist=new ArrayList<String>();

        try{

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
                    ("src/client4/" + fileName));
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
            System.out.println("ERRORRR!");

        }
    }

}
