package filessplit;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;


public class Filedivide {

	public int  filedivide(String filename) throws IOException {

		int line;
		File f = new File(filename);
	      BufferedInputStream bis = new BufferedInputStream( new FileInputStream(f));
	      
	      int i,j,chunkCount=0;
	      int chunksize=102400;
	      try {

	         int filelength=(int) f.length();
			  byte[] r = new byte[chunksize];
	         for(i=0;i<=filelength/chunksize;i++)
	         {
				 j=i+1;
	        	 String temp= "chunk."+j;
	        	 File fout =new File(temp);
	        	 BufferedOutputStream buffOut=new BufferedOutputStream(new FileOutputStream(fout));
				 chunkCount++;
	        		line= bis.read(r);
	        		buffOut.write(r, 0, line);
	        	 buffOut.close();
	         }
	         
	      }finally {
	            bis.close();
	      }
		return chunkCount;
    }
	public void fileChunk(int a)
	{
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("chunkcount.txt", "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		writer.println(a);
		writer.close();
	}

}
