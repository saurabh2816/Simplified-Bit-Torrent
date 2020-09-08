package client2;

import java.io.*;
import java.net.Socket;

/**
 * Created by ronak on 11/26/2015.
 */
public class Peer2connection implements Runnable{
    private static Socket csock;
    private String[] chunklist2;
    private ObjectOutputStream out;
    public BufferedReader br = null;
    public Peer2connection(Socket client, String[] list) {
        this.csock=client;
        this.chunklist2=list;
    }

    @Override
    public void run() {
        try {
            br = new BufferedReader(new InputStreamReader(csock.getInputStream()));
            out = new ObjectOutputStream(csock.getOutputStream());
            out.writeObject(chunklist2);

        } catch (IOException e) {
            System.out.println("Erroe"+e);
        }
        String temp;
        temp="hakuna matata";

        for (int i = 0; i < chunklist2.length; i++) {

            try {
                temp=br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (chunklist2[i] != null && temp.equals("yes"))
            {
                sendfile(chunklist2[i], "Client3");
            }
            else {
                System.out.println("No more new files to upload ");
            }
        }
        System.out.println("DONE uploading!!!");
        System.out.println("Waiting for more new files to arrive");
        // break;




    }

        public static void sendfile(String file, String client) {

            try {

                File myFile = new File(file);
                byte[] mybytearray = new byte[(int) myFile.length()];

                FileInputStream fis = new FileInputStream(myFile);
                BufferedInputStream bis = new BufferedInputStream(fis);

                DataInputStream dis = new DataInputStream(bis);
                dis.readFully(mybytearray, 0, mybytearray.length);

                OutputStream os = csock.getOutputStream();

                DataOutputStream dos = new DataOutputStream(os);
                dos.writeUTF(myFile.getName());
                dos.writeLong(mybytearray.length);
                dos.write(mybytearray, 0, mybytearray.length);
                dos.flush();

                System.out.println("File " + file + " send to " + client);

            } catch (Exception e) {
                System.out.println("Erroe" + e);
            }


        }

    }



