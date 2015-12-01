package merge;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by dell 2 on 24-Nov-15.
 */
public class MergerFiles {

    public static void file_merge(int chunkcount,String filename) throws IOException {



        ByteBuffer[] buffers = new ByteBuffer[chunkcount];
        String k;
        for (int i = 0; i < chunkcount; i++) {
            int j=i+1;
            k= "chunk."+j;
            RandomAccessFile raf = new RandomAccessFile(k, "r");
            FileChannel channel = raf.getChannel();
            buffers[i] = channel.map(FileChannel.MapMode.READ_ONLY, 0, raf.length());
        }

        FileOutputStream outFile = new FileOutputStream(filename);
        FileChannel out = outFile.getChannel();
        out.write(buffers);
        out.close();
    }
}