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

	                Thread t = new Thread(new ClientConnection(clientSocket));

	                t.start();

	            } catch (Exception e) {
	                System.err.println("Conection Error.");
	            }
	        }
	}
	
	public static void main(String [] args){
	
			Filedivide ob= new Filedivide();
		try {
			ob.filedivide("file.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Server obj= new Server();
		obj.connectc();
	}
}
