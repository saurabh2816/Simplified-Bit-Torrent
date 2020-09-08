package Main;

import splitfiles.Splitfile;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Mainserver {
        private static ServerSocket serverSocket;
        private static Socket clientSocket = null;
        public static int total ;
        private static PrintStream os;

    // public static int totalchunks;

        public static void main(String[] args) throws IOException {

            try {
                serverSocket = new ServerSocket(4450);
                System.out.println("Server started.");
            } catch (Exception e) {
                System.err.println("Port already in use.");
                System.exit(1);
            }

            try {
                Splitfile.splitfile("src/splitfiles/Test.zip");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while (true) {
                try {
                    clientSocket = serverSocket.accept();
                    System.out.println("Accepted connection : " + clientSocket);

                    Thread t = new Thread(new CLIENTConnection(clientSocket));

                    t.start();
                }
                catch (Exception e) {
                    System.err.println("Error in connection attempt.");
                }


            }
        }




        public static class CLIENTConnection implements Runnable {

            private Socket clientSocket;
            private BufferedReader in = null;

            public CLIENTConnection(Socket client) {
                this.clientSocket = client;
            }

            @Override
            public void run() {
                try {
                    in = new BufferedReader(new InputStreamReader(
                            clientSocket.getInputStream()));
                    String clientSelection;
                    while ((clientSelection = in.readLine()) != null) {
                        switch (clientSelection) {
                            case "1":
                                // receiveFile();
                                break;
                            case "2":
                                String upcommingclient;
                                upcommingclient = in.readLine();
                                sendclient(upcommingclient);
                                //sendFile(outGoingFileName);
                                break;
                            default:
                                System.out.println("Incorrect command received.");
                                break;
                        }
                        in.close();
                        break;
                    }

                } catch (IOException ex) {
                   // Logger.getLogger(CLIENTConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            public void sendclient(String upcomming)
            {
                int i;
                if(upcomming.equalsIgnoreCase("Client 1"))
                {
                    for(i=1;i<Splitfile.numchunk;i+=5)
                    {

                    sendFile("chunk"+i,upcomming);

                    }

                }
                if(upcomming.equalsIgnoreCase("Client 2")) {
                    for(i=2;i<=Splitfile.numchunk;i+=5)

                       sendFile("chunk"+i,upcomming);

                }
                if(upcomming.equalsIgnoreCase("Client 3"))
                {
                    for(i=3;i<=Splitfile.numchunk;i+=5)


                        sendFile("chunk" + i, upcomming);

                }
                if(upcomming.equalsIgnoreCase("Client 4"))
                {
                    for(i=4;i<=Splitfile.numchunk;i+=5)

                        sendFile("chunk"+i,upcomming);

                }
                if(upcomming.equalsIgnoreCase("Client 5"))
                {
                    for(i=5;i<=Splitfile.numchunk;i+=5)

                        sendFile("chunk"+i,upcomming);

                }

            }


            public void sendFile(String fileName , String clientname) {
                try {
                    //handle file read
                    File myFile = new File(fileName);
                    byte[] mybytearray = new byte[(int) myFile.length()];

                    FileInputStream fis = new FileInputStream(myFile);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    //bis.read(mybytearray, 0, mybytearray.length);

                    DataInputStream dis = new DataInputStream(bis);
                    dis.readFully(mybytearray, 0, mybytearray.length);
                      //handle file send over socket
                    OutputStream os = clientSocket.getOutputStream();

                    DataOutputStream dos = new DataOutputStream(os);
                    dos.writeUTF(myFile.getName());
                    dos.writeLong(mybytearray.length);
                    dos.write(mybytearray, 0, mybytearray.length);

                    System.out.println("File " + fileName + " sent to " + clientname);
                    dos.flush();

                } catch (Exception e) {
                    System.err.println("File does not exist!");
                }
            }
          }
    }


















