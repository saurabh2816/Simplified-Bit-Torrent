package client5;
import client1.*;
import count.counter;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;




/**
 * Created by ronak on 11/16/2015.
 */
public class Client5  {


        private static Socket sock;
        private static String fileName;
        private static BufferedReader stdin;
        private static PrintStream os;
        private static String chunklist5[];
        public static int k =0;
        public static int timeout;
        public static int mcount;
        public static int mtime=0;

        public static void main(String[] args) throws IOException {
            counter cc = new counter();
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

                    case 1:
                        os.println("2");
                        os.println("Client 5");
                        cc.noofchunks();
                        chunklist5 = new String[cc.countchunks];
                        for (int i = 5; i <= cc.countchunks; i += 5) {
                            fileName = receiveFile(fileName,sock, "server");
                            chunklist5[k++] = fileName;
                        }

                        break;
                }
            } catch (Exception e) {
                System.err.println("not valid input");
            }
            sock.close();
            timeout=0;
            while (true) {

                uploadtopeer();

               int o= downloadpeer();
                System.out.println(mcount);
                if(mcount==chunklist5.length){
                    m.merge();
                }



            }
        }


        public static void uploadtopeer(){

            try {
                ServerSocket csock5 = new ServerSocket(5002);
                System.out.println("client5 uploader started");
                Socket csock = csock5.accept();
                System.out.println("connection made " + csock);
                Thread t = new Thread(new Peer5connection(csock  ,chunklist5));
                t.start();
                csock5.close();

            } catch (IOException e) {
                System.out.println("Error"+e);

            }



        }
        public static int downloadpeer(){
            while(true) {
                try {
                    Socket sock2 = new Socket("localhost", 12000);
                    stdin = new BufferedReader(new InputStreamReader(System.in));
                    os = new PrintStream(sock2.getOutputStream());
                    ObjectInputStream odd = new ObjectInputStream(sock2.getInputStream());
                    String chunklist4[] = (String[]) odd.readObject();

                    System.out.println("Downloading from neighbour client 4\n ");

                    for (int i = 0; i < chunklist4.length; i++) {
                        if (chunklist4[i] == null)
                            break;
                        int flag = 0;
                        for (int j = 0; j < k; j++) {

                            if (chunklist4[i].equalsIgnoreCase(chunklist5[j])) {
                                //  System.out.println("already has this chunk");
                                flag = 1;
                                break;
                            }
                        }
                        if (flag == 0) {
                            os.println("yes");
                            receiveFile(chunklist4[i], sock2, "Client4");
                            chunklist5[k++] = chunklist4[i];
                            mcount=k;
                        } else
                            os.println("no");
                    }
                    //   System.out.println("\nFinished downloading !!!");

                } catch (Exception e) {
                    timeout++;
                    if(timeout>5)
                    {
                        timeout=0;
                        return 1;
                    }
                    System.out.println("Ping client4 to connect");

                }
            }


        }
        public static String selectAction() throws IOException {
            System.out.println("1. Get files from server");


            return stdin.readLine();
        }




        public static String receiveFile(String fileName ,Socket sock , String peer) {
            try {

                int bytesRead;
                InputStream in = sock.getInputStream();

                DataInputStream clientData = new DataInputStream(in);

                fileName = clientData.readUTF();
                OutputStream output = new FileOutputStream("src/client5/" +fileName);
                long size = clientData.readLong();
                byte[] buffer = new byte[1024];
                while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                    output.write(buffer, 0, bytesRead);
                    size -= bytesRead;
                }

                output.flush();


                System.out.println("File "+fileName+" received"+" "+peer);

            } catch (IOException ex) {
                // Logger.getLogger(src/Main.Mainserver.CLIENTConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
            return fileName;
        }
    }





