package client3;
import client1.*;

import client1.Mergefiles;
import count.counter;
import splitfiles.Splitfile;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by ronak on 11/16/2015.
 */
public class Client3 {


    private static Socket sock;
    private static Socket csock=null;
    private static ObjectInputStream stdin;
    private static String fileName;
    private static String chunklist3[];
    private static BufferedReader br;
    private static PrintStream os;
    private static ServerSocket csock2;
    public static int k =0;
    public static int timeout;
    public static int mcount;
    public static int mtime=0;



    public static void main(String[] args) throws IOException {

        counter cc = new counter();
        client3.Mergefiles m = new client3.Mergefiles();
        try {
            sock = new Socket("localhost", 4450);
            br = new BufferedReader(new InputStreamReader(System.in));
        } catch (Exception e) {
            System.err.println("Cannot connect to the server, try again later.");
            System.exit(1);
        }

        os = new PrintStream(sock.getOutputStream());
        int i;
        try {
            switch (Integer.parseInt(selectAction())) {

                case 1:
                    os.println("2");
                    os.println("client 3");
                    cc.noofchunks();

                    chunklist3 = new String[cc.countchunks];
                    for (i = 3; i <= cc.countchunks; i += 5) {

                        fileName=receiveFile(fileName,sock,"server");
                        chunklist3[k++]=fileName;
                        // System.out.println(chunklist2[k-1]);


                    }

                    break;
                default:
                    System.out.println("No input ");
            }
        } catch (Exception e) {
            System.out.println(e);
           System.err.println("not valid input");
        }

        sock.close();
        timeout=0;
        while (true) {
            uploadtopeer();
            int o = downloadpeer();
            System.out.println(mcount);
            if(mcount==chunklist3.length){
                m.merge();
              }
            }
        }



    public static int downloadpeer() {
        while(true) {
            try {
                Socket sock2 = new Socket("localhost", 5001);
                br = new BufferedReader(new InputStreamReader(System.in));
                os = new PrintStream(sock2.getOutputStream());
                stdin = new ObjectInputStream(sock2.getInputStream());
                String[] chunklist2 = (String[]) stdin.readObject();
                // int k = (int) stdin.readObject();

                System.out.println("Downloading from neighbour client 2\n ");


                for (int i = 0; i < chunklist2.length; i++) {
                    if (chunklist2[i] == null)
                        break;
                    int flag = 0;
                    for (int j = 0; j < k; j++) {

                        if (chunklist2[i].equalsIgnoreCase(chunklist3[j])) {
                            // System.out.println("already has this chunk");
                            flag = 1;
                            break;
                        }

                    }
                    if (flag == 0) {
                        os.println("yes");
                        receiveFile(chunklist2[i], sock2, "Client2");
                        chunklist3[k++] = chunklist2[i];
                        mcount=k;
                    } else
                        os.println("no");

                }


            } catch (Exception e) {
                timeout++;
                if (timeout > 5) {
                    timeout = 0;
                    return 1;
                }
                System.out.println("Ping sent to client2 to connect");

            }
        }


    }

    public static void uploadtopeer() throws IOException {
        try {
            csock2 = new ServerSocket(11000);
            System.out.println("Client3 uploader thread started");
            csock=csock2.accept();
            System.out.println("connection made " + csock);
            Thread t = new Thread(new Peer3connection(csock  ,chunklist3));
            t.start();
            csock2.close();

        } catch (IOException e) {
            System.out.println("Error"+e);
        }

        csock2.close();
    }


    public static String selectAction() throws IOException {
        System.out.println("1.Get files from server");

        return br.readLine();
    }
    public static String receiveFile(String fileName , Socket sock , String peer) {
        try {
            int bytesRead;
            InputStream in = sock.getInputStream();

            DataInputStream clientData = new DataInputStream(in);
            fileName = clientData.readUTF();
            OutputStream output = new FileOutputStream("src/client3/" + fileName);
            long size = clientData.readLong();
            byte[] buffer = new byte[1024];
            while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                output.write(buffer, 0, bytesRead);
                size -= bytesRead;
            }

            output.flush();


            System.out.println("File " + fileName + " received from"+" "+ peer);
        } catch (IOException ex) {
            // Logger.getLogger(src/Main.Mainserver.CLIENTConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fileName;
    }

}
