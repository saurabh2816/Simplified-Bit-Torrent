package client2;
import client1.*;

import count.counter;
import splitfiles.Splitfile;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Created by ronak on 11/16/2015.
 */
public class Client2 {


    private static Socket sock;
    private static Socket csock=null;
    private static ObjectInputStream stdin;
      private static String fileName;
    private static String chunklist2[];
    private static BufferedReader br;
    private static PrintStream os;
    private static ServerSocket csock2;
    public static int k =0;
    public static int timeout;
    public static int mcount;
    public static int mtime=0;



    public static void main(String[] args) throws IOException {

        counter cc = new counter();
        Mergefiles m = new Mergefiles();
        try {
            sock = new Socket("localhost", 4450);
            //
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
                    os.println("client 2");
                    cc.noofchunks();

                    chunklist2 = new String[cc.countchunks];
                    for (i = 2; i <= cc.countchunks; i += 5) {

                        fileName=receiveFile(fileName,sock,"server");
                        chunklist2[k++]=fileName;


                    }
                    break;
                default:
                    System.out.println("No input ");
            }
        } catch (Exception e) {
            System.err.println("not valid input");
        }

        sock.close();
            timeout=0;
            while (true) {
                int o = downloadpeer();
                System.out.println(mcount);
                uploadtopeer();
                if(mcount==chunklist2.length){
                    m.merge();
                }
            }

    }

    public static int downloadpeer() {
        while(true) {
            try {
                Socket sock2 = new Socket("localhost", 5000);
                br = new BufferedReader(new InputStreamReader(System.in));
                os = new PrintStream(sock2.getOutputStream());
                stdin = new ObjectInputStream(sock2.getInputStream());
                String[] chunklist = (String[]) stdin.readObject();
                // int k = (int) stdin.readObject();
                // System.out.println(k)

                System.out.println("Downloading from neighbour client 1\n ");


                for (int i = 0; i < chunklist.length; i++) {
                    if (chunklist[i] == null)
                        break;
                    int flag = 0;
                    for (int j = 0; j < k; j++) {

                        if (chunklist[i].equalsIgnoreCase(chunklist2[j])) {
                            // System.out.println("already has this chunk");
                            flag = 1;
                            break;
                        }

                    }
                    if (flag == 0) {
                        os.println("yes");
                        receiveFile(chunklist[i], sock2, "Client1");
                        chunklist2[k++] = chunklist[i];
                        mcount=k;
                    } else
                        os.println("no");

                }


            } catch (Exception e) {
                timeout++;
                if(timeout>5)
                {
                    timeout=0;
                    return 1;
                }
                System.out.println("Ping sent to client1 to connect");

            }

        }

    }

    public static void uploadtopeer() throws IOException {
        try {
             csock2 = new ServerSocket(5001);
            System.out.println("Client2 uploader thread started");
            csock=csock2.accept();
            System.out.println("connection made " + csock);
            Thread t = new Thread(new Peer2connection(csock  ,chunklist2));
            t.start();

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
            OutputStream output = new FileOutputStream("src/client2/" + fileName);
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
