package client4;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import client1.*;
import com.sun.scenario.effect.Merge;
import count.counter;

/**
 * Created by ronak on 11/16/2015.
 */
public class Client4 {



        private static Socket sock;
        private static String fileName;
        private static BufferedReader stdin;
        private static PrintStream os;
        private static ObjectInputStream odd;
        private static String[] chunklist4;
        private static int k =0;
        private static Socket csock = null;
         public static int timeout;
         public static int mcount;
         public static int mtime=0;

        public static void main(String[] args) throws IOException {
            counter cc=new counter();
            Mergefiles m = new Mergefiles();
            try {
                sock = new Socket("localhost", 4450);
                stdin = new BufferedReader(new InputStreamReader(System.in));
            } catch (Exception e) {
                System.err.println("Cannot connect to the server, try again later.");
                System.exit(1);
            }

            os = new PrintStream(sock.getOutputStream());

            try {
                switch (Integer.parseInt(selectAction())) {
                    // case 1:
                    // os.println("1");
                    //sendFile();
                    // break;
                    case 1:
                        os.println("2");
                        os.println("Client 4");
                        cc.noofchunks();
                        chunklist4 = new String[cc.countchunks];
                        for(int i=4;i<= cc.countchunks;i+=5){
                           fileName= receiveFile(fileName ,sock ,"Server");
                           chunklist4[k++]=fileName;
                        }

                        break;
                }
            } catch (Exception e) {
                System.out.println(e);
                System.err.println("not valid input");
            }


            sock.close();
            timeout=0;
            while (true) {
                int o = downloadpeer();
                System.out.println(mcount);
                uploadtopeer();
                if(mcount==chunklist4.length){
                    m.merge();
                }
            }
        }
        public static int downloadpeer(){
            while(true) {
                try {
                    Socket sock2 = new Socket("localhost", 11000);
                    stdin = new BufferedReader(new InputStreamReader(System.in));
                    os = new PrintStream(sock2.getOutputStream());
                    odd = new ObjectInputStream(sock2.getInputStream());
                    String chunklist3[] = (String[]) odd.readObject();

                    System.out.println("Downloading from neighbour client 3\n ");

                    for (int i = 0; i < chunklist3.length; i++) {
                        if (chunklist3[i] == null)
                            break;
                        int flag = 0;
                        for (int j = 0; j < k; j++) {

                            if (chunklist3[i].equalsIgnoreCase(chunklist4[j])) {
                                //  System.out.println("already has this chunk");
                                flag = 1;
                                break;
                            }
                        }
                        if (flag == 0) {
                            os.println("yes");
                            receiveFile(chunklist3[i], sock2, "Client3");
                            chunklist4[k++] = chunklist3[i];
                            mcount=k;
                        } else
                            os.println("no");


                    }
                    // System.out.println("\nFinished downloading !!!");

                } catch (Exception e) {
                    timeout++;
                    if (timeout > 5) {
                        timeout = 0;
                        return 1;
                    }

                    System.out.println("Ping client3 to connect");

                }
            }


        }
        public static void uploadtopeer(){


            try {
                ServerSocket csock4 = new ServerSocket(12000);
                System.out.println("Client4 uploader thread started");
                csock = csock4.accept();
                System.out.println("connection made " + csock);
                Thread t = new Thread(new Peer4connection(csock, chunklist4));
                t.start();
                csock4.close();

            } catch (IOException e) {
                System.out.println("Error"+e);

            }

            //csock.close;
        }



        public static String selectAction() throws IOException {
            System.out.println("1. Get file from server");

            return stdin.readLine();
        }

             public static String receiveFile(String fileName , Socket sock ,String peer) {
            try {
                int bytesRead;
                InputStream in = sock.getInputStream();

                DataInputStream clientData = new DataInputStream(in);

                fileName = clientData.readUTF();
                OutputStream output = new FileOutputStream("src/client4/" +fileName);
                long size = clientData.readLong();
                byte[] buffer = new byte[1024];
                while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                    output.write(buffer, 0, bytesRead);
                    size -= bytesRead;
                }

                output.flush();


                System.out.println("File "+fileName+" received from"+" "+peer);
            } catch (IOException ex) {
                // Logger.getLogger(src/Main.Mainserver.CLIENTConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        return fileName;
        }
    }





