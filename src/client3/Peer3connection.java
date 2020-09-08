package client3;

import java.io.*;
import java.net.Socket;

/**
 * Created by ronak on 11/26/2015.
 */
public class Peer3connection implements Runnable {
    public static String[] chunklist3;
    public static Socket csock3;
    private ObjectOutputStream out;
    public BufferedReader br = null;
    public Peer3connection(Socket client, String[] list) {
        this.chunklist3=list;
        this.csock3=client;

    }

    @Override
    public void run() {

        try {
            br = new BufferedReader(new InputStreamReader(csock3.getInputStream()));
            out = new ObjectOutputStream(csock3.getOutputStream());
            out.writeObject(chunklist3);

        } catch (IOException e) {
            System.out.println("Erroe"+e);
        }
        String temp;
        temp="hakuna matata";

        for (int i = 0; i < chunklist3.length; i++) {
            try {
                temp=br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (chunklist3[i] != null && temp.equals("yes")) {
                sendfile(chunklist3[i], "Client4");
            }
            else {
                System.out.println("No more new files to upload");
            }



        }
        System.out.println("DONE uploading!!!");
        System.out.println("Waiting for more new files to arrive");
        try {
            csock3.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void sendfile(String file, String client) {

        try {

            File myFile = new File(file);
            byte[] mybytearray = new byte[(int) myFile.length()];

            FileInputStream fis = new FileInputStream(myFile);
            BufferedInputStream bis = new BufferedInputStream(fis);

            DataInputStream dis = new DataInputStream(bis);
            dis.readFully(mybytearray, 0, mybytearray.length);

            OutputStream os = csock3.getOutputStream();

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
