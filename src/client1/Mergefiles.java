package client1;

import java.io.*;
import count.counter;


/**
 * Created by ronak on 11/28/2015.
 */
public class Mergefiles {

     counter cc = new counter();

    public void merge() throws IOException {

             FileOutputStream f = new FileOutputStream("src/client1/Test.zip");
            int i=1;
            int length;
        byte[] buffer = new byte[1024];
            while (i<=cc.countchunks) {

                FileInputStream fs = new FileInputStream("src/client1/chunk"+i);
                while ((length = fs.read(buffer)) > 0){
                    f.write(buffer, 0, length);
                }
                i++;
                fs.close();
            }
            f.close();
        System.out.println("Merging done");

        }
    }
