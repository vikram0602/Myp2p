import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.text.SimpleDateFormat;
import java.util.*;
import filessplit.*;


public class Server
{
	 	public static ServerSocket serverSocket;
	    public static Socket clientSocket = null;
		public static int count;
	public void connectc()
	{
		 try {
	            serverSocket = new ServerSocket(4444);
	            System.out.println("Server started.");
	        } catch (Exception e) {
	            System.err.println("Port in use.");
	            System.exit(1);
	        }

	        while (true) {
	            try {
	                clientSocket = serverSocket.accept();
	                System.out.println("Conection Accept : " + clientSocket);

	                Thread t = new Thread(new ClientConnection(clientSocket,count));

	                t.start();

	            } catch (Exception e) {
	                System.err.println("Conection Error.");
	            }
	        }
	}
	
	public static void main(String [] args){
	

		Filedivide ob= new Filedivide();
		try {
		count=	ob.filedivide("file.pdf");
		} catch (IOException e) {
			e.printStackTrace();
		}
		ob.fileChunk(count);
		System.out.println("No. of Chunks= "+count);
		Server obj= new Server();
		obj.connectc();
	}
}
