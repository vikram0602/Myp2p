package filessplit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.*;
import java.nio.channels.*;


public class Filedivide {
	public void storeByteArrayToFile(byte[] bytesToSave, String path) throws IOException {
	    FileOutputStream fOut = new FileOutputStream(path);
	    try {
	        fOut.write(bytesToSave);
	    }
	    catch (Exception ex) {
	       //System.out.print("error");
	       ex.printStackTrace();
	    }
	    finally {
	        fOut.close();
	    }
	}
	public void filedivide(String filename) throws IOException {
		File f = new File(filename);
		FileInputStream is = new FileInputStream(f);
		FileChannel fc = is.getChannel();
		ByteBuffer bb = ByteBuffer.allocate(102400);
		
		int chunkCount = 1;
	    byte[] bytes;
	    
	    while(fc.read(bb) >= 0){
	        bb.flip();
	        //save the part of the file into a chunk
	        bytes = bb.array();
	        storeByteArrayToFile(bytes, "chunk." + chunkCount);//mRecordingFile is the (String)path to file

			System.out.println("CHUNK "+ chunkCount+ " created!");
			chunkCount++;
	        bb.clear();
			//Thread.sleep(2000);

		}
	    is.close();
		
		/*int line;
		File f = new File(filename);
	      BufferedInputStream bis = new BufferedInputStream( new FileInputStream(f));
	      
	      int i,j;
	      int chunksize=102400;
	      try {
	    	 // InputStream in = new FileInputStream(in);
	         int filelength=(int) f.length();
	        
	         
	         for(i=1;i<=filelength/chunksize;i++)
	         {
	        	 String temp= "chunk"+i+".txt";
	        	 File fout =new File(temp);
	        	 BufferedOutputStream buffOut=new BufferedOutputStream(new FileOutputStream(fout));
	        	 //byte []arr = new byte [1024 * 1024];
	        	 for(j=1;j<=chunksize;j++)
	        	 {
	        		 line= bis.read();
	        		 buffOut.write(line);
	        		// buffOut.write(arr, 0, line);
	        	 }
	        	 buffOut.close();
	         }
	         
	      }finally {
	       
	            bis.close();
	          
	 
	      }
	      
	      
	      //merge
	      File fout =new File("output.txt");
     	 BufferedOutputStream buffOut1=new BufferedOutputStream(new FileOutputStream(fout));
	      for(j=1;j<=i-1;j++)
     	 {
	    	  String temp= "chunk"+j+".txt";
	    	  File fre = new File(temp);
		      BufferedInputStream bis1 = new BufferedInputStream( new FileInputStream(fre));
		      System.out.println(temp+" ");
		      for (int k=1; k <= chunksize; k++) {
		    	   line= bis1.read();
     		 buffOut1.write(line);
		      }
		      bis1.close();
     	 }   
	   buffOut1.close();*/
    }
		

}
