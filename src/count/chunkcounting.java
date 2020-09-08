package count;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by ronak on 11/18/2015.
 */
public class chunkcounting {

         public static int numchunk=1;
        public static byte[]readbytes;
    public static int count;



        //Method to split file
        public static String splitfile(String filetosplit) throws InterruptedException {
            File f = new File(filetosplit);
            try {
                FileInputStream in = new FileInputStream(f);
                FileChannel fchannel = in.getChannel();
                //Allocating Buffer size = 100KB
                ByteBuffer buf = ByteBuffer.allocate(102400);
                readbytes = new byte[102400];
                System.out.println("Size of the file"+" "+f.length()+" "+ "Bytes" );
                System.out.println("No of chunks to make of 100kb size "+" "+Math.incrementExact(f.length()/102400));
                int tmp = 0;
                while((tmp = fchannel.read(buf))>=0){
                    buf.flip();//Setting current index of the file to start at zero
                    buf.get(readbytes,0,tmp);//buf.array();
                    //calling method storetofile to make new chunksfile
                    storetofile(readbytes , "chunk"+numchunk, tmp);
                    System.out.println("Chunk "+numchunk +" "+"created");
                    numchunk++;
                    buf.clear();
                    // Thread.sleep(1000);


                }
                count=numchunk-1;


                in.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return filetosplit;
        }


        public static void  storetofile(byte[] bytestowrite ,String path, int tmp){

            try {
                FileOutputStream out = new FileOutputStream(path);
                out.write(bytestowrite, 0, tmp);
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }



    }
